package cn.edu.hfuu.iao.WModel_Experiments_SO;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Assert;
import org.junit.Test;

import cn.edu.hfuu.iao.TestBase;
import cn.edu.hfuu.iao.WModel.WModel_SingleObjective;
import cn.edu.hfuu.iao.utils.IOUtils;
import cn.edu.hfuu.iao.utils.SimpleParallelExecutor;

/**
 * A Test for Runners.
 */
public class Runner_Test extends TestBase {

  /**
   * test the runner for 1 algorithm and n<=5
   *
   * @throws IOException
   *           if i/o fails
   */
  @Test(timeout = 3600000)
  public void test_1_5() throws IOException {
    this.__run(1, 5);
  }

  /**
   * test the runner for 2 algorithms and n<=5
   *
   * @throws IOException
   *           if i/o fails
   */
  @Test(timeout = 3600000)
  public void test_2_5() throws IOException {
    if (TestBase.FAST_TESTS) {
      return;
    }
    this.__run(2, 5);
  }

  /**
   * test the runner for 3 algorithms and n<=5
   *
   * @throws IOException
   *           if i/o fails
   */
  @Test(timeout = 3600000)
  public void test_3_5() throws IOException {
    if (TestBase.FAST_TESTS) {
      return;
    }
    this.__run(3, 5);
  }

  /**
   * test the runner for 1 algorithm and n<=10
   *
   * @throws IOException
   *           if i/o fails
   */
  @Test(timeout = 3600000)
  public void test_1_10() throws IOException {
    if (TestBase.FAST_TESTS) {
      return;
    }
    this.__run(1, 10);
  }

  /**
   * test the runner for 2 algorithms and n<=10
   *
   * @throws IOException
   *           if i/o fails
   */
  @Test(timeout = 3600000)
  public void test_2_10() throws IOException {
    if (TestBase.FAST_TESTS) {
      return;
    }
    this.__run(2, 10);
  }

  /**
   * test the runner for 3 algorithms and n<=10
   *
   * @throws IOException
   *           if i/o fails
   */
  @Test(timeout = 3600000)
  public void test_3_10() throws IOException {
    if (TestBase.FAST_TESTS) {
      return;
    }
    this.__run(3, 10);
  }

  /**
   * test the runner for 1 algorithm and n<=32
   *
   * @throws IOException
   *           if i/o fails
   */
  @Test(timeout = 3600000)
  public void test_1_32() throws IOException {
    if (TestBase.FAST_TESTS) {
      return;
    }
    this.__run(1, 32);
  }

  /**
   * test the runner for 2 algorithms and n<=32
   *
   * @throws IOException
   *           if i/o fails
   */
  @Test(timeout = 3600000)
  public void test_2_32() throws IOException {
    if (TestBase.FAST_TESTS) {
      return;
    }
    this.__run(2, 32);
  }

  /**
   * test the runner for 3 algorithms and n<=32
   *
   * @throws IOException
   *           if i/o fails
   */
  @Test(timeout = 3600000)
  public void test_3_32() throws IOException {
    if (TestBase.FAST_TESTS) {
      return;
    }
    this.__run(3, 32);
  }

  /**
   * test the runner
   *
   * @param algos
   *          the number of algorithms
   * @param maxN
   *          the maximum n value
   */
  @SuppressWarnings({ "static-method", "unchecked", "rawtypes" })
  private void __run(final int algos, final int maxN) {
    try {
      final Path dest = Files
          .createTempDirectory(Runner_Test.class.getSimpleName());

      final __Algo[] array = new __Algo[algos];
      final Map.Entry<Algorithm, WModel_SingleObjective.Factory>[] setups = new Map.Entry[algos];

      for (int i = algos; (--i) >= 0;) {
        array[i] = new __Algo(new HashMap<>(), Integer.toString(i), maxN);
        setups[i] = Runner.setup(array[i]);
      }

      Runner.run(//
          maxN, //
          dest, //
          setups);
      SimpleParallelExecutor.waitForAll();

      for (final __Algo a : array) {
        if (a.m_caught != null) {
          throw a.m_caught;
        }
        final String algods = a.m_name;
        final Path dd = dest.resolve(algods);
        Assert.assertTrue(Files.exists(dd));
        Assert.assertTrue(Files.isDirectory(dd));

        Assert.assertTrue(a.m_n_mu_nu_gamma.size() > 0);
        Assert.assertNotNull(a.m_n_mu_nu_gamma.get(Integer.valueOf(maxN)));

        for (final Integer n : a.m_n_mu_nu_gamma.keySet()) {
          final String nds = ("n=" + n.intValue()); //$NON-NLS-1$
          final Path nd = dd.resolve(nds);
          Assert.assertTrue(Files.exists(nd));
          Assert.assertTrue(Files.isDirectory(nd));
          final HashMap<Integer, HashMap<Integer, HashMap<Integer, AtomicInteger>>> mu_nu_gamma = a.m_n_mu_nu_gamma
              .get(n);
          Assert.assertTrue(mu_nu_gamma.size() > 0);

          for (final Integer mu : mu_nu_gamma.keySet()) {
            final String muds = (nds + "_mu=" + mu.intValue()); //$NON-NLS-1$
            final Path mud = nd.resolve(muds);
            Assert.assertTrue(Files.exists(mud));
            Assert.assertTrue(Files.isDirectory(mud));
            final HashMap<Integer, HashMap<Integer, AtomicInteger>> nu_gamma = mu_nu_gamma
                .get(mu);
            Assert.assertTrue(nu_gamma.size() > 0);

            for (final Integer nu : nu_gamma.keySet()) {
              final String nuds = (muds + "_nu=" + nu.intValue()); //$NON-NLS-1$
              final Path nud = mud.resolve(nuds);
              Assert.assertTrue(Files.exists(nud));
              Assert.assertTrue(Files.isDirectory(nud));
              final HashMap<Integer, AtomicInteger> gammas = nu_gamma
                  .get(nu);
              Assert.assertTrue(gammas.size() > 0);

              for (final Integer gamma : gammas.keySet()) {

                final String gammads = (nuds + "_gamma=" + //$NON-NLS-1$
                    gamma.intValue());
                final Path gammad = nud.resolve(gammads);
                Assert.assertTrue(Files.exists(gammad));
                Assert.assertTrue(Files.isDirectory(gammad));
                final AtomicInteger gammaz = gammas.get(gamma);

                Assert.assertEquals(gammaz.intValue(), 100);

                final Path[] logs = Files.list(gammad)
                    .toArray((i) -> new Path[i]);
                Assert.assertEquals(logs.length, 100);
                for (final Path log : logs) {
                  Assert.assertTrue(Files.exists(log));
                  Assert.assertTrue(Files.isRegularFile(log));
                  Assert.assertTrue(Files.size(log) > 0L);
                }
              }
            }

          }
        }
      }

      IOUtils.delete(dest);

      for (int i = array.length; (--i) >= 0;) {
        for (int j = i; (--j) >= 0;) {
          Assert.assertEquals(array[i].m_n_mu_nu_gamma,
              array[j].m_n_mu_nu_gamma);
        }
      }

    } catch (final IOException ieo) {
      Assert.fail(ieo.getMessage());
    }
  }

