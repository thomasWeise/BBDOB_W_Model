package cn.edu.hfuu.iao.WModel;

import java.util.Random;

/**
 * The permutation transformation has the goal to separate decision
 * variables which might be interacting into different locations. We
 * propose a strength parameter {@code 1<=c<=n}, which defines the number
 * of positions that a {@linkplain #permutation(int, int, Random)
 * generated} permutation should differ from the canonical permutation.
 */
public final class WModel_Permutation {

  /**
   * Create a canonical permutation of length {@code n}.
   *
   * @param n
   *          the length of the permutation
   * @return the permutation
   */
  public static int[] canonical(final int n) {
    final int[] perm = new int[n];
    for (int i = n; (--i) >= 0;) {
      perm[i] = i;
    }
    return perm;
  }

  /**
   * Create a random permutation of length {@code n} where exactly
   * {@code c} elements are at different locations compared to the
   * canonical permutation. This is an idea for future inclusion in our
   * model, with the goal to reduce the proximity of interacting variables
   * in the solutions. A mapping based on the produced permutations could
   * be applied before the neutrality reduction.
   *
   * @param c
   *          the cycle length in 1..n
   * @param n
   *          the length of the representation
   * @param random
   *          the random number generator
   * @return the permutation
   */
  public static int[] permutation(final int n, final int c,
      final Random random) {
    final int[] perm = WModel_Permutation.canonical(n);

    if (c <= 1) {
      return perm;
    }

    final int[] apply = WModel_Permutation.canonical(c);
    final int[] indexes = perm.clone();

    makePermutation: for (;;) {
      WModel_Permutation.shuffle(apply, random);
      WModel_Permutation.shuffle(indexes, random);
      for (int i = c; (--i) >= 0;) {
        if ((indexes[apply[i]] == indexes[i])) {
          continue makePermutation;
        }
      }
      break makePermutation;
    }

    final int[] temp = new int[c];
    for (int i = c; (--i) >= 0;) {
      temp[i] = perm[indexes[i]];
    }
    for (int i = c; (--i) >= 0;) {
      perm[indexes[i]] = temp[apply[i]];
    }

    return perm;
  }

  /**
   * Parse a string of the form {@code 1, 2, 4, 3} into an integer array.
   *
   * @param str
   *          the string
   * @return the array
   */
  public static final int[] fromString(final String str) {
    final int length = str.length();
    int[] result;

    result = new int[length];
    int size = 0;
    int j = -1;
    while (j < length) {
      int i = str.indexOf(',', ++j);
      if (i < 0) {
        i = str.length();
      }
      result[size++] = Integer.parseInt(str.substring(j, i));
      j = i;
    }
    if (size < result.length) {
      final int[] t = new int[size];
      System.arraycopy(result, 0, t, 0, size);
      return t;
    }
    return result;
  }

  /**
   * Randomly shuffle a given permutation to be shuffled.
   *
   * @param perm
   *          the permutation
   * @param random
   *          the random number generator
   */
  public static final void shuffle(final int[] perm, final Random random) {
    final int n = perm.length;
    final int max = n - 1;
    for (int i = 0; i < max; i++) {
      final int j = (i + random.nextInt(n - i));
      final int temp = perm[i];
      perm[i] = perm[j];
      perm[j] = temp;
    }
  }
}
