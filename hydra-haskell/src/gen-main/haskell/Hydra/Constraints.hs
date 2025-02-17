-- | A model for path- and pattern-based graph constraints, which may be considered as part of the schema of a graph

module Hydra.Constraints where

import qualified Hydra.Core as Core
import qualified Hydra.Query as Query
import Data.Int
import Data.List
import Data.Map
import Data.Set

-- | A declared equivalence between two abstract paths in a graph
data PathEquation = 
  PathEquation {
    pathEquationLeft :: Query.Path,
    pathEquationRight :: Query.Path}
  deriving (Eq, Ord, Read, Show)

_PathEquation = (Core.Name "hydra/constraints.PathEquation")

_PathEquation_left = (Core.FieldName "left")

_PathEquation_right = (Core.FieldName "right")

-- | A pattern which, if it matches in a given graph, implies that another pattern must also match. Query variables are shared between the two patterns.
data PatternImplication a = 
  PatternImplication {
    patternImplicationAntecedent :: (Query.Pattern a),
    patternImplicationConsequent :: (Query.Pattern a)}
  deriving (Eq, Ord, Read, Show)

_PatternImplication = (Core.Name "hydra/constraints.PatternImplication")

_PatternImplication_antecedent = (Core.FieldName "antecedent")

_PatternImplication_consequent = (Core.FieldName "consequent")