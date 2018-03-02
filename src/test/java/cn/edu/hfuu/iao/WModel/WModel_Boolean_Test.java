package cn.edu.hfuu.iao.WModel;

import java.util.Arrays;

import org.junit.Assert;

/** A test for {@code boolean[]} representation */
public class WModel_Boolean_Test extends WModel_Test<boolean[]> {

  /** {@inheritDoc} */
  @Override
  protected final boolean equals(final boolean[] a, final boolean[] b) {
    return Arrays.equals(a, b);
  }

  /** {@inheritDoc} */
  @Override
  protected final void assertEqual(final boolean[] a, final boolean[] b) {
    Assert.assertArrayEquals(a, b);
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

  /** {@inheritDoc} */
  @Override
  protected final String toString(final boolean[] data) {
    final int length = data.length;
    final char[] txt = new char[length];
    for (int i = length; (--i) >= 0;) {
      txt[i] = data[i] ? '1' : '0';
    }
    return String.valueOf(txt);
  }

  /** {@inheritDoc} */
  @Override
  protected final boolean[] compute_neutrality(final boolean[] in,
      final int mu) {
    final boolean[] res = new boolean[in.length / mu];
    WModel_Boolean.neutrality(in, mu, res);
    return res;
  }

  /** {@inheritDoc} */
  @Override
  protected final boolean[] compute_epistasis(final boolean[] in,
      final int nu) {
    final boolean[] res = new boolean[in.length];
    WModel_Boolean.epistasis(in, nu, res);
    return res;
  }

  /** {@inheritDoc} */
  @Override
  protected final int compute_f(final boolean[] in, final int n) {
    return WModel_Boolean.f(in, n);
  }

  /** {@inheritDoc} */
  @Override
  protected final boolean[][] compute_multi_objectives(final boolean[] in,
      final int m, final int n) {
    final boolean[][] result = new boolean[m][n];
    WModel_Boolean.multi_objectivity(in, result);
    return result;
  }

  /** {@inheritDoc} */
  @Override
  protected final int compute_f_training_cases(final boolean[] in,
      final long[] training) {
    return WModel_Boolean.f_training_cases(in, training);
  }

  /** {@inheritDoc} */
  @Override
  protected final boolean[] compute_permutate(final boolean[] in,
      final int[] permutation) {
    final boolean[] result = new boolean[in.length];
    WModel_Boolean.permutate(in, permutation, result);
    return result;
  }
}
