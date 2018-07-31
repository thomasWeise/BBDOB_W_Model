package cn.edu.hfuu.iao.WModel;

import org.junit.Assert;
import org.junit.Test;

import cn.edu.hfuu.iao.TestBase;

/** Testing the ruggedness layer. */
public class WModel_Ruggedness_Test extends TestBase {
  /** test the paper example 1 */
  @SuppressWarnings("static-method")
  @Test(timeout = 3600000)
  public void ruggedness_transform_paper_example_gammap_34_q_25_old() {
    Assert.assertEquals(57,
        WModel_Ruggedness.ruggedness_translate(34, 25));
  }

  /** test the paper example 1 */
  @SuppressWarnings("static-method")
  @Test(timeout = 3600000)
  public void ruggedness_transform_paper_example_gammap_12_q_6_new() {
    Assert.assertEquals(9, WModel_Ruggedness.ruggedness_translate(12, 6));
  }

  /** test the paper example 1 */
  @SuppressWarnings("static-method")
  @Test(timeout = 3600000)
  public void ruggedness_transform_paper_example_gamma_12_q_6_new() {
    final int[] r = WModel_Ruggedness.ruggedness(12, 6);
    Assert.assertEquals(3, r[3]);
    Assert.assertEquals(5, r[6]);
  }

  /** test the paper example 2 */
  @SuppressWarnings("static-method")
  @Test(timeout = 3600000)
  public void ruggedness_transform_paper_example_gammap_q_5() {
    Assert.assertEquals(0, WModel_Ruggedness.ruggedness_translate(0, 5));
    Assert.assertEquals(1, WModel_Ruggedness.ruggedness_translate(1, 5));
    Assert.assertEquals(2, WModel_Ruggedness.ruggedness_translate(2, 5));
    Assert.assertEquals(3, WModel_Ruggedness.ruggedness_translate(3, 5));
    Assert.assertEquals(4, WModel_Ruggedness.ruggedness_translate(4, 5));
    Assert.assertEquals(8, WModel_Ruggedness.ruggedness_translate(5, 5));
    Assert.assertEquals(9, WModel_Ruggedness.ruggedness_translate(6, 5));
    Assert.assertEquals(10, WModel_Ruggedness.ruggedness_translate(7, 5));
    Assert.assertEquals(7, WModel_Ruggedness.ruggedness_translate(8, 5));
    Assert.assertEquals(6, WModel_Ruggedness.ruggedness_translate(9, 5));
    Assert.assertEquals(5, WModel_Ruggedness.ruggedness_translate(10, 5));
  }

  /**
   * test the raw ruggedness
   *
   * @param gammaPrime
   *          the gamma prime
   * @param q
   *          the q
   * @param expected
   *          the expected result
   */
  private static final void __testRuggednessRaw(final int gammaPrime,
      final int q, final int... expected) {
    Assert.assertArrayEquals(expected,
        WModel_Ruggedness.ruggedness_raw(gammaPrime, q));
  }

  /**
   * test the raw ruggedness
   *
   * @param gammaPrime
   *          the gamma prime
   * @param q
   *          the q
   * @param expected
   *          the expected result
   */
  private static final void __testRuggedness(final int gammaPrime,
      final int q, final int... expected) {
    Assert.assertArrayEquals(expected,
        WModel_Ruggedness.ruggedness(gammaPrime, q));
  }

  /** test the paper example 2 */
  @SuppressWarnings("static-method")
  @Test(timeout = 3600000)
  public void ruggedness_raw_paper_example_gammap_q_5() {
    WModel_Ruggedness_Test.__testRuggednessRaw(0, 5, 0, 1, 2, 3, 4, 5);
    WModel_Ruggedness_Test.__testRuggednessRaw(1, 5, 0, 1, 2, 3, 5, 4);
    WModel_Ruggedness_Test.__testRuggednessRaw(2, 5, 0, 1, 2, 4, 5, 3);
    WModel_Ruggedness_Test.__testRuggednessRaw(3, 5, 0, 1, 3, 4, 5, 2);
    WModel_Ruggedness_Test.__testRuggednessRaw(4, 5, 0, 2, 3, 4, 5, 1);
    WModel_Ruggedness_Test.__testRuggednessRaw(5, 5, 0, 5, 4, 3, 1, 2);
    WModel_Ruggedness_Test.__testRuggednessRaw(6, 5, 0, 5, 4, 2, 1, 3);
    WModel_Ruggedness_Test.__testRuggednessRaw(7, 5, 0, 5, 3, 2, 1, 4);
    WModel_Ruggedness_Test.__testRuggednessRaw(8, 5, 0, 5, 1, 2, 4, 3);
    WModel_Ruggedness_Test.__testRuggednessRaw(9, 5, 0, 5, 1, 3, 4, 2);
    WModel_Ruggedness_Test.__testRuggednessRaw(10, 5, 0, 5, 1, 4, 2, 3);
  }

