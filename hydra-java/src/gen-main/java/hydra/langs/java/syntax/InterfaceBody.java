package hydra.langs.java.syntax;

public class InterfaceBody {
  public static final hydra.core.Name NAME = new hydra.core.Name("hydra/langs/java/syntax.InterfaceBody");
  
  public final java.util.List<hydra.langs.java.syntax.InterfaceMemberDeclaration> value;
  
  public InterfaceBody (java.util.List<hydra.langs.java.syntax.InterfaceMemberDeclaration> value) {
    this.value = value;
  }
  
  @Override
  public boolean equals(Object other) {
    if (!(other instanceof InterfaceBody)) {
      return false;
    }
    InterfaceBody o = (InterfaceBody) (other);
    return value.equals(o.value);
  }
  
  @Override
  public int hashCode() {
    return 2 * value.hashCode();
  }
}