package hydra.phantoms;

/**
 * A pure association with a phantom type
 */
public class Reference<A> {
  public static final hydra.core.Name NAME = new hydra.core.Name("hydra/phantoms.Reference");
  
  public Reference () {
  
  }
  
  @Override
  public boolean equals(Object other) {
    if (!(other instanceof Reference)) {
      return false;
    }
    Reference o = (Reference) (other);
    return true;
  }
  
  @Override
  public int hashCode() {
    return 0;
  }
}