package cn.edu.hfuu.iao.WModel_Experiments_SO.examples.sa;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Random;

import cn.edu.hfuu.iao.WModel.WModel_SingleObjective;
import cn.edu.hfuu.iao.WModel_Experiments_SO.Algorithm_Boolean;

/**
 * A Simulated Annealing algorithm which tries to flip one bit and uses a
 * logarithmic temperature schedule
 */
public final class OneFlipSimulatedAnnealingLog extends Algorithm_Boolean {

  /** the starting temperature */
  private final int m_T0;

  /**
   * create
   *
   * @param T0
   *          the starting temperature
   */
  public OneFlipSimulatedAnnealingLog(final int T0) {
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
    final boolean[] solution = new boolean[n];
    Algorithm_Boolean.randomize(solution, random);
    int accepted = f.applyAsInt(solution);// and evaluate it

    final long tau = 1L; // initialize step counter to 1

    while (!(f.shouldTerminate())) {
      final int index = random.nextInt(n);
      solution[index] ^= true;
      final int current = f.applyAsInt(solution); // check solution

      if ((current <= accepted) || // accept if better solution OR
          (random.nextDouble() < // probability is e^(-dE/T)
          Math.exp((accepted - current) / // -dE == -(current-accepted)
              this.temperature(tau)))) {
        accepted = current; // keep it and remember
      } else {
        solution[index] ^= true;
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

  /** {@inheritDoc} */
  @Override
  public final String toString() {
    return "1-Flip Simulated Annealing"; //$NON-NLS-1$
  }

  /** {@inheritDoc} */
  @Override
  public final String folderName() {
    return "1FlipSALog_" + this.m_T0; //$NON-NLS-1$
  }

  /** {@inheritDoc} */
  @Override
  public final void printDescription(final BufferedWriter writer)
      throws IOException {//
    super.printDescription(writer);
    writer.write("# flips: 1");//$NON-NLS-1$
    writer.newLine();
    writer.write("# startTemperature: " + this.m_T0);//$NON-NLS-1$
    writer.newLine();
  }
}
