package hydra.langs.graphql.syntax;

public class ObjectTypeExtension_Sequence3 {
  public static final hydra.core.Name NAME = new hydra.core.Name("hydra/langs/graphql/syntax.ObjectTypeExtension.Sequence3");
  
  public final hydra.langs.graphql.syntax.Name name;
  
  public final hydra.langs.graphql.syntax.ImplementsInterfaces implementsInterfaces;
  
  public ObjectTypeExtension_Sequence3 (hydra.langs.graphql.syntax.Name name, hydra.langs.graphql.syntax.ImplementsInterfaces implementsInterfaces) {
    this.name = name;
    this.implementsInterfaces = implementsInterfaces;
  }
  
  @Override
  public boolean equals(Object other) {
    if (!(other instanceof ObjectTypeExtension_Sequence3)) {
      return false;
    }
    ObjectTypeExtension_Sequence3 o = (ObjectTypeExtension_Sequence3) (other);
    return name.equals(o.name) && implementsInterfaces.equals(o.implementsInterfaces);
  }
  
  @Override
  public int hashCode() {
    return 2 * name.hashCode() + 3 * implementsInterfaces.hashCode();
  }
  
  public ObjectTypeExtension_Sequence3 withName(hydra.langs.graphql.syntax.Name name) {
    return new ObjectTypeExtension_Sequence3(name, implementsInterfaces);
  }
  
  public ObjectTypeExtension_Sequence3 withImplementsInterfaces(hydra.langs.graphql.syntax.ImplementsInterfaces implementsInterfaces) {
    return new ObjectTypeExtension_Sequence3(name, implementsInterfaces);
  }
}