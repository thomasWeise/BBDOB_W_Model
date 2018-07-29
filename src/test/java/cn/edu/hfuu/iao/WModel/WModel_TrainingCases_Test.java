package cn.edu.hfuu.iao.WModel;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.Assert;
import org.junit.Test;

import cn.edu.hfuu.iao.Internal_Base;

/** A test class for training cases */
public class WModel_TrainingCases_Test extends Internal_Base {

  /** check the paper example for training cases */
  @SuppressWarnings("static-method")
  @Test(timeout = 3600000)
  public void training_cases_paper_example_old() {
    final long[] cases = WModel_TrainingCases.fromString(//
        "*10001", //$NON-NLS-1$
        "0101*0", //$NON-NLS-1$
        "1*0111", //$NON-NLS-1$
        "111*01", //$NON-NLS-1$
        "11*101"//$NON-NLS-1$
    );

    // if first bit is 1, we would get score 1
    Assert.assertEquals(cases[0] >>> 32L, 1);
    // if first bit is zero, score is 3
    Assert.assertEquals(cases[0] & 0xffffffffL, 3);

    Assert.assertEquals(cases[1] >>> 32L, 0);
    Assert.assertEquals(cases[1] & 0xffffffffL, 4);

    Assert.assertEquals(cases[2] >>> 32L, 3);
    Assert.assertEquals(cases[2] & 0xffffffffL, 1);

    Assert.assertEquals(cases[3] >>> 32L, 1);
    Assert.assertEquals(cases[3] & 0xffffffffL, 3);

    Assert.assertEquals(cases[4] >>> 32L, 3);
    Assert.assertEquals(cases[4] & 0xffffffffL, 1);

    Assert.assertEquals(cases[5] >>> 32L, 1);
    Assert.assertEquals(cases[5] & 0xffffffffL, 4);
  }

  /** check the paper example for training cases */
  @SuppressWarnings("static-method")
  @Test(timeout = 3600000)
  public void training_cases_paper_example_new() {
    final long[] cases = WModel_TrainingCases.fromString(//
        "*10001", //$NON-NLS-1$
        "0101*0", //$NON-NLS-1$
        "0*0111", //$NON-NLS-1$
        "011*01", //$NON-NLS-1$
        "11*101"//$NON-NLS-1$
    );

    // if first bit is 1, we would get score 3
    Assert.assertEquals(cases[0] >>> 32L, 3);
    // if first bit is zero, score is 1
    Assert.assertEquals(cases[0] & 0xffffffffL, 1);

    Assert.assertEquals(cases[1] >>> 32L, 0);
    Assert.assertEquals(cases[1] & 0xffffffffL, 4);

    Assert.assertEquals(cases[2] >>> 32L, 3);
    Assert.assertEquals(cases[2] & 0xffffffffL, 1);

    Assert.assertEquals(cases[3] >>> 32L, 1);
    Assert.assertEquals(cases[3] & 0xffffffffL, 3);

    Assert.assertEquals(cases[4] >>> 32L, 3);
    Assert.assertEquals(cases[4] & 0xffffffffL, 1);

    Assert.assertEquals(cases[5] >>> 32L, 1);
    Assert.assertEquals(cases[5] & 0xffffffffL, 4);
  }

  /**
   * randomly generate and then test a set of training cases
   *
   * @param n
   *          the n
   * @param epsilon
   *          the epsilon
   * @param o
   *          the o
   * @param t
   *          the t
   * @param random
   *          the random number generator
   */
  private static final void __test_sample_training_case(final int n,
      final int epsilon, final int o, final int t, final Random random) {
    final long[] result = WModel_TrainingCases.training_cases_create(n,
        epsilon, o, t, random);
    Assert.assertEquals(n, result.length);
    int foundo = 0;
    int founde = 0;
    for (int i = n; (--i) >= 0;) {
      final int ones = ((int) (result[i] >>> 32L));
      final int zeros = ((int) (result[i] & 0xffffffffL));
      if (Internal_Base.optimum(i)) {
        founde += ones;
        foundo += (t - zeros);
      } else {
        founde += zeros;
        foundo += (t - ones);
      }
    }
    Assert.assertEquals(epsilon * t, founde);
    Assert.assertEquals(o * t, foundo);

    final int[] range = new int[3];
    WModel_TrainingCases.training_cases_objective_value_range(result,
        range);
    Assert.assertTrue(range[0] >= 0);
    Assert.assertTrue(range[1] >= range[0]);
    Assert.assertTrue(range[2] >= range[0]);
    Assert.assertTrue(range[1] >= range[2]);
    Assert.assertTrue(range[1] <= ((n - o) * t));
    Assert.assertTrue(range[0] <= (t * epsilon));
    Assert.assertTrue(range[2] <= (t * epsilon));
  }