  /** test the paper example 2 */
  @SuppressWarnings("static-method")
  @Test(timeout = 3600000)
  public void ruggedness_paper_example_gammap_q_5() {
    WModel_Ruggedness_Test.__testRuggedness(0, 5, 0, 1, 2, 3, 4, 5);
    WModel_Ruggedness_Test.__testRuggedness(1, 5, 0, 1, 2, 3, 5, 4);
    WModel_Ruggedness_Test.__testRuggedness(2, 5, 0, 1, 2, 4, 5, 3);
    WModel_Ruggedness_Test.__testRuggedness(3, 5, 0, 1, 3, 4, 5, 2);
    WModel_Ruggedness_Test.__testRuggedness(4, 5, 0, 2, 3, 4, 5, 1);
    WModel_Ruggedness_Test.__testRuggedness(5, 5, 0, 5, 1, 2, 4, 3);
    WModel_Ruggedness_Test.__testRuggedness(6, 5, 0, 5, 1, 3, 4, 2);
    WModel_Ruggedness_Test.__testRuggedness(7, 5, 0, 5, 1, 4, 2, 3);
    WModel_Ruggedness_Test.__testRuggedness(8, 5, 0, 5, 3, 2, 1, 4);
    WModel_Ruggedness_Test.__testRuggedness(9, 5, 0, 5, 4, 2, 1, 3);
    WModel_Ruggedness_Test.__testRuggedness(10, 5, 0, 5, 4, 3, 1, 2);
  }

  /** test the paper example 2 */
  @SuppressWarnings("static-method")
  @Test(timeout = 3600000)
  public void ruggedness_max_gamma() {
    for (int q = 1000; (--q) >= 1;) {
      Assert.assertEquals(((q * (q - 1)) >>> 1),
          WModel_Ruggedness.max_gamma(q));
    }
  }

  /**
   * test the ruggedness predicate
   *
   * @param q
   *          the q
   */
  private static final void __test_ruggedness_raw_delta(final int q) {
    for (int gamma = WModel_Ruggedness.max_gamma(q); gamma >= 0; gamma--) {
      final int[] r = WModel_Ruggedness.ruggedness_raw(gamma, q);
      Assert.assertEquals(q + 1, r.length);
      int Delta = 0;
      for (int i = r.length - 1; (--i) >= 0;) {
        Delta += Math.abs(r[i] - r[i + 1]);
      }
      Assert.assertEquals(gamma, Delta - q);
    }
  }

  /** test whether the raw permutations meet the delta requirements */
  @SuppressWarnings("static-method")
  @Test(timeout = 3600000)
  public void ruggedness_raw_delta() {
    for (int i = 1; i < 300; i++) {
      WModel_Ruggedness_Test.__test_ruggedness_raw_delta(i);
    }
  }

  /**
   * test the ruggedness translation
   *
   * @param q
   *          the q
   */
  private static final void __test_ruggedness_translate(final int q) {
    final int[] perm = new int[WModel_Ruggedness.max_gamma(q) + 1];
    for (int gamma = perm.length; (--gamma) >= 0;) {
      final int translated = WModel_Ruggedness.ruggedness_translate(gamma,
          q);
      perm[gamma] = translated;
      Assert.assertTrue(translated >= 0);
      Assert.assertTrue(translated < perm.length);
    }
    Assert.assertEquals(perm[0], 0);
    for (int i = perm.length; (--i) > 0;) {
      for (int j = i; (--j) >= 0;) {
        Assert.assertNotEquals(perm[i], perm[j]);
      }
    }
  }

  /**
   * test whether the ruggedness translation is a bijective permutation
   * itself
   */
  @SuppressWarnings("static-method")
  @Test(timeout = 3600000)
  public void ruggedness_ruggedness_translate() {
    final int max = (TestBase.FAST_TESTS ? 150 : 400);
    for (int i = 1; i < max; i++) {
      WModel_Ruggedness_Test.__test_ruggedness_translate(i);
    }
  }
}
