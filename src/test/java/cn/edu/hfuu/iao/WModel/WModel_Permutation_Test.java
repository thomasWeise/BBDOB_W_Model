package cn.edu.hfuu.iao.WModel;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.Assert;
import org.junit.Test;

/** A test class for training cases */
public class WModel_Permutation_Test extends _Internal_Base {

  /** test the from string method for permutations of length 1 */
  @SuppressWarnings("static-method")
  @Test(timeout = 3600000)
  public void fromString_1() {
    Assert.assertArrayEquals(new int[1],
        WModel_Permutation.fromString("0")); //$NON-NLS-1$
  }

  /** test the from string method for permutations of length 2 */
  @SuppressWarnings("static-method")
  @Test(timeout = 3600000)
  public void fromString_2() {
    for (int i = 0; i <= 1; i++) {
      for (int j = 0; j <= 1; j++) {
        if (i != j) {
          Assert.assertArrayEquals(new int[] { i, j },
              WModel_Permutation.fromString(i + "," + j)); //$NON-NLS-1$
        }
      }
    }
  }

  /** test the from string method for permutations of length 3 */
  @SuppressWarnings("static-method")
  @Test(timeout = 3600000)
  public void fromString_3() {
    for (int i = 0; i <= 2; i++) {
      for (int j = 0; j <= 2; j++) {
        if (i != j) {
          for (int k = 0; k < 2; k++) {
            if ((k != i) && (k != j)) {
              Assert.assertArrayEquals(new int[] { i, j, k },
                  WModel_Permutation.fromString(((i + "," + j) //$NON-NLS-1$
                      + ',') + k));
            }
          }
        }
      }
    }
  }

  /** test the from string method for permutations of length 4 */
  @SuppressWarnings("static-method")
  @Test(timeout = 3600000)
  public void fromString_4() {
    for (int i = 0; i <= 3; i++) {
      for (int j = 0; j <= 3; j++) {
        if (i != j) {
          for (int k = 0; k < 3; k++) {
            if ((k != i) && (k != j)) {
              for (int l = 0; l < 3; l++) {
                if ((l != i) && (l != j) && (l != k)) {
                  Assert.assertArrayEquals(new int[] { i, j, k, l },
                      WModel_Permutation.fromString(((((i + "," + j) //$NON-NLS-1$
                          + ',') + k) + ',') + l));
                }
              }
            }
          }
        }
      }
    }
  }

  /** test the creation of canonical permutations */
  @SuppressWarnings("static-method")
  @Test(timeout = 3600000)
  public void canonical() {
    for (int i = 1; i < 100; i++) {
      final int[] canonical = WModel_Permutation.canonical(i);
      Assert.assertEquals(i, canonical.length);
      for (int j = i; (--j) >= 0;) {
        Assert.assertEquals(j, canonical[j]);
      }
    }
  }

  /**
   * test that shuffling does not lead to any loss or duplication
   *
   * @param n
   *          the length
   */
  private static final void __shuffle_preserves_values(final int n) {
    final int[] perm = WModel_Permutation.canonical(n);
    final int[] count = new int[n];

    for (int test = 300; (--test) >= 0;) {
      Arrays.fill(count, 0);
      WModel_Permutation.shuffle(perm, ThreadLocalRandom.current());
      for (final int i : perm) {
        count[i]++;
      }
      for (final int c : count) {
        Assert.assertEquals(1, c);
      }
    }
  }

  /** test that shuffling preserves values */
  @SuppressWarnings("static-method")
  @Test(timeout = 3600000)
  public void shuffle_preserves_values() {
    for (int i = 1; i < 300; i++) {
      WModel_Permutation_Test.__shuffle_preserves_values(i);
    }
  }

  /**
   * a heuristic test for approximate uniform shuffling
   *
   * @param n
   *          the length
   */
  private static final void __shuffle_approximately_uniform(final int n) {
    final int[] perm = WModel_Permutation.canonical(n);
    final int[][] count = new int[n][n];

    for (int test = 300 * n * n; (--test) >= 0;) {
      for (int i = n; (--i) >= 0;) {
        perm[i] = i;
      }
      WModel_Permutation.shuffle(perm, ThreadLocalRandom.current());
      int i = (-1);
      for (final int p : perm) {
        ++count[++i][p];
      }
    }

    for (final int[] cc : count) {
      for (final int c : cc) {
        if (c < 50) {
          // each value should occur at each position at least 1/6 of what
          // it would in a totally uniform scenario
          Assert.fail();
        }
      }
    }
  }

  /** test that shuffling preserves values */
  @SuppressWarnings("static-method")
  @Test(timeout = 3600000)
  public void shuffle_approximately_uniform() {
    final int maxN = _Internal_Base.FAST_TESTS ? 50 : 120;
    for (int i = 1; i < maxN; i++) {
      WModel_Permutation_Test.__shuffle_approximately_uniform(i);
    }
  }

  /**
   * test that shuffling does not lead to any loss or duplication
   *
   * @param c
   *          the c parameter
   * @param n
   *          the length
   */
  private static final void __permutation_preserves_values(final int n,
      final int c) {
    final int[] count = new int[n];

    for (int test = 300; (--test) >= 0;) {
      Arrays.fill(count, 0);
      final int[] perm = WModel_Permutation.permutation(n, c,
          ThreadLocalRandom.current());
      for (final int i : perm) {
        count[i]++;
      }
      for (final int cc : count) {
        Assert.assertEquals(1, cc);
      }
    }
  }

  /** test that the permutation method preserves values */
  @SuppressWarnings("static-method")
  @Test(timeout = 3600000)
  public void permutation_preserves_values() {
    final int maxN = _Internal_Base.FAST_TESTS ? 100 : 300;
    for (int n = 1; n < maxN; n++) {
      for (int c = 1; c <= n; c++) {
        WModel_Permutation_Test.__permutation_preserves_values(n, c);
      }
    }
  }

  /**
   * test that the permutation with c1 creates the canonical permutation
   */
  @SuppressWarnings("static-method")
  @Test(timeout = 3600000)
  public void permutation_c1() {
    for (int n = 1; n < 300; n++) {
      final int[] perm = WModel_Permutation.permutation(n, 1,
          ThreadLocalRandom.current());
      for (int i = n; (--i) >= 0;) {
        Assert.assertEquals(i, perm[i]);
      }
    }
  }

  /**
   * test the maximum cycle length of the permutatin method
   *
   * @param c
   *          the c parameter
   * @param n
   *          the length
   */
  private static final void __permutation_cycle_test(final int n,
      final int c) {
    for (int test = 300; (--test) >= 0;) {
      final int[] perm = WModel_Permutation.permutation(n, c,
          ThreadLocalRandom.current());
      int cc = 0;
      for (int i = perm.length; (--i) >= 0;) {
        if (perm[i] != i) {
          ++cc;
        }
      }
      Assert.assertEquals(c, cc);
    }
  }

  /**
   * test that the permutation method creates cycles of the right length
   */
  @SuppressWarnings("static-method")
  @Test(timeout = 3600000)
  public void permutation_cycle_test() {
    final int maxN = _Internal_Base.FAST_TESTS ? 100 : 400;
    for (int n = 1; n < maxN; n++) {
      for (int c = 2; c <= n; c++) {
        WModel_Permutation_Test.__permutation_cycle_test(n, c);
      }
    }
  }
}
