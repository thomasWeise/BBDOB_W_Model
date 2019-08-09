package cn.edu.hfuu.iao.WModel_Experiments_SO.examples.hc;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Random;

import cn.edu.hfuu.iao.WModel.WModel_SingleObjective;
import cn.edu.hfuu.iao.WModel_Experiments_SO.Algorithm_Boolean;

/**
 * A simple Hill Climbing algorithm which tries to flip each bit with the
 * same probability
 */
public final class StandardFlipHillClimber extends Algorithm_Boolean {

  /** create */
  public StandardFlipHillClimber() {
    super();
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
    int best = f.applyAsInt(solution_1);// and evaluate it

    while (!(f.shouldTerminate())) {
      StandardFlipHillClimber.__mutation(solution_1, solution_2, random);
      final int current = f.applyAsInt(solution_2); // check the new
                                                    // solution
      if (current <= best) { // if it is better (or equal)
        best = current; // keep it and remember
        solution_temp = solution_1;
        solution_1 = solution_2;
        solution_2 = solution_temp;
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
    return "Standard-Flip Hill Climber"; //$NON-NLS-1$
  }

  /** {@inheritDoc} */
  @Override
  public final String folderName() {
    return "StandardFlipHC"; //$NON-NLS-1$
  }

  /** {@inheritDoc} */
  @Override
  public final void printDescription(final BufferedWriter writer)
      throws IOException {//
    super.printDescription(writer);
    writer.write("# flips: ~1");//$NON-NLS-1$
    writer.newLine();
    writer.write("# restarts: false");//$NON-NLS-1$
    writer.newLine();
  }
}
