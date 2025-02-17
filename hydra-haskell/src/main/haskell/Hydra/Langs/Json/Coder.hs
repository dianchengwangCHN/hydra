module Hydra.Langs.Json.Coder (jsonCoder, literalJsonCoder, untypedTermToJson) where

import Hydra.Core
import Hydra.Compute
import Hydra.Graph
import Hydra.Flows
import Hydra.Strip
import Hydra.Basics
import Hydra.Tier1
import Hydra.Tier2
import Hydra.Adapters
import Hydra.TermAdapters
import Hydra.Langs.Json.Language
import Hydra.Lib.Literals
import Hydra.AdapterUtils
import qualified Hydra.Langs.Json.Model as Json
import qualified Hydra.Dsl.Terms as Terms
import qualified Hydra.Dsl.Types as Types

import qualified Control.Monad as CM
import qualified Data.Map as M
import qualified Data.Maybe as Y


jsonCoder :: (Eq a, Ord a, Read a, Show a) => Type a -> GraphFlow a (Coder (Graph a) (Graph a) (Term a) Json.Value)
jsonCoder typ = do
  adapter <- languageAdapter jsonLanguage typ
  coder <- termCoder $ adapterTarget adapter
  return $ composeCoders (adapterCoder adapter) coder

literalJsonCoder :: LiteralType -> GraphFlow a (Coder (Graph a) (Graph a) Literal Json.Value)
literalJsonCoder at = pure $ case at of
  LiteralTypeBoolean -> Coder {
    coderEncode = \(LiteralBoolean b) -> pure $ Json.ValueBoolean b,
    coderDecode = \s -> case s of
      Json.ValueBoolean b -> pure $ LiteralBoolean b
      _ -> unexpected "boolean" s}
  LiteralTypeFloat _ -> Coder {
    coderEncode = \(LiteralFloat (FloatValueBigfloat f)) -> pure $ Json.ValueNumber f,
    coderDecode = \s -> case s of
      Json.ValueNumber f -> pure $ LiteralFloat $ FloatValueBigfloat f
      _ -> unexpected "number" s}
  LiteralTypeInteger _ -> Coder {
    coderEncode = \(LiteralInteger (IntegerValueBigint i)) -> pure $ Json.ValueNumber $ bigintToBigfloat i,
    coderDecode = \s -> case s of
      Json.ValueNumber f -> pure $ LiteralInteger $ IntegerValueBigint $ bigfloatToBigint f
      _ -> unexpected "number" s}
  LiteralTypeString -> Coder {
    coderEncode = \(LiteralString s) -> pure $ Json.ValueString s,
    coderDecode = \s -> case s of
      Json.ValueString s' -> pure $ LiteralString s'
      _ -> unexpected "string" s}

recordCoder :: (Eq a, Ord a, Read a, Show a) => RowType a -> GraphFlow a (Coder (Graph a) (Graph a) (Term a) Json.Value)
recordCoder rt = do
    coders <- CM.mapM (\f -> (,) <$> pure f <*> termCoder (fieldTypeType f)) (rowTypeFields rt)
    return $ Coder (encode coders) (decode coders)
  where
    encode coders term = case stripTerm term of
      TermRecord (Record _ fields) -> Json.ValueObject . M.fromList . Y.catMaybes <$> CM.zipWithM encodeField coders fields
        where
          encodeField (ft, coder) (Field fname fv) = case (fieldTypeType ft, fv) of
            (TypeOptional _, TermOptional Nothing) -> pure Nothing
            _ -> Just <$> ((,) <$> pure (unFieldName fname) <*> coderEncode coder fv)
      _ -> unexpected "record" term
    decode coders n = case n of
      Json.ValueObject m -> Terms.record (rowTypeTypeName rt) <$> CM.mapM (decodeField m) coders -- Note: unknown fields are ignored
        where
          decodeField a (FieldType fname _, coder) = do
            v <- coderDecode coder $ Y.fromMaybe Json.ValueNull $ M.lookup (unFieldName fname) m
            return $ Field fname v
      _ -> unexpected "mapping" n
    getCoder coders fname = Y.maybe error pure $ M.lookup fname coders
      where
        error = fail $ "no such field: " ++ fname

