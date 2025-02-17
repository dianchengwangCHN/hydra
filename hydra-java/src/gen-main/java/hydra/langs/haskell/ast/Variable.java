package hydra.langs.haskell.ast;

public class Variable {
  public static final hydra.core.Name NAME = new hydra.core.Name("hydra/langs/haskell/ast.Variable");
  
  public final hydra.langs.haskell.ast.Name value;
  
  public Variable (hydra.langs.haskell.ast.Name value) {
    this.value = value;
  }
  
  @Override
  public boolean equals(Object other) {
    if (!(other instanceof Variable)) {
      return false;
    }
    Variable o = (Variable) (other);
    return value.equals(o.value);
  }
  
  @Override
  public int hashCode() {
    return 2 * value.hashCode();
  }
}