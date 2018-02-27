package cn.edu.hfuu.iao.WModel;

import java.util.HashSet;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 * A base class for testing a single implementation of the W-Model
 *
 * @param <T>
 *          the data type
 */
@Ignore
public abstract class WModel_Test<T> extends _Internal_Base {

  /**
   * Convert a string to a data object
   *
   * @param str
   *          the string
   * @return the data object
   */
  protected abstract T fromString(final String str);

  /**
   * Convert a data object to a string
   *
   * @param data
   *          the data object
   * @return the string
   */
  protected abstract String toString(final T data);

  /**
   * compare two data structures for equality
   *
   * @param a
   *          the first data structure
   * @param b
   *          the second one
   * @return {@code true} if and only if they are equal
   */
  protected abstract boolean equals(final T a, final T b);

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
      final T t_a = this.fromString(s_a);
      final String s_b = this.toString(t_a);
      Assert.assertEquals(s_a, s_b);
      final T t_b = this.fromString(s_b);
      this.assertEqual(t_a, t_b);
      Assert.assertTrue(set.add(s_b));
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
  protected abstract void assertEqual(final T a, final T b);

  /**
   * test whether the {@link #toString(Object)} and
   * {@link #fromString(String)} methods work reliable
   */
  @Test(timeout = 3600000)
  public void toString_fromString_exhaustive() {
    final int maxI = _Internal_Base.FAST_TESTS ? 20 : 22;
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
  protected abstract T compute_neutrality(final T in, final int mu);

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
    final T x = this.fromString(in);
    final T actual = this.compute_neutrality(x, mu);
    Assert.assertEquals(out, this.toString(actual));
  }

  /**
   * test whether the neutrality mapping correctly manages the example from
   * the paper
   */
  @Test(timeout = 3600000)
  public void neutrality_paper_example_old_mu_2() {
    this.test_neutrality("010001100000111010000", //$NON-NLS-1$
        2, "1011001110");//$NON-NLS-1$
  }

  /**
   * test whether the neutrality mapping correctly manages the example from
   * the paper
   */
  @Test(timeout = 3600000)
  public void neutrality_paper_example_new_mu_2() {
    this.test_neutrality("010101100000111010000", //$NON-NLS-1$
        2, "1111001110");//$NON-NLS-1$
  }

  /**
   * perform an epistasis transformation
   *
   * @param in
   *          the input
   * @param eta
   *          the eta
   * @return the result
   */
  protected abstract T compute_epistasis(final T in, final int eta);

  /**
   * run a single epistasis test case
   *
   * @param in
   *          the input string
   * @param out
   *          the output string
   * @param eta
   *          the eta
   */
  protected final void test_epistasis(final String in, final int eta,
      final String out) {
    final T x = this.fromString(in);
    final T actual = this.compute_epistasis(x, eta);
    Assert.assertEquals(out, this.toString(actual));
  }

