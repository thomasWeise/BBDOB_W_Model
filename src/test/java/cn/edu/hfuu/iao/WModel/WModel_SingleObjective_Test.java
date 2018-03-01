package cn.edu.hfuu.iao.WModel;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 * A base class for testing a single single-objective implementation of the
 * W-Model
 *
 * @param <T>
 *          the data type
 */
@Ignore
public abstract class WModel_SingleObjective_Test<T>
    extends _Internal_Base {

  /**
   * create the single-objective version of the W-Model
   *
   * @param _n
   *          the reduced size of a candidate solution
   * @param _mu
   *          the neutrality factor
   * @param _nu
   *          the epistasis nu
   * @param _gamma
   *          the ruggedness gamma
   * @return the instance
   */
  protected abstract WModel_SingleObjective<T> create(final int _n,
      final int _mu, final int _nu, final int _gamma);

  /**
   * Convert a string to a data object
   *
   * @param str
   *          the string
   * @return the data object
   */
  protected abstract T fromString(final String str);

  /**
   * test the number of optima via exhaustive enumeration
   *
   * @param _n
   *          the reduced size of a candidate solution
   * @param _mu
   *          the neutrality factor
   * @param _nu
   *          the epistasis nu
   * @param _gamma
   *          the ruggedness gamma
   */
  private final void __test_exhaustive_enumeration(final int _n,
      final int _mu, final int _nu, final int _gamma) {
    final WModel_SingleObjective<T> f;

    f = this.create(_n, _mu, _nu, _gamma);

    final long[] counter = new long[_n + 1];
    _Internal_Base._exhaustive_iteration(_n * _mu, (text) -> {
      final int res = f.applyAsInt(this.fromString(String.valueOf(text)));
      counter[res] = Math.max(counter[res], counter[res] + 1L);
    });

    for (final long value : counter) {
      Assert.assertTrue(value > 0L);
    }

    if (_mu == 1) {
      Assert.assertEquals(1L, counter[0]);
    } else {

      long times = -1L;
      if (_nu <= 2) {
        final int ones = (_n >>> 1);
        final int zeros = (_n - ones);

        times = WModel_SingleObjective_Test.__number(zeros, ones, _mu);
      } else {
        times = (-1L);
      }

      if (times <= 0L) {
        Assert.assertTrue(counter[0] >= 1);
      } else {
        if (times >= Long.MAX_VALUE) {
          Assert.assertTrue(counter[0] >= Long.MAX_VALUE);
        } else {
          Assert.assertEquals(times, counter[0]);
        }
      }
    }
  }

  /**
   * compute the number of times a string with the given number of zeros
   * and ones can appear if the specified redundancy is applied
   *
   * @param zeros
   *          the zeros
   * @param ones
   *          the ones
   * @param mu
   *          the mu
   * @return the number of times, or Long.MAX_VALUE if too big
   */
  private static final long __number(final int zeros, final int ones,
      final int mu) {
    final int majority = 1 + (mu >>> 1);

    long valuesPerZero = 0;
    for (int i = majority; i <= mu; i++) {
      valuesPerZero += WModel_SingleObjective_Test.__n_over_k(mu, i);
      if (valuesPerZero < 1L) {
        return Long.MAX_VALUE;
      }
    }

    final long valuesPerOne = (1L << mu) - valuesPerZero;
    if (valuesPerOne < 1L) {
      return Long.MAX_VALUE;
    }
    long total = 1L;
    for (int i = zeros; (--i) >= 0;) {
      total *= valuesPerZero;
      if (total <= 0L) {
        return Long.MAX_VALUE;
      }
    }
    for (int i = ones; (--i) >= 0;) {
      total *= valuesPerOne;
      if (total <= 0L) {
        return Long.MAX_VALUE;
      }
    }

    return total;
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n10_mu1_nu2_gamma0() {
    final int n = 10;
    final int mu = 1;
    final int nu = 2;
    final int gamma = 0;
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n17_mu1_nu2_gamma0() {
    final int n = 17;
    final int mu = 1;
    final int nu = 2;
    final int gamma = 0;
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n20_mu1_nu2_gamma0() {
    final int n = 20;
    final int mu = 1;
    final int nu = 2;
    final int gamma = 0;
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n12_mu2_nu2_gamma0() {
    if (_Internal_Base.FAST_TESTS) {
      return;
    }
    final int n = 12;
    final int mu = 2;
    final int nu = 2;
    final int gamma = 0;
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n7_mu2_nu2_gamma0() {
    final int n = 7;
    final int mu = 2;
    final int nu = 2;
    final int gamma = 0;
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n6_mu2_nu2_gamma0() {
    final int n = 6;
    final int mu = 2;
    final int nu = 2;
    final int gamma = 0;
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n10_mu2_nu2_gamma0() {
    final int n = 10;
    final int mu = 2;
    final int nu = 2;
    final int gamma = 0;
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n5_mu2_nu2_gamma0() {
    final int n = 5;
    final int mu = 2;
    final int nu = 2;
    final int gamma = 0;
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n2_mu2_nu2_gamma0() {
    final int n = 2;
    final int mu = 2;
    final int nu = 2;
    final int gamma = 0;
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n3_mu2_nu2_gamma0() {
    final int n = 3;
    final int mu = 2;
    final int nu = 2;
    final int gamma = 0;
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n4_mu2_nu2_gamma0() {
    final int n = 4;
    final int mu = 2;
    final int nu = 2;
    final int gamma = 0;
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n6_mu2_nu2_gamma3() {
    final int n = 6;
    final int mu = 2;
    final int nu = 2;
    final int gamma = 3;
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n5_mu3_nu2_gamma0() {
    final int n = 5;
    final int mu = 3;
    final int nu = 2;
    final int gamma = 0;
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n2_mu3_nu2_gamma0() {
    final int n = 2;
    final int mu = 3;
    final int nu = 2;
    final int gamma = 0;
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n3_mu3_nu2_gamma0() {
    final int n = 3;
    final int mu = 3;
    final int nu = 2;
    final int gamma = 0;
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n4_mu3_nu2_gamma0() {
    final int n = 4;
    final int mu = 3;
    final int nu = 2;
    final int gamma = 0;
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n6_mu3_nu2_gamma0() {
    final int n = 6;
    final int mu = 3;
    final int nu = 2;
    final int gamma = 0;
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n20_mu1_nu2_gamma10() {
    final int n = 20;
    final int mu = 1;
    final int nu = 2;
    final int gamma = 10;
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n17_mu1_nu2_gamma10() {
    final int n = 17;
    final int mu = 1;
    final int nu = 2;
    final int gamma = 10;
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n10_mu1_nu2_gamma10() {
    final int n = 10;
    final int mu = 1;
    final int nu = 2;
    final int gamma = 10;
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n10_mu1_nu3_gamma0() {
    final int n = 10;
    final int mu = 1;
    final int nu = 3;
    final int gamma = 0;
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n17_mu1_nu3_gamma0() {
    final int n = 17;
    final int mu = 1;
    final int nu = 3;
    final int gamma = 0;
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n20_mu1_nu3_gamma0() {
    final int n = 20;
    final int mu = 1;
    final int nu = 3;
    final int gamma = 0;
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n12_mu2_nu3_gamma0() {
    if (_Internal_Base.FAST_TESTS) {
      return;
    }
    final int n = 12;
    final int mu = 2;
    final int nu = 3;
    final int gamma = 0;
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n9_mu2_nu3_gamma0() {
    final int n = 9;
    final int mu = 2;
    final int nu = 3;
    final int gamma = 0;
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n7_mu2_nu3_gamma0() {
    final int n = 7;
    final int mu = 2;
    final int nu = 3;
    final int gamma = 0;
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n10_mu2_nu3_gamma0() {
    final int n = 10;
    final int mu = 2;
    final int nu = 3;
    final int gamma = 0;
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n5_mu2_nu3_gamma0() {
    final int n = 5;
    final int mu = 2;
    final int nu = 3;
    final int gamma = 0;
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n3_mu2_nu3_gamma0() {
    final int n = 3;
    final int mu = 2;
    final int nu = 3;
    final int gamma = 0;
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n4_mu2_nu3_gamma0() {
    final int n = 4;
    final int mu = 2;
    final int nu = 3;
    final int gamma = 0;
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n6_mu2_nu3_gamma0() {
    final int n = 6;
    final int mu = 2;
    final int nu = 3;
    final int gamma = 0;
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n5_mu3_nu3_gamma0() {
    final int n = 5;
    final int mu = 3;
    final int nu = 3;
    final int gamma = 0;
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n3_mu3_nu3_gamma0() {
    final int n = 3;
    final int mu = 3;
    final int nu = 3;
    final int gamma = 0;
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n4_mu3_nu3_gamma0() {
    final int n = 4;
    final int mu = 3;
    final int nu = 3;
    final int gamma = 0;
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n6_mu3_nu3_gamma0() {
    final int n = 6;
    final int mu = 3;
    final int nu = 3;
    final int gamma = 0;
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n20_mu1_nu3_gamma10() {
    final int n = 20;
    final int mu = 1;
    final int nu = 3;
    final int gamma = 10;
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n17_mu1_nu3_gamma10() {
    final int n = 17;
    final int mu = 1;
    final int nu = 3;
    final int gamma = 10;
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n10_mu1_nu3_gamma10() {
    final int n = 10;
    final int mu = 1;
    final int nu = 3;
    final int gamma = 10;
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n10_mu1_nu4_gamma0() {
    final int n = 10;
    final int mu = 1;
    final int nu = 4;
    final int gamma = 0;
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n17_mu1_nu4_gamma0() {
    final int n = 17;
    final int mu = 1;
    final int nu = 4;
    final int gamma = 0;
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n20_mu1_nu4_gamma0() {
    final int n = 20;
    final int mu = 1;
    final int nu = 4;
    final int gamma = 0;
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n12_mu2_nu4_gamma0() {
    if (_Internal_Base.FAST_TESTS) {
      return;
    }
    final int n = 12;
    final int mu = 2;
    final int nu = 4;
    final int gamma = 0;
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n7_mu2_nu4_gamma0() {
    final int n = 7;
    final int mu = 2;
    final int nu = 4;
    final int gamma = 0;
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n8_mu2_nu4_gamma0() {
    final int n = 8;
    final int mu = 2;
    final int nu = 4;
    final int gamma = 0;
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n10_mu2_nu4_gamma0() {
    final int n = 10;
    final int mu = 2;
    final int nu = 4;
    final int gamma = 0;
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n5_mu2_nu4_gamma0() {
    final int n = 5;
    final int mu = 2;
    final int nu = 4;
    final int gamma = 0;
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n4_mu2_nu4_gamma0() {
    final int n = 4;
    final int mu = 2;
    final int nu = 4;
    final int gamma = 0;
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n6_mu2_nu4_gamma0() {
    final int n = 6;
    final int mu = 2;
    final int nu = 4;
    final int gamma = 0;
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n5_mu3_nu4_gamma0() {
    final int n = 5;
    final int mu = 3;
    final int nu = 4;
    final int gamma = 0;
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n4_mu3_nu4_gamma0() {
    final int n = 4;
    final int mu = 3;
    final int nu = 4;
    final int gamma = 0;
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n6_mu3_nu4_gamma0() {
    final int n = 6;
    final int mu = 3;
    final int nu = 4;
    final int gamma = 0;
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n20_mu1_nu4_gamma10() {
    final int n = 20;
    final int mu = 1;
    final int nu = 4;
    final int gamma = 10;
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n17_mu1_nu4_gamma10() {
    final int n = 17;
    final int mu = 1;
    final int nu = 4;
    final int gamma = 10;
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n10_mu1_nu4_gamma10() {
    final int n = 10;
    final int mu = 1;
    final int nu = 4;
    final int gamma = 10;
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n10_mu1_nu9_gamma0() {
    final int n = 10;
    final int mu = 1;
    final int nu = 9;
    final int gamma = 0;
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n17_mu1_nu9_gamma0() {
    final int n = 17;
    final int mu = 1;
    final int nu = 9;
    final int gamma = 0;
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n20_mu1_nu9_gamma0() {
    final int n = 20;
    final int mu = 1;
    final int nu = 9;
    final int gamma = 0;
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n12_mu2_nu9_gamma9() {
    if (_Internal_Base.FAST_TESTS) {
      return;
    }
    final int n = 12;
    final int mu = 2;
    final int nu = 3;
    final int gamma = 9;
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n6_mu2_nu9_gamma9() {
    final int n = 6;
    final int mu = 2;
    final int nu = 3;
    final int gamma = 9;
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n7_mu2_nu9_gamma9() {
    final int n = 7;
    final int mu = 2;
    final int nu = 3;
    final int gamma = 9;
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n10_mu2_nu9_gamma0() {
    final int n = 10;
    final int mu = 2;
    final int nu = 9;
    final int gamma = 0;
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n20_mu1_nu9_gamma10() {
    final int n = 20;
    final int mu = 1;
    final int nu = 9;
    final int gamma = 10;
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n17_mu1_nu9_gamma10() {
    final int n = 17;
    final int mu = 1;
    final int nu = 9;
    final int gamma = 10;
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n10_mu1_nu9_gamma10() {
    final int n = 10;
    final int mu = 1;
    final int nu = 9;
    final int gamma = 10;
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n10_mu1_nu9_gamma_max() {
    final int n = 10;
    final int mu = 1;
    final int nu = 9;
    final int gamma = WModel_Ruggedness.max_gamma(n);
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n17_mu1_nu9_gamma_max() {
    final int n = 17;
    final int mu = 1;
    final int nu = 9;
    final int gamma = WModel_Ruggedness.max_gamma(n);
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n20_mu1_nu9_gamma_max() {
    final int n = 20;
    final int mu = 1;
    final int nu = 9;
    final int gamma = WModel_Ruggedness.max_gamma(n);
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n12_mu2_nu9_gamma_max() {
    if (_Internal_Base.FAST_TESTS) {
      return;
    }
    final int n = 12;
    final int mu = 2;
    final int nu = 9;
    final int gamma = WModel_Ruggedness.max_gamma(n);
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n7_mu2_nu9_gamma_max() {
    final int n = 7;
    final int mu = 2;
    final int nu = 9;
    final int gamma = WModel_Ruggedness.max_gamma(n);
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n10_mu2_nu9_gamma_max() {
    final int n = 10;
    final int mu = 2;
    final int nu = 9;
    final int gamma = WModel_Ruggedness.max_gamma(n);
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n17_mu1_nu17_gamma0() {
    final int n = 17;
    final int mu = 1;
    final int nu = n;
    final int gamma = 0;
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n10_mu1_nu10_gamma0() {
    final int n = 10;
    final int mu = 1;
    final int nu = n;
    final int gamma = 0;
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n17_mu1_nu17_gamma10() {
    final int n = 17;
    final int mu = 1;
    final int nu = n;
    final int gamma = 10;
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n10_mu1_nu10_gamma10() {
    final int n = 10;
    final int mu = 1;
    final int nu = n;
    final int gamma = 10;
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n10_mu1_nu10_gamma_max() {
    final int n = 10;
    final int mu = 1;
    final int nu = 10;
    final int gamma = WModel_Ruggedness.max_gamma(n);
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n17_mu1_nu17_gamma_max() {
    final int n = 17;
    final int mu = 1;
    final int nu = 17;
    final int gamma = WModel_Ruggedness.max_gamma(n);
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n20_mu1_nu20_gamma_max() {
    final int n = 20;
    final int mu = 1;
    final int nu = 20;
    final int gamma = WModel_Ruggedness.max_gamma(n);
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n12_mu2_nu12_gamma_max() {
    if (_Internal_Base.FAST_TESTS) {
      return;
    }
    final int n = 12;
    final int mu = 2;
    final int nu = 12;
    final int gamma = WModel_Ruggedness.max_gamma(n);
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n5_mu2_nu12_gamma_max() {
    final int n = 5;
    final int mu = 2;
    final int nu = 12;
    final int gamma = WModel_Ruggedness.max_gamma(n);
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n7_mu2_nu12_gamma_max() {
    final int n = 7;
    final int mu = 2;
    final int nu = 12;
    final int gamma = WModel_Ruggedness.max_gamma(n);
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n5_mu3_nu12_gamma_max() {
    final int n = 5;
    final int mu = 3;
    final int nu = 12;
    final int gamma = WModel_Ruggedness.max_gamma(n);
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /** test a specific setting */
  @Test(timeout = 3600000)
  public void test_exhaustive_n10_mu2_nu10_gamma_max() {
    final int n = 10;
    final int mu = 2;
    final int nu = 10;
    final int gamma = WModel_Ruggedness.max_gamma(n);
    this.__test_exhaustive_enumeration(n, mu, nu, gamma);
  }

  /**
   * compute the binomial coefficient
   *
   * @param n
   *          the n
   * @param k
   *          the k
   * @return the coefficient
   */
  private static final long __n_over_k(final long n, final long k) {
    if ((k < 0L) || (k > n) || (n < 0L)) {
      return 0L;
    }

    if ((k <= 0L) || (k >= n)) {
      return 1L;
    }

    if ((k <= 1L) || (k >= (n - 1L))) {
      return n;
    }

    return WModel_SingleObjective_Test.__internalBinomial(n, k);
  }

  /**
   * compute the binomial coefficient
   *
   * @param n
   *          the n
   * @param k
   *          the k
   * @return the coefficient, or {@code -1l} on overflow
   */
  private static final long __internalBinomial(final long n,
      final long k) {
    long r, d, v, rn, g;
    final long kk;
    final int ni, ki;

    if (n < WModel_SingleObjective_Test.FACTORIALS.length) {
      ni = ((int) n);
      ki = ((int) k);
      return ((WModel_SingleObjective_Test.FACTORIALS[ni]
          / WModel_SingleObjective_Test.FACTORIALS[ki]) / //
          WModel_SingleObjective_Test.FACTORIALS[ni - ki]);
    }

    g = (n >>> 1);
    if (k > g) {
      kk = (n - k);
    } else {
      kk = k;
    }

    r = 1L;
    v = n;
    for (d = 1L; d <= kk; d++) {
      rn = ((r * v) / d);

      // overflow handling
      if (rn <= r) {
        g = WModel_SingleObjective_Test.__gcd(r, d);
        rn = (((r / g) * v) / (d / g));
        if (rn <= r) {
          g = WModel_SingleObjective_Test.__gcd(v, d);
          rn = ((r * (v / g)) / (d / g));
          if (rn <= r) {
            return (-1L);
          }
        }
      }
      v--;
      r = rn;
    }

    return r;
  }

  /**
   * the factorials
   */
  static final long[] FACTORIALS = new long[] { //
      1L, // 0
      1L, // 1
      2L, // 2
      6L, // 3
      24L, // 4
      120L, // 5
      720L, // 6
      5040L, // 7
      40320L, // 8
      362880L, // 9
      3628800L, // 10
      39916800L, // 11
      479001600L, // 12
      6227020800L, // 13
      87178291200L, // 14
      1307674368000L, // 15
      20922789888000L, // 16
      355687428096000L, // 17
      6402373705728000L, // 18
      121645100408832000L, // 19
      2432902008176640000L,// 20
  };

  /**
   * <p>
   * Compute the greatest common divisor of the absolute value of two
   * numbers, using the "binary gcd" method which avoids division and
   * modulo operations. See Knuth 4.5.2 algorithm B. This algorithm is due
   * to Josef Stein (1961).
   * </p>
   * <p>
   * <em>The implementation is adapted from Apache Commons Math 3.</em> The
   * reason why we don't use their library code here is that I don't want
   * to create a dependency on a whole library for a single function.
   * </p>
   * Special cases:
   * <ul>
   * <li>The invocations {@code gcd(Long.MIN_VALUE, Long.MIN_VALUE)},
   * {@code gcd(Long.MIN_VALUE, 0L)} and {@code gcd(0L, Long.MIN_VALUE)}
   * return {@code -1}, because the result would be 2^63, which is too
   * large for a long value.</li>
   * <li>The result of {@code gcd(x, x)}, {@code gcd(0L, x)} and
   * {@code gcd(x, 0L)} is the absolute value of {@code x}, except for the
   * special cases above.
   * <li>The invocation {@code gcd(0L, 0L)} is the only one which returns
   * {@code 0L}.</li>
   * </ul>
   *
   * @param p
   *          the first number
   * @param q
   *          the second number
   * @return the greatest common divisor, never negative.
   */
  private static final long __gcd(final long p, final long q) {
    long u, v, t;
    int k;

    u = p;
    v = q;
    if ((u == 0L) || (v == 0L)) {
      if ((u <= Long.MIN_VALUE) || (v <= Long.MIN_VALUE)) {
        return (-1L);
      }
      return (Math.abs(u) + Math.abs(v));
    }

    // Keep u and v negative, as negative integers range down to -2^63,
    // while positive numbers can only be as large as 2^63-1 (i.e. we can't
    // necessarily negate a negative number without overflow)

    if (u > 0L) {
      u = (-u);
    }
    if (v > 0L) {
      v = (-v);
    }

    // B1. [Find power of 2]
    k = 0;
    while (((u & 1L) == 0L) && ((v & 1L) == 0L) && (k < 63)) {
      // while u and v are both even...
      u /= 2L;
      v /= 2L;
      k++; // cast out twos.
    }
    if (k == 63) {
      return (-1L);
    }

    // B2. Initialize: u and v have been divided by 2^k and at least
    // one is odd.
    t = ((u & 1L) == 1L) ? v : (-(u / 2L));/* B3 */
    // t negative: u was odd, v may be even (t replaces v)
    // t positive: u was even, v is odd (t replaces u)
    do {
      // B4/B3: cast out twos from t.
      while ((t & 1L) == 0L) { // while t is even..
        t /= 2L; // cast out twos
      }
      // B5 [reset max(u,v)]
      if (t > 0L) {
        u = (-t);
      } else {
        v = t;
      }
      // B6/B3. at this point both u and v should be odd.
      t = ((v - u) / 2L);
      // |u| larger: t positive (replace u)
      // |v| larger: t negative (replace v)
    } while (t != 0L);

    return ((-u) * (1L << k)); // gcd is u*2^k
  }
}
