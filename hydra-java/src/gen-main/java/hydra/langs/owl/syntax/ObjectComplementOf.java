package hydra.langs.owl.syntax;

public class ObjectComplementOf {
  public static final hydra.core.Name NAME = new hydra.core.Name("hydra/langs/owl/syntax.ObjectComplementOf");
  
  public final hydra.langs.owl.syntax.ClassExpression value;
  
  public ObjectComplementOf (hydra.langs.owl.syntax.ClassExpression value) {
    this.value = value;
  }
  
  @Override
  public boolean equals(Object other) {
    if (!(other instanceof ObjectComplementOf)) {
      return false;
    }
    ObjectComplementOf o = (ObjectComplementOf) (other);
    return value.equals(o.value);
  }
  
  @Override
  public int hashCode() {
    return 2 * value.hashCode();
  }
}