  /** check the paper example for training cases */
  @SuppressWarnings("static-method")
  @Test(timeout = 3600000)
  public void random_training_cases() {
    final Random random = ThreadLocalRandom.current();

    final int maxN = (Internal_Base.FAST_TESTS ? 20 : 27);
    final int maxSamples = (Internal_Base.FAST_TESTS ? 15 : 21);

    for (int n = 1; n < maxN; n++) {
      for (int t = (3 * n); t > 0; t--) {
        for (int epsilon = n; epsilon >= 0; --epsilon) {
          for (int o = (n - epsilon); o >= 0; --o) {
            for (int samples = maxSamples; (--samples) >= 0;) {
              WModel_TrainingCases_Test.__test_sample_training_case(n,
                  epsilon, o, t, random);
            }
          }
        }
      }
    }
  }

  /**
   * randomly generate and then test a set of training cases
   *
   * @param n
   *          the n
   * @param epsilon
   *          the epsilon
   * @param o
   *          the o
   * @param t
   *          the t
   * @param random
   *          the random number generator
   */
  private static final void __test_sample_training_case_ruggedness(
      final int n, final int epsilon, final int o, final int t,
      final Random random) {
    final long[] result = WModel_TrainingCases.training_cases_create(n,
        epsilon, o, t, random);
    final int[] range = new int[3];
    WModel_TrainingCases.training_cases_objective_value_range(result,
        range);
    Assert.assertTrue(range[0] >= 0);
    Assert.assertTrue(range[1] >= range[0]);
    Assert.assertTrue(range[2] >= range[0]);
    Assert.assertTrue(range[1] >= range[2]);
    Assert.assertTrue(range[1] <= ((n - o) * t));
    Assert.assertTrue(range[0] <= (t * epsilon));
    Assert.assertTrue(range[2] <= (t * epsilon));

    final int validRange = range[1] - range[2];

    for (int gamma = WModel_Ruggedness
        .max_gamma(validRange); (--gamma) >= 0;) {
      final int[] ruggedness = WModel_TrainingCases.ruggedness(gamma,
          result);
      Assert.assertEquals(range[1] + 1, ruggedness.length);
      for (int i = range[0]; i >= 0; --i) {
        Assert.assertEquals(0, ruggedness[i]);
      }
      for (int i = range[2]; i >= range[0]; i--) {
        Assert.assertEquals(i - range[0], ruggedness[i]);
      }
      for (int i = range[1]; i >= range[2]; i--) {
        Assert.assertTrue(ruggedness[i] <= range[1]);
        Assert.assertTrue(ruggedness[i] >= (range[2] - range[1]));
      }
    }
  }

  /** check the paper example for training cases */
  @SuppressWarnings("static-method")
  @Test(timeout = 3600000)
  public void random_training_cases_ruggness() {
    final Random random = ThreadLocalRandom.current();

    final int maxN = Internal_Base.FAST_TESTS ? 9 : 11;
    final int maxSamples = Internal_Base.FAST_TESTS ? 10 : 21;

    for (int n = 1; n < maxN; n++) {
      for (int t = (3 * n); t > 0; t--) {
        for (int epsilon = n; epsilon >= 0; --epsilon) {
          for (int o = (n - epsilon); o >= 0; --o) {
            for (int samples = maxSamples; (--samples) >= 0;) {
              WModel_TrainingCases_Test
                  .__test_sample_training_case_ruggedness(n, epsilon, o, t,
                      random);
            }
          }
        }
      }
    }
  }
}
