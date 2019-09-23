package cn.edu.hfuu.iao.WModel_Experiments_SO.examples.sa;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Random;

import cn.edu.hfuu.iao.WModel.WModel_SingleObjective;
import cn.edu.hfuu.iao.WModel_Experiments_SO.Algorithm_Boolean;

/**
 * A Simulated Annealing algorithm which tries to flip each bit with the
 * same probability and uses a logarithmic temperature schedule
 */
public final class StandardFlipSimulatedAnnealingLog
    extends Algorithm_Boolean {

  /** the starting temperature */
  private final int m_T0;

  /**
   * create
   *
   * @param T0
   *          the starting temperature
   */
  public StandardFlipSimulatedAnnealingLog(final int T0) {
    super();
    if (T0 <= 0) {
      throw new IllegalArgumentException("Invalid T0 " //$NON-NLS-1$
          + T0);
    }
    this.m_T0 = T0;
  }

  /** {@inheritDoc} */
  @Override
  public final void solve(final WModel_SingleObjective<boolean[]> f,
      final Random random) {
    final int n = f.get_candidate_solution_length();

    // create the first, random solution
    boolean[] solution_1 = new boolean[n];
    boolean[] solution_2 = new boolean[n];
    boolean[] solution_temp = null;
    Algorithm_Boolean.randomize(solution_1, random);
    int accepted = f.applyAsInt(solution_1);// and evaluate it

    final long tau = 1L; // initialize step counter to 1

    while (!(f.shouldTerminate())) {
      StandardFlipSimulatedAnnealingLog.__mutation(solution_1, solution_2,
          random);
      final int current = f.applyAsInt(solution_2); // check solution

      if ((current <= accepted) || // accept if better solution OR
          (random.nextDouble() < // probability is e^(-dE/T)
          Math.exp((accepted - current) / // -dE == -(current-accepted)
              this.temperature(tau)))) {
        accepted = current; // keep it and remember
        solution_temp = solution_1;
        solution_1 = solution_2;
        solution_2 = solution_temp;
      }
    }
  }

  /**
   * compute the logarithmic temperature
   *
   * @param tau
   *          the step index
   * @return the temperature
   */
  private final double temperature(final long tau) {
    if (tau >= Long.MAX_VALUE) {
      return 0d;
    }
    return (this.m_T0 / Math.log((tau - 1L) + Math.E));
  }

  /**
   * Flip each bit with probability 1/n
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
    boolean changed = false;
    do {
      for (int i = dest.length; (--i) >= 0;) {
        final boolean change = (random.nextInt(dest.length) <= 0);
        dest[i] = (change ^ p1[i]);
        changed |= change;
      }
    } while (!changed);
  }

  /** {@inheritDoc} */
  @Override
  public final String toString() {
    return "Standard-Flip Simulated Annealing"; //$NON-NLS-1$
  }

  /** {@inheritDoc} */
  @Override
  public final String folderName() {
    return "StandardFlipSALog_" + this.m_T0; //$NON-NLS-1$
  }

  /** {@inheritDoc} */
  @Override
  public final void printDescription(final BufferedWriter writer)
      throws IOException {//
    super.printDescription(writer);
    writer.write("# flips: ~1");//$NON-NLS-1$
    writer.newLine();
    writer.write("# startTemperature: " + this.m_T0);//$NON-NLS-1$
    writer.newLine();
  }
}
