package cn.edu.hfuu.iao.WModel_Experiments.singleObjective.examples.ea;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

import cn.edu.hfuu.iao.WModel.WModel_SingleObjective;
import cn.edu.hfuu.iao.WModel_Experiments.singleObjective.Algorithm_Boolean;

/**
 * A (mu+lambda) EA using bit-flip mutation and uniform crossover, which
 * prunes multiple occurences of the same solution automatically together
 * with frequency fitness assignment.
 */
public class FFA_EA extends Algorithm_Boolean {
  /** the number of parents */
  private final int m_mu;
  /** the number of offsprings */
  private final int m_lambda;
  /** the crossover rate */
  private final double m_crossoverRate;

  /**
   * create
   *
   * @param mu
   *          the mu
   * @param lambda
   *          the lambda
   * @param crossoverRate
   *          the crossover rate
   */
  public FFA_EA(final int mu, final int lambda,
      final double crossoverRate) {
    super();
    this.m_mu = mu;
    this.m_lambda = lambda;
    this.m_crossoverRate = crossoverRate;
  }

  /** {@inheritDoc} */
  @Override
  public final void solve(final WModel_SingleObjective<boolean[]> f,
      final Random random) {
    final int n = f.get_candidate_solution_length();
    final long[] ffa_table = new long[f.get_n() + 1];

    // allocate the necessary data structure
    final __Individual[] all = new __Individual[this.m_lambda + this.m_mu];

    // fill the individuals with random solutions
    for (int i = all.length; (--i) >= 0;) {
      Algorithm_Boolean.randomize(//
          (all[i] = new __Individual(n)).m_solution, random);
    }

    int startIndex = 0;
    // repeat
    while (!(f.shouldTerminate())) {

      // evaluate solution quality
      if (startIndex < all.length) {
        for (int i = all.length; (--i) >= startIndex;) {
          all[i].m_quality = f.applyAsInt(all[i].m_solution);
        }
      }

      // update the ffa table
      long max_ffa = 0L;
      for (final __Individual indi : all) {
        max_ffa = Math.max(max_ffa, ++ffa_table[indi.m_quality]);
      }

      // set the fitness
      for (final __Individual indi : all) {
        indi.m_fitness = ffa_table[indi.m_quality];
      }

      // punish duplicates in the population
      ++max_ffa;
      for (int i = all.length; (--i) > 0;) {
        final boolean[] current = all[i].m_solution;
        for (int j = i; (--j) >= i;) {
          if (Arrays.equals(current, all[j].m_solution)) {
            all[i].m_fitness += max_ffa;
          }
        }
      }

      // select
      Arrays.sort(all);

      // create the offspring
      for (int i = all.length, parent1Idx = 0; (--i) >= this.m_mu;) {
        // the destination and the parent
        final boolean[] dest = all[i].m_solution;
        final boolean[] parent_1 = all[parent1Idx].m_solution;

        reproduce: {
          // should we do crossover or mutation?
          if (random.nextDouble() < this.m_crossoverRate) {
            // the new individual should result from crossover
            for (int trials = 10; (--trials) >= 0;) {
              final boolean[] parent_2 = all[random
                  .nextInt(this.m_mu)].m_solution;
              if (parent_1 != parent_2) {
                FFA_EA.__crossover(parent_1, parent_2, dest, random);
                break reproduce;
              }
            }
          }
          FFA_EA.__mutation(parent_1, dest, random);
        }

        // step to the next first parent
        parent1Idx = ((parent1Idx + 1) % this.m_mu);
      }

      // all.length- mu = lambda new offspring to be created at the
      // beginning of the next generation
      startIndex = this.m_mu;
    }
  }

  /**
   * Uniform crossover
   *
   * @param p1
   *          the first parent
   * @param p2
   *          the second parent
   * @param dest
   *          the destination
   * @param random
   *          the random number generator
   */
  private static final void __crossover(final boolean[] p1,
      final boolean[] p2, final boolean[] dest, final Random random) {
    for (int i = dest.length; (--i) >= 0;) {
      dest[i] = random.nextBoolean() ? p1[i] : p2[i];
    }
  }

  /**
   * Single bit-flip mutation
   *
   * @param p1
   *          the first parent
   * @param dest
   *          the destination
   * @param random
   *          the random number generator
   */
  private static final void __mutation(final boolean[] p1,
      final boolean[] dest, final Random random) {
    System.arraycopy(p1, 0, dest, 0, p1.length);
    dest[random.nextInt(p1.length)] ^= true;
  }

  /** {@inheritDoc} */
  @Override
  public final String toString() {
    return "(mu+lambda) Frequency Fitness Evolutionary Algorithm"; //$NON-NLS-1$
  }

  /** {@inheritDoc} */
  @Override
  public final String folderName() {
    return "ffa_ea_m=" + this.m_mu + //$NON-NLS-1$
        "_l=" + this.m_lambda + //$NON-NLS-1$
        "_cr=" + //$NON-NLS-1$
        Double.toString(this.m_crossoverRate).substring(2);
  }

  /** {@inheritDoc} */
  @Override
  public final void printDescription(final BufferedWriter writer)
      throws IOException {//
    super.printDescription(writer);
    writer.write("# mu: ");//$NON-NLS-1$
    writer.write(Integer.toString(this.m_mu));
    writer.newLine();
    writer.write("# lambda: ");//$NON-NLS-1$
    writer.write(Integer.toString(this.m_lambda));
    writer.newLine();
    writer.write("# crossover rate: ");//$NON-NLS-1$
    writer.write(Double.toString(this.m_crossoverRate));
    writer.newLine();
    writer.write("# mutaton: 1-flip");//$NON-NLS-1$
    writer.newLine();
    writer.write("# crossover: uniform");//$NON-NLS-1$
    writer.newLine();
    writer.write("# fitness: FFA + punishing repeated strings");//$NON-NLS-1$
    writer.newLine();
  }

  /** the internal individual record */
  private static final class __Individual
      implements Comparable<__Individual> {
    /** the solution */
    final boolean[] m_solution;

    /** the quality */
    int m_quality;

    /** the fitness */
    long m_fitness;

    /**
     * create
     *
     * @param n
     *          the bit string length
     */
    __Individual(final int n) {
      super();
      this.m_solution = new boolean[n];
    }

    /** {@inheritDoc} */
    @Override
    public final int compareTo(final __Individual o) {
      final int result = Long.compare(this.m_fitness, o.m_fitness);
      if (result != 0) {
        return result;
      }
      return Integer.compare(this.m_quality, o.m_quality);
    }
  }
}