  /**
   * test whether the epistasis transformation represents the first batch
   * of examples from the old paper correctly
   */
  @Test(timeout = 3600000)
  public void epistasis_paper_example_eta_4_old() {
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
  public void epistasis_paper_big_example_eta_4_old() {
    this.test_epistasis("1011001110", 4, //$NON-NLS-1$
        "1001011011");//$NON-NLS-1$
  }

  /**
   * test whether the epistasis transformation correctly manages the second
   * set of examples from the paper
   */
  @Test(timeout = 3600000)
  public void epistasis_paper_big_example_eta_4_new() {
    this.test_epistasis("1111001110", 4, //$NON-NLS-1$
        "1110011011");//$NON-NLS-1$
  }

  /**
   * test the epistasis mapping exhaustively for bijectivity
   *
   * @param n
   *          the number of bits
   * @param eta
   *          the eta
   */
  private final void __test_epistasis_bijectivity_exhaustively(final int n,
      final int eta) {
    final HashSet<String> set = new HashSet<>();
    _Internal_Base._exhaustive_iteration(n, (bits) -> {
      final String result = this.toString(this.compute_epistasis(//
          this.fromString(String.valueOf(bits)), eta));
      Assert.assertTrue(set.add(result));
    });
    Assert.assertEquals((1 << n), set.size());
  }

  /** test whether the epistasis transformation is OK */
  @Test(timeout = 3600000)
  public void epistasis_bijectivity_exhaustive() {
    final int maxN = _Internal_Base.FAST_TESTS ? 18 : 22;
    for (int n = 1; n < maxN; n++) {
      for (int eta = 1; eta < n; ++eta) {
        this.__test_epistasis_bijectivity_exhaustively(n, eta);
      }
    }
  }

  /**
   * test the epistasis promise exhaustively
   *
   * @param n
   *          the number of bits
   * @param eta
   *          the eta
   */
  private final void __test_epistasis_promise_exhaustively(final int n,
      final int eta) {
    final char[] other = new char[n];
    _Internal_Base._exhaustive_iteration(n, (bits) -> {
      final String vbits = this.toString(this
          .compute_epistasis(this.fromString(String.valueOf(bits)), eta));
      System.arraycopy(bits, 0, other, 0, n);

      final int remainingPromise = n % eta;
      final int maxPromiseIndex = (n - remainingPromise);

      for (int i = n; (--i) >= 0;) {
        other[i] = _Internal_Base._bit(!(_Internal_Base._bit(other[i])));
        final String vother = this.toString(this.compute_epistasis(
            this.fromString(String.valueOf(other)), eta));
        int changed = 0;

        for (int j = n; (--j) >= 0;) {
          if (vbits.charAt(j) != vother.charAt(j)) {
            changed++;
          }
        }

        final int promise = (i < maxPromiseIndex) ? (eta - 1)
            : (remainingPromise - 1);

        if (changed < promise) {
          Assert.fail("the number " + //$NON-NLS-1$
          changed + " of changed bits must be greater or equal to " //$NON-NLS-1$
              + promise + " when moving from " + //$NON-NLS-1$
          String.valueOf(bits) + " to "//$NON-NLS-1$
              + String.valueOf(other) + //
          " for eta=" + eta + //$NON-NLS-1$
          ", but got " + //$NON-NLS-1$
          vbits + " and " + vother);//$NON-NLS-1$
        }
        other[i] = bits[i];
      }
    });
  }

  /** test whether the epistasis transformation is OK */
  @Test(timeout = 3600000)
  public void epistasis_promise_exhaustive() {
    final int maxN = _Internal_Base.FAST_TESTS ? 17 : 18;
    for (int n = 1; n < maxN; n++) {
      for (int eta = 2; eta < n; ++eta) {
        this.__test_epistasis_promise_exhaustively(n, eta);
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
  protected abstract int compute_f(final T in, final int n);

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
    final T x = this.fromString(in);
    final int actual = this.compute_f(x, n);
    Assert.assertEquals(out, actual);
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
   * test the objective function returns correct results for the paper
   * examples
   */
  @Test(timeout = 3600000)
  public void f_paper_example() {
    this.test_f("110110", 6, 3); //$NON-NLS-1$
    this.test_f("101010", 6, 6); //$NON-NLS-1$
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
        final boolean bit = _Internal_Base._bit(i, j);
        if (_Internal_Base._optimum(j) != bit) {
          ++value;
        }
        bits[j] = _Internal_Base._bit(bit);
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
  protected abstract T[] compute_multi_objectives(final T in, final int m,
      final int n);

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
    final T x = this.fromString(in);
    final T[] actual = this.compute_multi_objectives(x, out.length, n);
    Assert.assertEquals(out.length, actual.length);
    for (int i = 0; i < out.length; i++) {
      Assert.assertEquals(out[i], this.toString(actual[i]));
    }
  }

  /** check the paper example for {@code m=2} */
  @Test(timeout = 3600000)
  public void multiple_objectives_paper_example_m_2_old() {
    this.test_multiple_objectives("1001011011", 6, //$NON-NLS-1$
        "100110", //$NON-NLS-1$
        "011010");//$NON-NLS-1$
  }

  /** check the paper example for {@code m=2} */
  @Test(timeout = 3600000)
  public void multiple_objectives_paper_example_m_2_new() {
    this.test_multiple_objectives("1110011011", 6, //$NON-NLS-1$
        "110110", //$NON-NLS-1$
        "101010");//$NON-NLS-1$
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
  protected abstract int compute_f_training_cases(final T in,
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
    final T x = this.fromString(in);
    Assert.assertEquals(out, this.compute_f_training_cases(x, training));
  }

  /** check the paper example for training cases */
  @Test(timeout = 3600000)
  public void training_cases_example_old() {
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
  public void training_cases_example_new() {
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

}
