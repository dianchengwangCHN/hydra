package hydra.core;

/**
 * The name of a field, unique within a record or union type
 */
public class FieldName {
  public static final hydra.core.Name NAME = new hydra.core.Name("hydra/core.FieldName");
  
  public final String value;
  
  public FieldName (String value) {
    this.value = value;
  }
  
  @Override
  public boolean equals(Object other) {
    if (!(other instanceof FieldName)) {
      return false;
    }
    FieldName o = (FieldName) (other);
    return value.equals(o.value);
  }
  
  @Override
  public int hashCode() {
    return 2 * value.hashCode();
  }
}