package cn.edu.hfuu.iao.WModel_Experiments_SO;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Assert;
import org.junit.Test;

import cn.edu.hfuu.iao.Internal_Base;
import cn.edu.hfuu.iao.WModel.WModel_SingleObjective;
import cn.edu.hfuu.iao.utils.IOUtils;
import cn.edu.hfuu.iao.utils.SimpleParallelExecutor;

/**
 * A Test for Runners.
 */
public class Runner_Test extends Internal_Base {

  /**
   * test the runner
   *
   * @throws IOException
   *           if i/o fails
   */
  @SuppressWarnings({ "static-method", "unchecked" })
  @Test(timeout = 3600000)
  public void testRunner() throws IOException {
    final int maxN = Internal_Base.FAST_TESTS ? 21 : 33;
    final Path dest = Files
        .createTempDirectory(Runner_Test.class.getSimpleName());

    final __Algo a1 = new __Algo(new HashMap<>(), "a"); //$NON-NLS-1$
    final __Algo a2 = new __Algo(new HashMap<>(), "b");//$NON-NLS-1$

    Runner.run(//
        maxN, //
        dest, //
        Runner.setup(a1), Runner.setup(a2));
    SimpleParallelExecutor.waitForAll();

    for (final __Algo a : new __Algo[] { a1, a2 }) {
      final String algods = a.m_name;
      final Path dd = dest.resolve(algods);
      Assert.assertTrue(Files.exists(dd));
      Assert.assertTrue(Files.isDirectory(dd));

      for (final Integer n : a.m_n_mu_nu_gamma.keySet()) {
        final String nds = ("n=" + n.intValue()); //$NON-NLS-1$
        final Path nd = dd.resolve(nds);
        Assert.assertTrue(Files.exists(nd));
        Assert.assertTrue(Files.isDirectory(nd));
        final HashMap<Integer, HashMap<Integer, HashMap<Integer, AtomicInteger>>> mu_nu_gamma = a.m_n_mu_nu_gamma
            .get(n);

        for (final Integer mu : mu_nu_gamma.keySet()) {
          final String muds = (nds + "_mu=" + mu.intValue()); //$NON-NLS-1$
          final Path mud = nd.resolve(muds);
          Assert.assertTrue(Files.exists(mud));
          Assert.assertTrue(Files.isDirectory(mud));
          final HashMap<Integer, HashMap<Integer, AtomicInteger>> nu_gamma = mu_nu_gamma
              .get(mu);

          for (final Integer nu : nu_gamma.keySet()) {
            final String nuds = (muds + "_nu=" + nu.intValue()); //$NON-NLS-1$
            final Path nud = mud.resolve(nuds);
            Assert.assertTrue(Files.exists(nud));
            Assert.assertTrue(Files.isDirectory(nud));
            final HashMap<Integer, AtomicInteger> gammas = nu_gamma
                .get(nu);

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
  }

  /** the internal algorithm */
  private static final class __Algo extends Algorithm<boolean[]> {
    /** the internal map */
    final HashMap<Integer, HashMap<Integer, HashMap<Integer, HashMap<Integer, AtomicInteger>>>> m_n_mu_nu_gamma;
    /** the name */
    final String m_name;

    /**
     * create
     *
     * @param n_mu_nu_gamma
     *          the map to store the experiments
     * @param name
     *          the name
     */
    __Algo(
        final HashMap<Integer, HashMap<Integer, HashMap<Integer, HashMap<Integer, AtomicInteger>>>> n_mu_nu_gamma,
        final String name) {
      super();
      this.m_n_mu_nu_gamma = Objects.requireNonNull(n_mu_nu_gamma);
      this.m_name = Objects.requireNonNull(name);
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
      Assert.assertTrue(n >= 10);
      Assert.assertTrue(n <= 33);
      Assert.assertTrue(gamma >= 0);
      Assert.assertTrue(gamma <= ((n * (n - 1)) / 2));
      Assert.assertTrue(nu >= 2);
      Assert.assertTrue(nu <= f.get_n());
      Assert.assertTrue((n <= 2) || (((((nu - 2) / 4) * 4) + 2) != nu));

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
    }
  }
}
