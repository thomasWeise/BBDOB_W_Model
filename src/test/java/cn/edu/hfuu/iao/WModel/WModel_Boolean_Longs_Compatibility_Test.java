package cn.edu.hfuu.iao.WModel;

import org.junit.Assert;

import cn.edu.hfuu.iao.WModel.WModel_Longs.Solution;

/**
 * A test whether the {@code boolean[]} and {@code WModel_Longs}
 * representations are compatible
 */
public class WModel_Boolean_Longs_Compatibility_Test
    extends WModel_Compatibility_Test<boolean[], Solution> {

  /** {@inheritDoc} */
  @Override
  protected final void assertEqual(final boolean[] a, final Solution b) {
    Assert.assertEquals(a.length, b.size());
    for (int i = a.length; (--i) >= 0;) {
      Assert.assertTrue(a[i] == b.get(i));
    }
  }

  /** {@inheritDoc} */
  @Override
  protected final boolean[] fromString1(final String str) {
    final int length = str.length();
    final boolean[] result = new boolean[length];
    for (int i = length; (--i) >= 0;) {
      result[i] = (str.charAt(i) == '1');
    }
    return result;
  }

  /** {@inheritDoc} */
  @Override
  protected final String toString1(final boolean[] data) {
    final int length = data.length;
    final char[] txt = new char[length];
    for (int i = length; (--i) >= 0;) {
      txt[i] = data[i] ? '1' : '0';
    }
    return String.valueOf(txt);
  }

  /** {@inheritDoc} */
  @Override
  protected final boolean[] compute_neutrality1(final boolean[] in,
      final int mu) {
    final boolean[] res = new boolean[in.length / mu];
    WModel_Boolean.neutrality(in, mu, res);
    return res;
  }

  /** {@inheritDoc} */
  @Override
  protected final boolean[] compute_epistasis1(final boolean[] in,
      final int eta) {
    final boolean[] res = new boolean[in.length];
    WModel_Boolean.epistasis(in, eta, res);
    return res;
  }

  /** {@inheritDoc} */
  @Override
  protected final int compute_f1(final boolean[] in, final int n) {
    return WModel_Boolean.f(in, n);
  }

  /** {@inheritDoc} */
  @Override
  protected final boolean[][] compute_multi_objectives1(final boolean[] in,
      final int m, final int n) {
    final boolean[][] result = new boolean[m][n];
    WModel_Boolean.multi_objectivity(in, result);
    return result;
  }

  /** {@inheritDoc} */
  @Override
  protected final int compute_f_training_cases1(final boolean[] in,
      final long[] training) {
    return WModel_Boolean.f_training_cases(in, training);
  }

  /** {@inheritDoc} */
  @Override
  protected final Solution fromString2(final String str) {
    final int length = str.length();
    final Solution result = new Solution(length);
    for (int i = length; (--i) >= 0;) {
      result.set(i, (str.charAt(i) == '1'));
    }
    return result;
  }

  /** {@inheritDoc} */
  @Override
  protected final String toString2(final Solution data) {
    return data.toString();
  }

  /** {@inheritDoc} */
  @Override
  protected final Solution compute_neutrality2(final Solution in,
      final int mu) {
    final Solution res = new Solution(in.size() / mu);
    WModel_Longs.neutrality(in, mu, res);
    return res;
  }

  /** {@inheritDoc} */
  @Override
  protected final Solution compute_epistasis2(final Solution in,
      final int eta) {
    final Solution res = new Solution(in.size());
    WModel_Longs.epistasis(in, eta, res);
    return res;
  }

  /** {@inheritDoc} */
  @Override
  protected final int compute_f2(final Solution in, final int n) {
    return WModel_Longs.f(in, n);
  }

  /** {@inheritDoc} */
  @Override
  protected final Solution[] compute_multi_objectives2(final Solution in,
      final int m, final int n) {
    final Solution[] result = new Solution[m];
    for (int i = m; (--i) >= 0;) {
      result[i] = new Solution(n);
    }
    WModel_Longs.multi_objectivity(in, result);
    return result;
  }

  /** {@inheritDoc} */
  @Override
  protected final int compute_f_training_cases2(final Solution in,
      final long[] training) {
    return WModel_Longs.f_training_cases(in, training);
  }

  /** {@inheritDoc} */
  @Override
  protected final boolean[] compute_permutate1(final boolean[] in,
      final int[] permutation) {
    final boolean[] res = new boolean[in.length];
    WModel_Boolean.permutate(in, permutation, res);
    return res;
  }

  /** {@inheritDoc} */
  @Override
  protected final Solution compute_permutate2(final Solution in,
      final int[] permutation) {
    final Solution res = new Solution(in.size());
    WModel_Longs.permutate(in, permutation, res);
    return res;
  }
}
