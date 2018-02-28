package cn.edu.hfuu.iao.WModel;

import java.util.Arrays;

/**
 * Here we implement the {@code W model} for the
 * {@code long[]}-representations of candidate solutions. This
 * representation is more compact than the {@linkplain WModel_Boolean
 * boolean representation}, as it can store 64 bits in one {@code long} of
 * its internal array. However, it may also be slower due to the overhead
 * of using methods when accessing the bits. Generally, having both a
 * boolean and a long-based representation allows us to exercise unit tests
 * for representation compatibility.
 */
public final class WModel_Longs {

  /**
   * Compute the objective value of a candidate solution, i.e., the Hamming
   * distance from the string {@code 010101010101}. This is the basic
   * problem objective function. It can be applied either directly, on top
   * of transformations such as the
   * {@linkplain #neutrality(Solution, int, Solution) neutrality}- or
   * {@link #epistasis(Solution, int, Solution) epistasis} mappings, or
   * after splitting the candidate solution into multiple piece to generate
   * a {@link #multi_objectivity(Solution, Solution[]) multi-objective
   * problem}. Its result can be transformed by a
   * {@link WModel_Ruggedness#ruggedness(int, int) ruggedness permutation}
   * to introduce ruggedness into the problem. If training case-based
   * evaluation is used, please use
   * {@link #f_training_cases(Solution, long[])} instead.
   *
   * @param x
   *          the candidate solution
   * @param n
   *          the expected length
   * @return the objective value
   * @see WModel_Ruggedness#ruggedness(int, int)
   * @see #f_training_cases(Solution, long[])
   */
  public static final int f(final Solution x, final int n) {
    int i = x.m_length;
    int result = 0;
    if (i > n) {
      i = n;
    } else {
      if (i < n) {
        result = (n - i);
      }
    }
    for (; (--i) >= 0;) {
      result += (((x.get(i) == ((i & 1) != 0)) ? 0 : 1));
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
  public static final void neutrality(final Solution xIn, final int mu,
      final Solution xOut) {
    final int thresholdFor1 = (mu >>> 1);
    for (int i = 0, j = 0, ones = 0, flush = mu; (i < xIn.m_length)
        && (j < xOut.m_length);) {
      if (xIn.get(i)) {
        ++ones;
      }
      if ((++i) >= flush) {
        flush += mu;
        xOut.set(j++, (ones >= thresholdFor1));
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
  private static final void __epistasis(final Solution xIn,
      final int start, final int nu, final Solution xOut) {

    final int end = (start + nu) - 1;
    final boolean flip = xIn.get(start);

    for (int i = end, skip = start; i >= start; --i) {
      boolean result = flip;
      for (int j = end; j > start; --j) {
        if (j != skip) {
          result ^= (xIn.get(j));
        }
      }
      xOut.set(i, result);
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
  public static final void epistasis(final Solution xIn, final int nu,
      final Solution xOut) {
    final int end = xIn.m_length - nu;
    int i = 0;
    for (; i <= end; i += nu) {
      WModel_Longs.__epistasis(xIn, i, nu, xOut);
    }
    if (i < xIn.m_length) {
      WModel_Longs.__epistasis(xIn, i, xIn.m_length - i, xOut);
    }
  }

  /**
   * Perform the multi-objectivity split. A candidate solution is split
   * into {@code xOut.length} pieces of equal size. Each of which can be
   * treated as single, independent optimization problem to which you can
   * apply {@link #f(Solution, int)} (or
   * {@link #f_training_cases(Solution, long[])}). Neutrality and epistasis
   * transformations should be carried out before the multi-objectivity
   * transformation, ruggedness mappings should be applied to the objective
   * values computed from the single problems after the transformation.
   *
   * @param xIn
   *          the input bit string
   * @param xOut
   *          the destination bit strings
   */
  public static final void multi_objectivity(final Solution xIn,
      final Solution[] xOut) {
    final int m = xOut.length;
    final int n = xOut[0].m_length; // assume that all have same length
    final int min = Math.min(xIn.m_length, (m * n));

    for (int i = 0; i < m; i++) {
      final Solution x = xOut[i];
      int j = 0;
      int k = i;
      for (; k < min; j++, k += m) {
        x.set(j, xIn.get(k));
      }
      for (; j < n; j++) {
        x.set(j, ((j & 1) == 0));
      }
    }
  }

  /**
   * Compute the objective value of a candidate solution based on training
   * cases. The result of this function should always be transformed using
   * a mapping created by
   * {@link WModel_TrainingCases#ruggedness(int, long[])}. This method
   * replaces the {@link #f(Solution, int)} objective function if training
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
   * @see #f(Solution, int)
   */
  public static final int f_training_cases(final Solution x,
      final long[] training) {
    int sum = 0;
    for (int i = training.length; (--i) >= 0;) {
      sum += WModel_TrainingCases.summand(x.get(i), i, training);
    }
    return sum;
  }

  /** a class for representing a solution as {@code long} array */
  public static final class Solution {
    /** the bits */
    final long[] m_data;
    /** the length */
    final int m_length;

    /**
     * create
     *
     * @param data
     *          the data
     * @param length
     *          the length
     */
    Solution(final long[] data, final int length) {
      super();
      this.m_data = data;
      this.m_length = length;
    }

    /**
     * Create a new candidate solution record of the given length
     *
     * @param length
     *          the length
     */
    public Solution(final int length) {
      this(new long[((length + 63) >>> 6)], length);
    }

    /** {@inheritDoc} */
    @Override
    public final Solution clone() {
      return new Solution(this.m_data.clone(), this.m_length);
    }

    /** Clear this solution */
    public final void clear() {
      Arrays.fill(this.m_data, 0L);
    }

    /**
     * get the size of the data structure
     *
     * @return the size of the data structure
     */
    public final int size() {
      return this.m_length;
    }

    /**
     * Get the value of the bit at the given index
     *
     * @param index
     *          the index
     * @return the value of the bit at that index
     */
    public final boolean get(final int index) {
      return (this.m_length > index)
          ? ((this.m_data[index >>> 6] & (1L << (index & 63L))) != 0L)
          : false;
    }

    /**
     * Set the value of the bit at the given index
     *
     * @param index
     *          the index
     * @param value
     *          the value to set
     */
    public final void set(final int index, final boolean value) {
      if (this.m_length > index) {
        if (value) {
          this.m_data[index >>> 6] |= (1L << (index & 63L));
        } else {
          this.m_data[index >>> 6] &= (~(1L << (index & 63L)));
        }
      } else {
        throw new IndexOutOfBoundsException(//
            "invalid index " + index //$NON-NLS-1$
                + " for bits of length " + this.m_length);//$NON-NLS-1$
      }
    }

    /** {@inheritDoc} */
    @Override
    public final String toString() {
      final char[] array = new char[this.m_length];
      int index = 0;
      looper: for (final long value : this.m_data) {
        for (long mask = 1L; mask != 0L; mask <<= 1L) {
          array[index] = (((value & mask) != 0) ? '1' : '0');
          if ((++index) >= array.length) {
            break looper;
          }
        }
      }
      return new String(array);
    }

    /** {@inheritDoc} */
    @Override
    public final int hashCode() {
      return Arrays.hashCode(this.m_data);
    }

    /** {@inheritDoc} */
    @Override
    public final boolean equals(final Object o) {
      if (o == this) {
        return true;
      }
      if (o instanceof Solution) {
        final Solution x = ((Solution) o);
        if (x.m_length == this.m_length) {
          return Arrays.equals(this.m_data, x.m_data);
        }
      }
      return false;
    }

    /**
     * copy another solution
     *
     * @param other
     *          the other solution
     */
    public final void copy(final Solution other) {
      if (other.m_length != this.m_length) {
        throw new IllegalArgumentException(
            "Solution lengths are different."); //$NON-NLS-1$
      }
      System.arraycopy(other.m_data, 0, this.m_data, 0,
          this.m_data.length);
    }
  }

  /**
   * Mix the the decision variables in a bit string {@code xIn} based on
   * the provided
   * {@link WModel_Permutation#permutation(int, int, java.util.Random)
   * permutation}. This transformation could be applied before the
   * neutrality transformation in order to make the problems harder for
   * solvers that use the variable sequence information. It should not make
   * the problem harder for solvers that do not.
   *
   * @param xIn
   *          the input bit string
   * @param permutation
   *          the permutation
   * @param xOut
   *          the output bit string
   * @see WModel_Permutation#permutation(int, int, java.util.Random)
   */
  public static final void permutate(final Solution xIn,
      final int[] permutation, final Solution xOut) {
    int j = (-1);
    for (final int i : permutation) {
      xOut.set(i, xIn.get(++j));
    }
  }
}
