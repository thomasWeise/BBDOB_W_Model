package cn.edu.hfuu.iao.WModel;

import java.util.HashSet;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import cn.edu.hfuu.iao.Internal_Base;

/**
 * A base class for testing the compatibility of two implementations of the
 * W-Model. If a new implementation is added, we can add an
 * implementation-specific test set and establish its compatibility to an
 * existing, tested implementation. While the single-implementation tests
 * check for keeping all promises defined in the paper, the compatibility
 * test checks whether the computed values are also identical between two
 * models.
 *
 * @param <T1>
 *          the first type
 * @param <T2>
 *          the second type
 */
@Ignore
public abstract class WModel_Compatibility_Test<T1, T2>
    extends Internal_Base {

  /**
   * Convert a string to a data object of type 1
   *
   * @param str
   *          the string
   * @return the data object
   */
  protected abstract T1 fromString1(final String str);

  /**
   * Convert a data object of type 1 to a string
   *
   * @param data
   *          the data object
   * @return the string
   */
  protected abstract String toString1(final T1 data);

  /**
   * Convert a string to a data object of type 2
   *
   * @param str
   *          the string
   * @return the data object
   */
  protected abstract T2 fromString2(final String str);

  /**
   * Convert a data object of type 2 to a string
   *
   * @param data
   *          the data object
   * @return the string
   */
  protected abstract String toString2(final T2 data);

  /**
   * test exhaustively for n bits
   *
   * @param n
   *          the number of bits
   */
  private final void __to_and_from_exhaustive(final int n) {
    final char[] bits;

    bits = new char[n];
    final HashSet<String> set = new HashSet<>();
    for (int i = (1 << n); (--i) >= 0;) {
      for (int j = n; (--j) >= 0;) {
        bits[j] = (((i & (1 << j)) != 0) ? '1' : '0');
      }
      final String s_a = String.valueOf(bits);
      final T1 t_a1 = this.fromString1(s_a);
      final T2 t_a2 = this.fromString2(s_a);
      final String s_b1 = this.toString1(t_a1);
      Assert.assertEquals(s_a, s_b1);
      final String s_b2 = this.toString2(t_a2);
      Assert.assertEquals(s_a, s_b2);
      final T1 t_b1 = this.fromString1(s_b1);
      final T2 t_b2 = this.fromString2(s_b2);
      this.assertEqual(t_a1, t_b2);
      this.assertEqual(t_b1, t_a2);
      Assert.assertTrue(set.add(s_b1));
      Assert.assertFalse(set.add(s_b2));
    }
    Assert.assertEquals((1 << n), set.size());
  }

  /**
   * Assert that two data structures are equal
   *
   * @param a
   *          the first data structure
   * @param b
   *          the second one
   */
  protected abstract void assertEqual(final T1 a, final T2 b);

  /** test whether the to- and from-string conversions work correctly */
  @Test(timeout = 3600000)
  public void toString_fromString_exhaustive() {
    final int maxI = Internal_Base.FAST_TESTS ? 16 : 20;
    for (int i = 1; i <= maxI; i++) {
      this.__to_and_from_exhaustive(i);
    }
  }

  /**
   * perform a neutrality transformation
   *
   * @param in
   *          the input
   * @param mu
   *          the mu
   * @return the result
   */
  protected abstract T1 compute_neutrality1(final T1 in, final int mu);

  /**
   * perform a neutrality transformation
   *
   * @param in
   *          the input
   * @param mu
   *          the mu
   * @return the result
   */
  protected abstract T2 compute_neutrality2(final T2 in, final int mu);

  /**
   * Run one neutrality test case
   *
   * @param in
   *          the input string
   * @param out
   *          the output string
   * @param mu
   *          the mu
   */
  protected final void test_neutrality(final String in, final int mu,
      final String out) {
    final T1 x1 = this.fromString1(in);
    final T2 x2 = this.fromString2(in);
    this.assertEqual(x1, x2);
    final T1 actual1 = this.compute_neutrality1(x1, mu);
    final T2 actual2 = this.compute_neutrality2(x2, mu);
    this.assertEqual(actual1, actual2);
    Assert.assertEquals(out, this.toString1(actual1));
    Assert.assertEquals(out, this.toString2(actual2));
  }

  /**
   * test whether the neutrality mapping correctly manages the example from
   * the paper
   */
  @Test(timeout = 3600000)
  public void neutrality_paper_example_mu_2() {
    this.test_neutrality("010001100000111010000", //$NON-NLS-1$
        2, "1011001110");//$NON-NLS-1$
  }

  /**
   * perform an epistasis transformation
   *
   * @param in
   *          the input
   * @param nu
   *          the nu
   * @return the result
   */
  protected abstract T1 compute_epistasis1(final T1 in, final int nu);

  /**
   * perform an epistasis transformation
   *
   * @param in
   *          the input
   * @param nu
   *          the nu
   * @return the result
   */
  protected abstract T2 compute_epistasis2(final T2 in, final int nu);

  /**
   * run a single epistasis test case
   *
   * @param in
   *          the input string
   * @param out
   *          the output string
   * @param nu
   *          the nu
   */
  protected final void test_epistasis(final String in, final int nu,
      final String out) {
    final T1 x1 = this.fromString1(in);
    final T2 x2 = this.fromString2(in);
    this.assertEqual(x1, x2);
    final T1 actual1 = this.compute_epistasis1(x1, nu);
    final T2 actual2 = this.compute_epistasis2(x2, nu);
    this.assertEqual(actual1, actual2);
    Assert.assertEquals(out, this.toString1(actual1));
    Assert.assertEquals(out, this.toString2(actual2));
  }

  /**
   * test whether the epistasis transformation represents the first batch
   * of examples from the paper correctly
   */
  @Test(timeout = 3600000)
  public void epistasis_paper_example_eta_4() {
    this.test_epistasis("0000", 4, "0000"); //$NON-NLS-1$ //$NON-NLS-2$
    this.test_epistasis("0001", 4, "1101"); //$NON-NLS-1$ //$NON-NLS-2$
    this.test_epistasis("0010", 4, "1011"); //$NON-NLS-1$ //$NON-NLS-2$
    this.test_epistasis("0100", 4, "0111"); //$NON-NLS-1$ //$NON-NLS-2$
    this.test_epistasis("1000", 4, "1111"); //$NON-NLS-1$ //$NON-NLS-2$
    this.test_epistasis("1111", 4, "1110"); //$NON-NLS-1$ //$NON-NLS-2$
    this.test_epistasis("0111", 4, "0001"); //$NON-NLS-1$ //$NON-NLS-2$
    this.test_epistasis("1011", 4, "1001"); //$NON-NLS-1$ //$NON-NLS-2$
    this.test_epistasis("1101", 4, "0101"); //$NON-NLS-1$ //$NON-NLS-2$
    this.test_epistasis("1110", 4, "0011"); //$NON-NLS-1$ //$NON-NLS-2$
    this.test_epistasis("0011", 4, "0110"); //$NON-NLS-1$ //$NON-NLS-2$
    this.test_epistasis("0101", 4, "1010"); //$NON-NLS-1$ //$NON-NLS-2$
    this.test_epistasis("0110", 4, "1100"); //$NON-NLS-1$ //$NON-NLS-2$
    this.test_epistasis("1001", 4, "0010"); //$NON-NLS-1$ //$NON-NLS-2$
    this.test_epistasis("1010", 4, "0100"); //$NON-NLS-1$ //$NON-NLS-2$
    this.test_epistasis("1100", 4, "1000"); //$NON-NLS-1$ //$NON-NLS-2$
  }

  /**
   * test whether the epistasis transformation correctly manages the second
   * set of examples from the paper
   */
  @Test(timeout = 3600000)
  public void epistasis_paper_big_example_eta_4() {
    this.test_epistasis("1011001110", 4, //$NON-NLS-1$
        "1001011011");//$NON-NLS-1$
  }

  /**
   * test the epistasis mapping exhaustively for bijectivity
   *
   * @param n
   *          the number of bits
   * @param nu
   *          the nu
   */
  private final void __test_epistasis_bijectivity_exhaustively(final int n,
      final int nu) {
    final HashSet<String> set = new HashSet<>();
    Internal_Base.exhaustive_iteration(n, (bits) -> {
      final String str = String.valueOf(bits);
      final T1 x1 = this.fromString1(str);
      final T2 x2 = this.fromString2(str);
      this.assertEqual(x1, x2);
      final T1 r1 = this.compute_epistasis1(x1, nu);
      final T2 r2 = this.compute_epistasis2(x2, nu);
      this.assertEqual(r1, r2);
      final String s1 = this.toString1(r1);
      final String s2 = this.toString2(r2);
      Assert.assertEquals(s1, s2);
      Assert.assertTrue(set.add(s1));
      Assert.assertFalse(set.add(s1));
    });
    Assert.assertEquals((1 << n), set.size());
  }

  /** test whether the epistasis transformation is OK */
  @Test(timeout = 3600000)
  public void epistasis_bijectivity_exhaustive() {
    final int maxN = Internal_Base.FAST_TESTS ? 14 : 16;
    for (int n = 1; n < maxN; n++) {
      for (int nu = 1; nu < n; ++nu) {
        this.__test_epistasis_bijectivity_exhaustively(n, nu);
      }
    }
  }

  /**
   * compute a plain objective value
   *
   * @param in
   *          the input
   * @param n
   *          the expected string length
   * @return the objective value
   */
  protected abstract int compute_f1(final T1 in, final int n);

  /**
   * compute a plain objective value
   *
   * @param in
   *          the input
   * @param n
   *          the expected string length
   * @return the objective value
   */
  protected abstract int compute_f2(final T2 in, final int n);

  /**
   * Run one test case of the objective function
   *
   * @param in
   *          the input string
   * @param n
   *          the string length
   * @param out
   *          the expected result
   */
  protected final void test_f(final String in, final int n,
      final int out) {
    final T1 x1 = this.fromString1(in);
    final T2 x2 = this.fromString2(in);
    final int actual1 = this.compute_f1(x1, n);
    final int actual2 = this.compute_f2(x2, n);
    Assert.assertEquals(out, actual1);
    Assert.assertEquals(out, actual2);
  }

  /**
   * test the objective function returns correct results for the optimum
   * string
   */
  @Test(timeout = 3600000)
  public void f_optimum() {
    this.test_f("0", 1, 0); //$NON-NLS-1$
    this.test_f("01", 2, 0); //$NON-NLS-1$
    this.test_f("010", 3, 0); //$NON-NLS-1$
    this.test_f("0101", 4, 0); //$NON-NLS-1$
    this.test_f("01010", 5, 0); //$NON-NLS-1$
    this.test_f("0101010", 6, 0); //$NON-NLS-1$
    this.test_f("01010101", 7, 0); //$NON-NLS-1$
    this.test_f("010101010", 8, 0); //$NON-NLS-1$
    this.test_f("0101010101", 9, 0); //$NON-NLS-1$
    this.test_f("01010101010", 10, 0); //$NON-NLS-1$
    this.test_f("010101010101", 11, 0); //$NON-NLS-1$
    this.test_f("0101010101010", 12, 0); //$NON-NLS-1$
  }

  /**
   * test the objective function returns correct results for the worst
   * possible string
   */
  @Test(timeout = 3600000)
  public void f_worst_possible() {
    this.test_f("1", 1, 1); //$NON-NLS-1$
    this.test_f("10", 2, 2); //$NON-NLS-1$
    this.test_f("101", 3, 3); //$NON-NLS-1$
    this.test_f("1010", 4, 4); //$NON-NLS-1$
    this.test_f("10101", 5, 5); //$NON-NLS-1$
    this.test_f("1010101", 6, 6); //$NON-NLS-1$
    this.test_f("10101010", 7, 7); //$NON-NLS-1$
    this.test_f("101010101", 8, 8); //$NON-NLS-1$
    this.test_f("1010101010", 9, 9); //$NON-NLS-1$
    this.test_f("10101010101", 10, 10); //$NON-NLS-1$
    this.test_f("101010101010", 11, 11); //$NON-NLS-1$
    this.test_f("1010101010101", 12, 12); //$NON-NLS-1$
  }

  /**
   * test the objective function returns correct results for solutions
   * which are too long
   */
  @Test(timeout = 3600000)
  public void f_too_long() {
    this.test_f("0", 1, 0); //$NON-NLS-1$
    this.test_f("1", 1, 1); //$NON-NLS-1$
    this.test_f("00", 1, 0); //$NON-NLS-1$
    this.test_f("01", 1, 0); //$NON-NLS-1$
    this.test_f("10", 1, 1); //$NON-NLS-1$
    this.test_f("11", 1, 1); //$NON-NLS-1$
    this.test_f("000", 1, 0); //$NON-NLS-1$
    this.test_f("010", 1, 0); //$NON-NLS-1$
    this.test_f("100", 1, 1); //$NON-NLS-1$
    this.test_f("110", 1, 1); //$NON-NLS-1$
    this.test_f("001", 1, 0); //$NON-NLS-1$
    this.test_f("011", 1, 0); //$NON-NLS-1$
    this.test_f("101", 1, 1); //$NON-NLS-1$
    this.test_f("111", 1, 1); //$NON-NLS-1$
    this.test_f("0000", 1, 0); //$NON-NLS-1$
    this.test_f("0100", 1, 0); //$NON-NLS-1$
    this.test_f("1000", 1, 1); //$NON-NLS-1$
    this.test_f("1100", 1, 1); //$NON-NLS-1$
    this.test_f("0010", 1, 0); //$NON-NLS-1$
    this.test_f("0110", 1, 0); //$NON-NLS-1$
    this.test_f("1010", 1, 1); //$NON-NLS-1$
    this.test_f("1110", 1, 1); //$NON-NLS-1$
    this.test_f("0001", 1, 0); //$NON-NLS-1$
    this.test_f("0101", 1, 0); //$NON-NLS-1$
    this.test_f("1001", 1, 1); //$NON-NLS-1$
    this.test_f("1101", 1, 1); //$NON-NLS-1$
    this.test_f("0011", 1, 0); //$NON-NLS-1$
    this.test_f("0111", 1, 0); //$NON-NLS-1$
    this.test_f("1011", 1, 1); //$NON-NLS-1$
    this.test_f("1111", 1, 1); //$NON-NLS-1$

    this.test_f("00", 2, 1); //$NON-NLS-1$
    this.test_f("01", 2, 0); //$NON-NLS-1$
    this.test_f("10", 2, 2); //$NON-NLS-1$
    this.test_f("11", 2, 1); //$NON-NLS-1$
    this.test_f("000", 2, 1); //$NON-NLS-1$
    this.test_f("010", 2, 0); //$NON-NLS-1$
    this.test_f("100", 2, 2); //$NON-NLS-1$
    this.test_f("110", 2, 1); //$NON-NLS-1$
    this.test_f("001", 2, 1); //$NON-NLS-1$
    this.test_f("011", 2, 0); //$NON-NLS-1$
    this.test_f("101", 2, 2); //$NON-NLS-1$
    this.test_f("111", 2, 1); //$NON-NLS-1$
    this.test_f("0000", 2, 1); //$NON-NLS-1$
    this.test_f("0100", 2, 0); //$NON-NLS-1$
    this.test_f("1000", 2, 2); //$NON-NLS-1$
    this.test_f("1100", 2, 1); //$NON-NLS-1$
    this.test_f("0010", 2, 1); //$NON-NLS-1$
    this.test_f("0110", 2, 0); //$NON-NLS-1$
    this.test_f("1010", 2, 2); //$NON-NLS-1$
    this.test_f("1110", 2, 1); //$NON-NLS-1$
    this.test_f("0001", 2, 1); //$NON-NLS-1$
    this.test_f("0101", 2, 0); //$NON-NLS-1$
    this.test_f("1001", 2, 2); //$NON-NLS-1$
    this.test_f("1101", 2, 1); //$NON-NLS-1$
    this.test_f("0011", 2, 1); //$NON-NLS-1$
    this.test_f("0111", 2, 0); //$NON-NLS-1$
    this.test_f("1011", 2, 2); //$NON-NLS-1$
    this.test_f("1111", 2, 1); //$NON-NLS-1$

    this.test_f("000", 3, 1); //$NON-NLS-1$
    this.test_f("010", 3, 0); //$NON-NLS-1$
    this.test_f("100", 3, 2); //$NON-NLS-1$
    this.test_f("110", 3, 1); //$NON-NLS-1$
    this.test_f("001", 3, 2); //$NON-NLS-1$
    this.test_f("011", 3, 1); //$NON-NLS-1$
    this.test_f("101", 3, 3); //$NON-NLS-1$
    this.test_f("111", 3, 2); //$NON-NLS-1$
    this.test_f("0000", 3, 1); //$NON-NLS-1$
    this.test_f("0100", 3, 0); //$NON-NLS-1$
    this.test_f("1000", 3, 2); //$NON-NLS-1$
    this.test_f("1100", 3, 1); //$NON-NLS-1$
    this.test_f("0010", 3, 2); //$NON-NLS-1$
    this.test_f("0110", 3, 1); //$NON-NLS-1$
    this.test_f("1010", 3, 3); //$NON-NLS-1$
    this.test_f("1110", 3, 2); //$NON-NLS-1$
    this.test_f("0001", 3, 1); //$NON-NLS-1$
    this.test_f("0101", 3, 0); //$NON-NLS-1$
    this.test_f("1001", 3, 2); //$NON-NLS-1$
    this.test_f("1101", 3, 1); //$NON-NLS-1$
    this.test_f("0011", 3, 2); //$NON-NLS-1$
    this.test_f("0111", 3, 1); //$NON-NLS-1$
    this.test_f("1011", 3, 3); //$NON-NLS-1$
    this.test_f("1111", 3, 2); //$NON-NLS-1$
  }

  /**
   * test the objective function returns correct results for solutions
   * which are too short
   */
  @Test(timeout = 3600000)
  public void f_too_short() {
    this.test_f("0", 1, 0); //$NON-NLS-1$
    this.test_f("1", 1, 1); //$NON-NLS-1$
    this.test_f("0", 2, 1); //$NON-NLS-1$
    this.test_f("1", 2, 2); //$NON-NLS-1$
    this.test_f("0", 3, 2); //$NON-NLS-1$
    this.test_f("1", 3, 3); //$NON-NLS-1$
    this.test_f("0", 4, 3); //$NON-NLS-1$
    this.test_f("1", 4, 4); //$NON-NLS-1$

    this.test_f("00", 1, 0); //$NON-NLS-1$
    this.test_f("10", 1, 1); //$NON-NLS-1$
    this.test_f("01", 1, 0); //$NON-NLS-1$
    this.test_f("11", 1, 1); //$NON-NLS-1$
    this.test_f("00", 2, 1); //$NON-NLS-1$
    this.test_f("10", 2, 2); //$NON-NLS-1$
    this.test_f("01", 2, 0); //$NON-NLS-1$
    this.test_f("11", 2, 1); //$NON-NLS-1$
    this.test_f("00", 3, 2); //$NON-NLS-1$
    this.test_f("10", 3, 3); //$NON-NLS-1$
    this.test_f("01", 3, 1); //$NON-NLS-1$
    this.test_f("11", 3, 2); //$NON-NLS-1$
    this.test_f("00", 4, 3); //$NON-NLS-1$
    this.test_f("10", 4, 4); //$NON-NLS-1$
    this.test_f("01", 4, 2); //$NON-NLS-1$
    this.test_f("11", 4, 3); //$NON-NLS-1$
    this.test_f("00", 5, 4); //$NON-NLS-1$
    this.test_f("10", 5, 5); //$NON-NLS-1$
    this.test_f("01", 5, 3); //$NON-NLS-1$
    this.test_f("11", 5, 4); //$NON-NLS-1$

    this.test_f("000", 1, 0); //$NON-NLS-1$
    this.test_f("100", 1, 1); //$NON-NLS-1$
    this.test_f("010", 1, 0); //$NON-NLS-1$
    this.test_f("110", 1, 1); //$NON-NLS-1$
    this.test_f("001", 1, 0); //$NON-NLS-1$
    this.test_f("101", 1, 1); //$NON-NLS-1$
    this.test_f("011", 1, 0); //$NON-NLS-1$
    this.test_f("111", 1, 1); //$NON-NLS-1$

    this.test_f("000", 2, 1); //$NON-NLS-1$
    this.test_f("100", 2, 2); //$NON-NLS-1$
    this.test_f("010", 2, 0); //$NON-NLS-1$
    this.test_f("110", 2, 1); //$NON-NLS-1$
    this.test_f("001", 2, 1); //$NON-NLS-1$
    this.test_f("101", 2, 2); //$NON-NLS-1$
    this.test_f("011", 2, 0); //$NON-NLS-1$
    this.test_f("111", 2, 1); //$NON-NLS-1$

    this.test_f("000", 3, 1); //$NON-NLS-1$
    this.test_f("100", 3, 2); //$NON-NLS-1$
    this.test_f("010", 3, 0); //$NON-NLS-1$
    this.test_f("110", 3, 1); //$NON-NLS-1$
    this.test_f("001", 3, 2); //$NON-NLS-1$
    this.test_f("101", 3, 3); //$NON-NLS-1$
    this.test_f("011", 3, 1); //$NON-NLS-1$
    this.test_f("111", 3, 2); //$NON-NLS-1$

    this.test_f("000", 4, 2); //$NON-NLS-1$
    this.test_f("100", 4, 3); //$NON-NLS-1$
    this.test_f("010", 4, 1); //$NON-NLS-1$
    this.test_f("110", 4, 2); //$NON-NLS-1$
    this.test_f("001", 4, 3); //$NON-NLS-1$
    this.test_f("101", 4, 4); //$NON-NLS-1$
    this.test_f("011", 4, 2); //$NON-NLS-1$
    this.test_f("111", 4, 3); //$NON-NLS-1$
    this.test_f("000", 5, 3); //$NON-NLS-1$
    this.test_f("100", 5, 4); //$NON-NLS-1$
    this.test_f("010", 5, 2); //$NON-NLS-1$
    this.test_f("110", 5, 3); //$NON-NLS-1$
    this.test_f("001", 5, 4); //$NON-NLS-1$
    this.test_f("101", 5, 5); //$NON-NLS-1$
    this.test_f("011", 5, 3); //$NON-NLS-1$
    this.test_f("111", 5, 4); //$NON-NLS-1$
  }

  /**
   * test exhaustively for n bits
   *
   * @param n
   *          the number of bits
   */
  private final void __f_exhaustive(final int n) {
    final char[] bits;

    bits = new char[n];
    for (int i = (1 << n); (--i) >= 0;) {
      int value = 0;
      for (int j = n; (--j) >= 0;) {
        final boolean bit = Internal_Base.bit(i, j);
        if (Internal_Base.optimum(j) != bit) {
          ++value;
        }
        bits[j] = Internal_Base.bit(bit);
      }
      this.test_f(String.valueOf(bits), n, value);
    }
  }

  /**
   * test the objective function exhaustively for up to 25 bits
   */
  @Test(timeout = 3600000)
  public void f_exhaustive() {
    for (int i = 1; i <= 25; ++i) {
      this.__f_exhaustive(i);
    }
  }

  /**
   * compute a multi-objective split
   *
   * @param in
   *          the input
   * @param m
   *          the number of objectives
   * @param n
   *          the expected string length
   * @return the array with the multiple objective splits
   */
  protected abstract T1[] compute_multi_objectives1(final T1 in,
      final int m, final int n);

  /**
   * compute a multi-objective split
   *
   * @param in
   *          the input
   * @param m
   *          the number of objectives
   * @param n
   *          the expected string length
   * @return the array with the multiple objective splits
   */
  protected abstract T2[] compute_multi_objectives2(final T2 in,
      final int m, final int n);

  /**
   * Run one test case of the multiple objectives
   *
   * @param in
   *          the input string
   * @param n
   *          the string length
   * @param out
   *          the expected results
   */
  protected final void test_multiple_objectives(final String in,
      final int n, final String... out) {
    final T1 x1 = this.fromString1(in);
    final T2 x2 = this.fromString2(in);
    final T1[] actual1 = this.compute_multi_objectives1(x1, out.length, n);
    final T2[] actual2 = this.compute_multi_objectives2(x2, out.length, n);
    Assert.assertEquals(out.length, actual1.length);
    Assert.assertEquals(out.length, actual2.length);
    for (int i = 0; i < out.length; i++) {
      Assert.assertEquals(out[i], this.toString1(actual1[i]));
      Assert.assertEquals(out[i], this.toString2(actual2[i]));
    }
  }

  /** check the paper example for {@code m=2} */
  @Test(timeout = 3600000)
  public void multiple_objectives_paper_example_m_2() {
    this.test_multiple_objectives("1001011011", 6, //$NON-NLS-1$
        "100110", //$NON-NLS-1$
        "011010");//$NON-NLS-1$
  }

  /**
   * compute a objective value based on training cases
   *
   * @param in
   *          the input
   * @param training
   *          the training cases
   * @return the objective value
   */
  protected abstract int compute_f_training_cases1(final T1 in,
      final long[] training);

  /**
   * compute a objective value based on training cases
   *
   * @param in
   *          the input
   * @param training
   *          the training cases
   * @return the objective value
   */
  protected abstract int compute_f_training_cases2(final T2 in,
      final long[] training);

  /**
   * Run one test case of the training cases
   *
   * @param in
   *          the input string
   * @param training
   *          the training cases
   * @param out
   *          the expected results
   */
  protected final void test_training_cases(final String in,
      final long[] training, final int out) {
    final T1 x1 = this.fromString1(in);
    final T2 x2 = this.fromString2(in);
    Assert.assertEquals(out, this.compute_f_training_cases1(x1, training));
    Assert.assertEquals(out, this.compute_f_training_cases2(x2, training));
  }

  /** check the paper example for training cases */
  @Test(timeout = 3600000)
  public void training_cases_paper_example_old() {
    final long[] cases = WModel_TrainingCases.fromString(//
        "*10001", //$NON-NLS-1$
        "0101*0", //$NON-NLS-1$
        "0*0111", //$NON-NLS-1$
        "011*01", //$NON-NLS-1$
        "11*101"//$NON-NLS-1$
    );

    this.test_training_cases("100110", cases, 16);//$NON-NLS-1$
    this.test_training_cases("011010", cases, 14);//$NON-NLS-1$
  }

  /** check the paper example for training cases */
  @Test(timeout = 3600000)
  public void training_cases_paper_example_new() {
    final long[] cases = WModel_TrainingCases.fromString(//
        "*10001", //$NON-NLS-1$
        "0101*0", //$NON-NLS-1$
        "1*0111", //$NON-NLS-1$
        "111*01", //$NON-NLS-1$
        "11*101"//$NON-NLS-1$
    );

    this.test_training_cases("100110", cases, 14);//$NON-NLS-1$
    this.test_training_cases("011010", cases, 16);//$NON-NLS-1$
  }

  /**
   * compute a permutation
   *
   * @param in
   *          the input solution
   * @param permutation
   *          the permutation
   * @return the permutated result
   */
  protected abstract T1 compute_permutate1(final T1 in,
      final int[] permutation);

  /**
   * compute a permutation
   *
   * @param in
   *          the input solution
   * @param permutation
   *          the permutation
   * @return the permutated result
   */
  protected abstract T2 compute_permutate2(final T2 in,
      final int[] permutation);

  /** check compatibility on many random permutations */
  @Test(timeout = 3600000)
  public void random_permutations() {
    final ThreadLocalRandom random = ThreadLocalRandom.current();
    for (int samples = Internal_Base.FAST_TESTS ? 300_000
        : 2_000_000; (--samples) >= 0;) {
      final int n = random.nextInt(1, 200);
      final int c = random.nextInt(1, n + 1);
      final int[] perm = WModel_Permutation.permutation(n, c, random);
      final String x = Internal_Base.random(n, random);
      final T1 x1 = this.fromString1(x);
      final T2 x2 = this.fromString2(x);
      this.assertEqual(x1, x2);
      final T1 y1 = this.compute_permutate1(x1, perm);
      final T2 y2 = this.compute_permutate2(x2, perm);
      this.assertEqual(y1, y2);
      Assert.assertEquals(this.toString1(y1), this.toString2(y2));
    }
  }
}