  /** the internal algorithm */
  private static final class __Algo extends Algorithm<boolean[]> {
    /** the internal map */
    final HashMap<Integer, HashMap<Integer, HashMap<Integer, HashMap<Integer, AtomicInteger>>>> m_n_mu_nu_gamma;
    /** the name */
    final String m_name;
    /** the maximum permitted n */
    final int m_max_n;

    /** the assertion error caught */
    AssertionError m_caught;

    /**
     * create
     *
     * @param n_mu_nu_gamma
     *          the map to store the experiments
     * @param name
     *          the name
     * @param max_n
     *          the maximum permitted n
     */
    __Algo(
        final HashMap<Integer, HashMap<Integer, HashMap<Integer, HashMap<Integer, AtomicInteger>>>> n_mu_nu_gamma,
        final String name, final int max_n) {
      super();
      this.m_n_mu_nu_gamma = Objects.requireNonNull(n_mu_nu_gamma);
      this.m_name = Objects.requireNonNull(name);
      this.m_max_n = max_n;
    }

    /** {@inheritDoc} */
    @Override
    public final String toString() {
      return this.m_name;
    }

    /** {@inheritDoc} */
    @Override
    public void solve(final WModel_SingleObjective<boolean[]> f,
        final Random random) {
      final boolean[] sc;

      if (this.m_caught != null) {
        return;
      }

      try {
        Assert.assertNotNull(f);
        Assert.assertNotNull(random);

        final int len = f.get_candidate_solution_length();
        final int n = f.get_n();
        final int mu = f.get_mu();
        final int nu = f.get_nu();
        final int gamma = f.get_gamma();

        Assert.assertTrue(len > 0);
        Assert.assertTrue(len >= n);
        Assert.assertTrue(len >= mu);
        Assert.assertTrue(len == (n * mu));
        Assert.assertTrue((len / n) == mu);
        Assert.assertTrue((len / mu) == n);
        Assert.assertTrue(mu > 0);
        Assert.assertTrue(mu <= 4);
        Assert.assertTrue(n >= Math.min(10, this.m_max_n));
        Assert.assertTrue(n <= this.m_max_n);
        Assert.assertTrue(gamma >= 0);
        Assert.assertTrue(gamma <= ((n * (n - 1)) / 2));
        Assert.assertTrue(nu >= 2);
        Assert.assertTrue(nu <= f.get_n());
        Assert.assertTrue((nu <= 2) || (((((nu - 2) / 4) * 4) + 2) != nu));

        HashMap<Integer, HashMap<Integer, HashMap<Integer, AtomicInteger>>> mu_nu_gamma;
        HashMap<Integer, HashMap<Integer, AtomicInteger>> nu_gamma;
        HashMap<Integer, AtomicInteger> sgamma;

        final Integer in = Integer.valueOf(n);
        final Integer imu = Integer.valueOf(mu);
        final Integer inu = Integer.valueOf(nu);
        final Integer igamma = Integer.valueOf(gamma);

        synchronized (this.m_n_mu_nu_gamma) {
          mu_nu_gamma = this.m_n_mu_nu_gamma.get(in);
          if (mu_nu_gamma == null) {
            mu_nu_gamma = new HashMap<>();
            this.m_n_mu_nu_gamma.put(in, mu_nu_gamma);
          }

          nu_gamma = mu_nu_gamma.get(imu);
          if (nu_gamma == null) {
            nu_gamma = new HashMap<>();
            mu_nu_gamma.put(imu, nu_gamma);
          }

          sgamma = nu_gamma.get(inu);
          if (sgamma == null) {
            sgamma = new HashMap<>();
            nu_gamma.put(inu, sgamma);
          }

          final AtomicInteger cnt = sgamma.get(igamma);
          if (cnt != null) {
            cnt.incrementAndGet();
          } else {
            sgamma.put(igamma, new AtomicInteger(1));
          }
        }

        sc = new boolean[len];
        for (int i = len; (--i) >= 0;) {
          sc[i] = random.nextBoolean();
        }
        f.applyAsInt(sc);
        for (int i = 10; (--i) >= 0;) {
          sc[random.nextInt(len)] ^= true;
          f.applyAsInt(sc);
        }

      } catch (final AssertionError er) {
        this.m_caught = er;
      }
    }
  }
}
