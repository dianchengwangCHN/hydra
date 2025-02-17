package hydra.langs.owl.syntax;

public class DataMaxCardinality {
  public static final hydra.core.Name NAME = new hydra.core.Name("hydra/langs/owl/syntax.DataMaxCardinality");
  
  public final java.math.BigInteger bound;
  
  public final hydra.langs.owl.syntax.DataPropertyExpression property;
  
  public final java.util.List<hydra.langs.owl.syntax.DataRange> range;
  
  public DataMaxCardinality (java.math.BigInteger bound, hydra.langs.owl.syntax.DataPropertyExpression property, java.util.List<hydra.langs.owl.syntax.DataRange> range) {
    this.bound = bound;
    this.property = property;
    this.range = range;
  }
  
  @Override
  public boolean equals(Object other) {
    if (!(other instanceof DataMaxCardinality)) {
      return false;
    }
    DataMaxCardinality o = (DataMaxCardinality) (other);
    return bound.equals(o.bound) && property.equals(o.property) && range.equals(o.range);
  }
  
  @Override
  public int hashCode() {
    return 2 * bound.hashCode() + 3 * property.hashCode() + 5 * range.hashCode();
  }
  
  public DataMaxCardinality withBound(java.math.BigInteger bound) {
    return new DataMaxCardinality(bound, property, range);
  }
  
  public DataMaxCardinality withProperty(hydra.langs.owl.syntax.DataPropertyExpression property) {
    return new DataMaxCardinality(bound, property, range);
  }
  
  public DataMaxCardinality withRange(java.util.List<hydra.langs.owl.syntax.DataRange> range) {
    return new DataMaxCardinality(bound, property, range);
  }
}