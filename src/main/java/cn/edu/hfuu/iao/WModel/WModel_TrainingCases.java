package cn.edu.hfuu.iao.WModel;

import java.util.Random;

/**
 * A class implementing the training cases concept. The {@code t} training
 * cases for bit strings of length {@code n} are represented in a compact
 * way as {@code long[n]}. The upper 32 bit at an index mark value to be
 * added to the objective if a bit at this index was {@code 1}, the lower
 * 32 bit mark the value to be added if the bit was {@code 0}.
 */
public class WModel_TrainingCases {

  /**
   * Create the training cases
   *
   * @param n
   *          the bit string length
   * @param epsilon
   *          the number of bits to flip per case
   * @param o
   *          the number of don't care symbols per case
   * @param t
   *          the number of training cases
   * @param random
   *          the random number generator
   * @return the training cases
   */
  public static final long[] training_cases_create(final int n,
      final int epsilon, final int o, final int t, final Random random) {
    final int[] perm = WModel_Permutation.canonical(n);

    // create the raw training cases
    final int[] ones = new int[n];
    final int[] zeros = new int[n];
    for (int i = n; (--i) >= 0;) {
      if ((i & 1) == 0) {
        ones[i] = t;
      } else {
        zeros[i] = t;
      }
    }

    // now modify them
    for (int caze = t; (--caze) >= 0;) {

      // first shuffle perm
      // by using the values of perm in sequence, we create non-repeating,
      // uniformly random indices
      WModel_Permutation.shuffle(perm, random);

      int z = 0;
      // add don't care
      for (int j = o; (--j) >= 0;) {
        final int i = perm[z++];
        // a don't care symbol basically reduces one of the original values
        if ((i & 1) == 0) {
          --ones[i];
        } else {
          --zeros[i];
        }
      }

      // add noise
      for (int j = epsilon; (--j) >= 0;) {
        final int i = perm[z++];
        // a don't care symbol basically increases the opposite values
        if ((i & 1) == 0) {
          ++zeros[i];
        } else {
          ++ones[i];
        }
      }
    }

    final long[] result = new long[n];
    for (int i = n; (--i) >= 0;) {
      result[i] = (((long) (ones[i]) << 32L) | zeros[i]);
    }

    return result;
  }

  /**
   * Get the value to be added to the objective value it the {@code index}
   * in the candidate solution has the bit value {@code bit}.
   *
   * @param bit
   *          the value of the bit
   * @param index
   *          the bit index
   * @param training_cases
   *          the training case
   * @return the result
   */
  public static final int summand(final boolean bit, final int index,
      final long[] training_cases) {
    return (bit ? ((int) (training_cases[index] >>> 32L))
        : ((int) (training_cases[index] & 0xffffffffL)));
  }

  /**
   * Compute the values that are covered by the training-case based
   * objective function. The first value in the list is the smallest
   * possible value that can be attained.
   *
   * @param training_cases
   *          the training cases
   * @param minMaxTrue
   *          {@code int[]} of length 3 to receive the minimally (index 0)
   *          and maximally (index 1) possible objective value, as well as
   *          the objective value of the actual optimum
   *          ({@code 0101010101...}) at index 2
   */
  public static final void training_cases_objective_value_range(
      final long[] training_cases, final int[] minMaxTrue) {
    minMaxTrue[0] = 0;
    minMaxTrue[1] = 0;
    minMaxTrue[2] = 0;
    for (int i = training_cases.length; (--i) >= 0;) {
      final int ones = ((int) (training_cases[i] >>> 32L));
      final int zeros = ((int) (training_cases[i] & 0xffffffffL));
      if (ones < zeros) {
        minMaxTrue[0] += ones;
        minMaxTrue[1] += zeros;
      } else {
        minMaxTrue[0] += zeros;
        minMaxTrue[1] += ones;
      }
      if ((i & 1) != 0) {
        minMaxTrue[2] += ones;
      } else {
        minMaxTrue[2] += zeros;
      }
    }
  }

  /**
   * Create a ruggedness transformation for the given training cases. You
   * should always use this transformation, even if no ruggedness is
   * required (then use {@code gamma=0}), as the returned permutation
   * guarantees that the objective value of the optimum will be mapped to
   * 0.
   *
   * @param gamma
   *          the gamma
   * @param training_cases
   *          the training cases
   * @return the transformation
   * @see WModel_Ruggedness#ruggedness(int, int)
   */
  public static final int[] ruggedness(final int gamma,
      final long[] training_cases) {
    final int[] minMaxTrue = new int[3];
    WModel_TrainingCases
        .training_cases_objective_value_range(training_cases, minMaxTrue);

    final int minSuccess = minMaxTrue[2];
    final int[] r = WModel_Ruggedness.ruggedness(gamma,
        minMaxTrue[1] - minSuccess);

    if (minSuccess <= 0) {
      return r;
    }

    final int minimal = minMaxTrue[0];
    final int shift = (minSuccess - minimal);

    if (shift > 0) {
      for (int i = r.length; (--i) > 0;) {
        r[i] += shift;
      }
    }

    final int[] nr = new int[minSuccess + r.length];
    System.arraycopy(r, 0, nr, minSuccess, r.length);

    if (shift > 0) {
      for (int j = 0, i = minimal; (++i) <= minSuccess;) {
        nr[i] = (++j);
      }
    }
    return nr;
  }

  /**
   * Create a set of training cases from a string list in compact
   * representation.
   *
   * @param cases
   *          the training cases as strings
   * @return the training cases
   */
  public static final long[] fromString(final String... cases) {
    final int n = cases[0].length();
    final int[] ones = new int[n];
    final int[] zeros = new int[n];
    for (final String caze : cases) {
      for (int i = n; (--i) >= 0;) {
        final char z = caze.charAt(i);
        switch (z) {
          case '1': {
            ++ones[i];
            break;
          }
          case '0': {
            ++zeros[i];
            break;
          }
          case '*': {
            break;
          }
          default: {
            throw new IllegalArgumentException("invalid: " + z); //$NON-NLS-1$
          }
        }
      }
    }
    final long[] res = new long[n];
    for (int i = n; (--i) >= 0;) {
      res[i] = ((((long) (zeros[i])) << 32L) | ones[i]);
    }
    return res;
  }
}
