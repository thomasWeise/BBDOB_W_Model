package cn.edu.hfuu.iao.WModel;

/**
 * Here we implement the {@code W model} for the
 * {@code boolean[]}-representations of candidate solutions.
 */
public final class WModel_Boolean {

  /**
   * Compute the objective value of a candidate solution, i.e., the Hamming
   * distance from the string {@code 010101010101}. This is the basic
   * problem objective function. It can be applied either directly, on top
   * of transformations such as the
   * {@linkplain #neutrality(boolean[], int, boolean[]) neutrality}- or
   * {@link #epistasis(boolean[], int, boolean[]) epistasis} mappings, or
   * after splitting the candidate solution into multiple piece to generate
   * a {@link #multi_objectivity(boolean[], boolean[][]) multi-objective
   * problem}. Its result can be transformed by a
   * {@link WModel_Ruggedness#ruggedness(int, int) ruggedness permutation}
   * to introduce ruggedness into the problem. If training case-based
   * evaluation is used, please use
   * {@link #f_training_cases(boolean[], long[])} instead.
   *
   * @param x
   *          the candidate solution
   * @param n
   *          the expected length
   * @return the objective value
   * @see WModel_Ruggedness#ruggedness(int, int)
   * @see #f_training_cases(boolean[], long[])
   */
  public static final int f(final boolean[] x, final int n) {
    int i = x.length;
    int result = 0;
    if (i > n) {
      i = n;
    } else {
      if (i < n) {
        result = (n - i);
      }
    }
    for (; (--i) >= 0;) {
      result += (((x[i] == ((i & 1) != 0)) ? 0 : 1));
    }
    return result;
  }

  /**
   * The neutrality transformation is the first layer of the
   * {@code W Model}. It introduces (or better, removes during the mapping)
   * uniform redundancy by basically reducing the size of a bit string by
   * factor {@code mu}.
   *
   * @param xIn
   *          the candidate solution
   * @param xOut
   *          the shortened candidate solution:
   *          {@code xOut.length >= xIn.length*mu}
   * @param mu
   *          the factor
   */
  public static final void neutrality(final boolean[] xIn, final int mu,
      final boolean[] xOut) {
    final int thresholdFor1 = (mu >>> 1);
    for (int i = 0, j = 0, ones = 0, flush = mu; (i < xIn.length)
        && (j < xOut.length);) {
      if (xIn[i]) {
        ++ones;
      }
      if ((++i) >= flush) {
        flush += mu;
        xOut[j++] = (ones >= thresholdFor1);
        ones = 0;
      }
    }
  }

  /**
   * The internal function for performing the epistasis transformation
   * {@code eta_nu} of a sub-string of length {@code nu} of {@code xIn}
   * starting at offset {@code start}, writing the result to {@code xOut}.
   *
   * @param xIn
   *          the input bit string
   * @param start
   *          the start index
   * @param nu
   *          the nu value
   * @param xOut
   *          the destination bit string
   */
  private static final void __epistasis(final boolean[] xIn,
      final int start, final int nu, final boolean[] xOut) {

    final int end = (start + nu) - 1;
    final boolean flip = xIn[start];

    for (int i = end, skip = start; i >= start; --i) {
      boolean result = flip;
      for (int j = end; j > start; --j) {
        if (j != skip) {
          result ^= (xIn[j]);
        }
      }
      xOut[i] = result;
      if ((--skip) < start) {
        skip = end;
      }
    }
  }

  /**
   * The second layer of the {@code W model} is the epistasis
   * transformation {@code eta_nu}. A string {@code xIn} is taken and
   * {@code eta_nu(xIn)} is written to {@code xOut}.
   *
   * @param xIn
   *          the input bit string
   * @param nu
   *          the nu value
   * @param xOut
   *          the destination bit string
   */
  public static final void epistasis(final boolean[] xIn, final int nu,
      final boolean[] xOut) {
    final int end = xIn.length - nu;
    int i = 0;
    for (; i <= end; i += nu) {
      WModel_Boolean.__epistasis(xIn, i, nu, xOut);
    }
    if (i < xIn.length) {
      WModel_Boolean.__epistasis(xIn, i, xIn.length - i, xOut);
    }
  }

  /**
   * Perform the multi-objectivity split. A candidate solution is split
   * into {@code xOut.length} pieces of equal size. Each of which can be
   * treated as single, independent optimization problem to which you can
   * apply {@link #f(boolean[], int)} (or
   * {@link #f_training_cases(boolean[], long[])}). Neutrality and
   * epistasis transformations should be carried out before the
   * multi-objectivity transformation, ruggedness mappings should be
   * applied to the objective values computed from the single problems
   * after the transformation.
   *
   * @param xIn
   *          the input bit string
   * @param xOut
   *          the destination bit strings
   */
  public static final void multi_objectivity(final boolean[] xIn,
      final boolean[][] xOut) {
    final int m = xOut.length;
    final int n = xOut[0].length; // assume that all have same length
    final int min = Math.min(xIn.length, (m * n));

    for (int i = 0; i < m; i++) {
      final boolean[] x = xOut[i];
      int j = 0;
      int k = i;
      for (; k < min; j++, k += m) {
        x[j] = xIn[k];
      }
      for (; j < n; j++) {
        x[j] = ((j & 1) == 0);
      }
    }
  }

  /**
   * Compute the objective value of a candidate solution based on training
   * cases. The result of this function should always be transformed using
   * a mapping created by
   * {@link WModel_TrainingCases#ruggedness(int, long[])}. This method
   * replaces the {@link #f(boolean[], int)} objective function if training
   * cases are used (see
   * {@link WModel_TrainingCases#training_cases_create(int, int, int, int, java.util.Random)}
   * and {@link WModel_TrainingCases#ruggedness(int, long[])})
   *
   * @param x
   *          the candidate solution
   * @param training
   *          the training cases
   * @return the objective value
   * @see WModel_TrainingCases#training_cases_create(int, int, int, int,
   *      java.util.Random)
   * @see WModel_TrainingCases#ruggedness(int, long[])
   * @see #f(boolean[], int)
   */
  public static final int f_training_cases(final boolean[] x,
      final long[] training) {
    int sum = 0;
    for (int i = training.length; (--i) >= 0;) {
      sum += WModel_TrainingCases.summand(x[i], i, training);
    }
    return sum;
  }
}