termCoder :: (Eq a, Ord a, Read a, Show a) => Type a -> GraphFlow a (Coder (Graph a) (Graph a) (Term a) Json.Value)
termCoder typ = case stripType typ of
  TypeLiteral at -> do
    ac <- literalJsonCoder at
    return Coder {
      coderEncode = \(TermLiteral av) -> coderEncode ac av,
      coderDecode = \n -> case n of
        s -> Terms.literal <$> coderDecode ac s}
  TypeList lt -> do
    lc <- termCoder lt
    return Coder {
      coderEncode = \(TermList els) -> Json.ValueArray <$> CM.mapM (coderEncode lc) els,
      coderDecode = \n -> case n of
        Json.ValueArray nodes -> Terms.list <$> CM.mapM (coderDecode lc) nodes
        _ -> unexpected "sequence" n}
  TypeOptional ot -> do
    oc <- termCoder ot
    return Coder {
      coderEncode = \t -> case t of
        TermOptional el -> Y.maybe (pure Json.ValueNull) (coderEncode oc) el
        _ -> unexpected "optional term" t,
      coderDecode = \n -> case n of
        Json.ValueNull -> pure $ Terms.optional Nothing
        _ -> Terms.optional . Just <$> coderDecode oc n}
  TypeMap (MapType kt vt) -> do
      kc <- termCoder kt
      vc <- termCoder vt
      cx <- getState
      let encodeEntry (k, v) = (,) (toString cx k) <$> coderEncode vc v
      let decodeEntry (k, v) = (,) (fromString cx k) <$> coderDecode vc v
      return Coder {
        coderEncode = \(TermMap m) -> Json.ValueObject . M.fromList <$> CM.mapM encodeEntry (M.toList m),
        coderDecode = \n -> case n of
          Json.ValueObject m -> Terms.map . M.fromList <$> CM.mapM decodeEntry (M.toList m)
          _ -> unexpected "mapping" n}
    where
      toString cx v = if isStringKey cx
        then case stripTerm v of
          TermLiteral (LiteralString s) -> s
        else show v
      fromString cx s = Terms.string $ if isStringKey cx then s else read s
      isStringKey cx = stripType kt == Types.string
  TypeRecord rt -> recordCoder rt
  TypeVariable name -> return $ Coder encode decode
    where
      encode term = pure $ Json.ValueString $ show term
      decode term = fail $ "type variable " ++ unName name ++ " does not support decoding"
  _ -> fail $ "unsupported type in JSON: " ++ show (typeVariant typ)

-- | A simplistic, unidirectional encoding for terms as JSON values. Not type-aware; best used for human consumption.
untypedTermToJson :: Show a => Term a -> Flow s Json.Value
untypedTermToJson term = case stripTerm term of
      TermList terms -> Json.ValueArray <$> (CM.mapM untypedTermToJson terms)
      TermLiteral lit -> pure $ case lit of
        LiteralBinary s -> Json.ValueString s
        LiteralBoolean b -> Json.ValueBoolean b
        LiteralFloat f -> Json.ValueNumber $ floatValueToBigfloat f
        LiteralInteger i -> Json.ValueNumber $ bigintToBigfloat $ integerValueToBigint i
        LiteralString s -> Json.ValueString s
      TermRecord (Record _ fields) -> do
        keyvals <- CM.mapM fieldToKeyval fields
        return $ Json.ValueObject $ M.fromList $ Y.catMaybes keyvals
      TermUnion (Injection _ field) -> do
        mkeyval <- fieldToKeyval field
        return $ Json.ValueObject $ M.fromList $ case mkeyval of
          Nothing -> []
          Just keyval -> [keyval]
      t -> unexpected "literal value" t
  where
    fieldToKeyval f = do
        mjson <- forTerm $ fieldTerm f
        return $ case mjson of
          Nothing -> Nothing
          Just j -> Just (unFieldName $ fieldName f, j)
      where
        forTerm t = case t of
          TermOptional mt -> case mt of
            Nothing -> pure Nothing
            Just t' -> forTerm t'
          t' -> Just <$> untypedTermToJson t'