package hydra.langs.xml.schema;

public class Int {
  public static final hydra.core.Name NAME = new hydra.core.Name("hydra/langs/xml/schema.Int");
  
  public final Integer value;
  
  public Int (Integer value) {
    this.value = value;
  }
  
  @Override
  public boolean equals(Object other) {
    if (!(other instanceof Int)) {
      return false;
    }
    Int o = (Int) (other);
    return value.equals(o.value);
  }
  
  @Override
  public int hashCode() {
    return 2 * value.hashCode();
  }
}