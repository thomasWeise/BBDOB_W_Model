package cn.edu.hfuu.iao.WModel_Experiments_SO.examples.hc;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Random;

import cn.edu.hfuu.iao.WModel.WModel_SingleObjective;
import cn.edu.hfuu.iao.WModel_Experiments_SO.Algorithm_Boolean;

/**
 * <p>
 * A simple Hill Climbing algorithm which tries to flip each bit with the
 * same probability and accepts the new solution if it is better.
 * </p>
 * <p>
 * This algorithm will re-start if it did not find any solution equally
 * good or better than the optimum for {@code z} steps. {@code z} is
 * initialized to half of the length of a candidate solution and increased
 * by at least 6.25% at every restart.
 * </p>
 */
public final class StandardFlipHillClimberWithRestarts
    extends Algorithm_Boolean {
  /** create */
  public StandardFlipHillClimberWithRestarts() {
    super();
  }

  /** {@inheritDoc} */
  @Override
  public final void solve(final WModel_SingleObjective<boolean[]> f,
      final Random random) {
    final int n = f.get_candidate_solution_length();

    boolean[] solution_1 = new boolean[n];
    boolean[] solution_2 = new boolean[n];
    boolean[] solution_temp = null;

    int nextRestart = n >>> 1;
    restart: while (!(f.shouldTerminate())) {
      int withoutImprovementOrEqual = 0; // reset no-improvement counter

      // create the first, random solution
      Algorithm_Boolean.randomize(solution_1, random);
      int best = f.applyAsInt(solution_1);// and evaluate it

      while (!(f.shouldTerminate())) {
        StandardFlipHillClimberWithRestarts.__mutation(solution_1,
            solution_2, random);
        final int current = f.applyAsInt(solution_2); // check the new
                                                      // solution
        if (current <= best) { // if it is better (or equal)
          best = current; // keep it and remember
          solution_temp = solution_1;
          solution_1 = solution_2;
          solution_2 = solution_temp;
          withoutImprovementOrEqual = 0; // reset no-improvement counter
        } else { // if it is worse...
          if ((++withoutImprovementOrEqual) >= nextRestart) {
            // increase restart counter
            nextRestart = Math.max(nextRestart + 1,
                nextRestart + (nextRestart >>> 4));
            continue restart; // we need to do a restart
          }
        }
      }
    }
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
    return "Standard-Flip Hill Climber with Restarts"; //$NON-NLS-1$
  }

  /** {@inheritDoc} */
  @Override
  public final String folderName() {
    return "StandardFlipHCrs"; //$NON-NLS-1$
  }

  /** {@inheritDoc} */
  @Override
  public final void printDescription(final BufferedWriter writer)
      throws IOException {//
    super.printDescription(writer);
    writer.write("# flips: ~1");//$NON-NLS-1$
    writer.newLine();
    writer.write("# restarts: true");//$NON-NLS-1$
    writer.newLine();
    writer.write("# restart after: n/2 non-improving FEs");//$NON-NLS-1$
    writer.newLine();
    writer.write("# restarts increase: max(delay+1, delay+delay>>>4)");//$NON-NLS-1$
    writer.newLine();
  }
}
