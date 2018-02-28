package cn.edu.hfuu.iao.WModel;

/**
 * The ruggedness transformation is a transformation of the objective
 * values and thus independent of the model layers. We put it into a
 * separate class.
 */
public final class WModel_Ruggedness {

  /**
   * Create the ruggedness permutation where groups of rugged
   * transformations alternate with deceptive permutations for increasing
   * gamma.
   *
   * @param gamma
   *          the gamma value
   * @param q
   *          the maximum possible objective value (equivalent to {@code n}
   *          if no training-case based evaluation is applied)
   * @return the permutation
   */
  public static int[] ruggedness_raw(final int gamma, final int q) {
    int j, k, start, max;

    final int[] r = new int[q + 1];

    max = WModel_Ruggedness.max_gamma(q);// (q * (q - 1)) >>> 1;

    if (gamma <= 0) {
      start = 0;
    } else {
      start = q - 1 - ((int) (0.5d + //
          Math.sqrt(0.25 + ((max - gamma) << 1))));
    }

    k = 0;
    for (j = 1; j <= start; j++) {
      r[j] = ((j & 1) != 0) ? (q - k) : (++k);
    }
    for (; j <= q; j++) {
      ++k;
      r[j] = ((start & 1) != 0) ? (q - k) : k;
    }

    final int upper = ((gamma - max)
        + (((q - start - 1) * (q - start)) >>> 1));
    --j;
    for (int i = 1; i <= upper; i++) {
      final int t = r[--j];
      r[j] = r[q];
      r[q] = t;
    }
    return r;
  }

  /**
   * translate the gamma value
   *
   * @param gamma
   *          the value of gamma'
   * @param q
   *          the maximum possible objective value (equivalent to {@code n}
   *          if no training-case based evaluation is applied)
   * @return the return value
   */
  public static final int ruggedness_translate(final int gamma,
      final int q) {
    int j, k, max, g, lastUpper;

    if (gamma <= 0) {
      return 0;
    }
    g = gamma;
    max = WModel_Ruggedness.max_gamma(q);// (q * (q - 1)) >>> 1;
    lastUpper = ((q >>> 1) * ((q + 1) >>> 1));
    if (g <= lastUpper) {
      j = (int) (((q + 2) * 0.5d)
          - Math.sqrt((((q * q) * 0.25d) + 1) - g));

      k = ((g - ((q + 2) * j)) + (j * j) + q);
      return ((k + 1 + ((((q + 2) * j) - (j * j) - q - 1) << 1))
          - (j - 1));
    }

    j = (int) ((((q % 2) + 1) * 0.5d)
        + Math.sqrt((((1 - (q % 2)) * 0.25d) + g) - 1 - lastUpper));

    k = g - (((j - (q % 2)) * (j - 1)) + 1 + lastUpper);

    return (max - k - ((2 * j * j) - j) - ((q % 2) * ((-2 * j) + 1)));
  }

  /**
   * Create the ruggedness permutation where all rugged transformations
   * come first and afterwards the deceptive permutations come.
   *
   * @param gamma
   *          the gamma prime value
   * @param q
   *          the maximum possible objective value (equivalent to {@code n}
   *          if no training-case based evaluation is applied)
   * @return the permutation
   */
  public static final int[] ruggedness(final int gamma, final int q) {
    return WModel_Ruggedness.ruggedness_raw(
        WModel_Ruggedness.ruggedness_translate(gamma, q), q);
  }

  /**
   * Get the maximum gamma value for a given maximum objective value.
   * {@code gamma} can then range from {@code 0..max_gamma}.
   *
   * @param q
   *          the q, i.e., the maximum objective value.
   * @return the maximum gamma value
   */
  public static final int max_gamma(final int q) {
    return ((q * (q - 1)) >>> 1);
  }
}
