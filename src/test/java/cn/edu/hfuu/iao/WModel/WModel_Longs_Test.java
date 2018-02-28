package cn.edu.hfuu.iao.WModel;

import java.util.Objects;

import org.junit.Assert;

import cn.edu.hfuu.iao.WModel.WModel_Longs.Solution;

/** A test for {@code WModel_Longs} representation */
public class WModel_Longs_Test extends WModel_Test<WModel_Longs.Solution> {

  /** {@inheritDoc} */
  @Override
  protected final boolean equals(final Solution a, final Solution b) {
    return Objects.equals(a, b);
  }

  /** {@inheritDoc} */
  @Override
  protected final void assertEqual(final Solution a, final Solution b) {
    Assert.assertEquals(a, b);
  }

  /** {@inheritDoc} */
  @Override
  protected final Solution fromString(final String str) {
    final int length = str.length();
    final Solution result = new Solution(length);
    for (int i = length; (--i) >= 0;) {
      result.set(i, (str.charAt(i) == '1'));
    }
    return result;
  }

  /** {@inheritDoc} */
  @Override
  protected final String toString(final Solution data) {
    return data.toString();
  }

  /** {@inheritDoc} */
  @Override
  protected final Solution compute_neutrality(final Solution in,
      final int mu) {
    final Solution res = new Solution(in.size() / mu);
    WModel_Longs.neutrality(in, mu, res);
    return res;
  }

  /** {@inheritDoc} */
  @Override
  protected final Solution compute_epistasis(final Solution in,
      final int eta) {
    final Solution res = new Solution(in.size());
    WModel_Longs.epistasis(in, eta, res);
    return res;
  }

  /** {@inheritDoc} */
  @Override
  protected final int compute_f(final Solution in, final int n) {
    return WModel_Longs.f(in, n);
  }

  /** {@inheritDoc} */
  @Override
  protected final Solution[] compute_multi_objectives(final Solution in,
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
  protected final int compute_f_training_cases(final Solution in,
      final long[] training) {
    return WModel_Longs.f_training_cases(in, training);
  }

  /** {@inheritDoc} */
  @Override
  protected final Solution compute_permutate(final Solution in,
      final int[] permutation) {
    final Solution result = new Solution(in.size());
    WModel_Longs.permutate(in, permutation, result);
    return result;
  }
}
