package hydra.langs.sql.ansi;

public class ReferenceResolution {
  public static final hydra.core.Name NAME = new hydra.core.Name("hydra/langs/sql/ansi.ReferenceResolution");
  
  public ReferenceResolution () {
  
  }
  
  @Override
  public boolean equals(Object other) {
    if (!(other instanceof ReferenceResolution)) {
      return false;
    }
    ReferenceResolution o = (ReferenceResolution) (other);
    return true;
  }
  
  @Override
  public int hashCode() {
    return 0;
  }
}