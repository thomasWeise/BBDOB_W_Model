package cn.edu.hfuu.iao.WModel;

/**
 * Testing the {@code boolean[]} objective version
 */
public class WModel_SingleObjective_Boolean_Test
    extends WModel_SingleObjective_Test<boolean[]> {

  /** {@inheritDoc} */
  @Override
  protected final WModel_SingleObjective<boolean[]> create(final int _n,
      final int _mu, final int _nu, final int _gamma) {
    return WModel_SingleObjective_Boolean.factory().create(_n, _mu, _nu,
        _gamma);
  }

  /** {@inheritDoc} */
  @Override
  protected final boolean[] fromString(final String str) {
    final int length = str.length();
    final boolean[] result = new boolean[length];
    for (int i = length; (--i) >= 0;) {
      result[i] = (str.charAt(i) == '1');
    }
    return result;
  }
}
