package cn.edu.hfuu.iao.WModel_Experiments.singleObjective.examples.hc;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Random;

import cn.edu.hfuu.iao.WModel.WModel_SingleObjective;
import cn.edu.hfuu.iao.WModel_Experiments.singleObjective.Algorithm_Boolean;

/**
 * <p>
 * A simple Hill Climbing algorithm which tries to flip a single bit in
 * each iteration and accepts the new solution if it is better.
 * </p>
 * <p>
 * This algorithm will re-start if it did not find any solution equally
 * good or better than the optimum for {@code z} steps. {@code z} is
 * initialized to half of the length of a candidate solution and increased
 * by at least 6.25% at every restart.
 * </p>
 */
public final class OneFlipHillClimberWithRestarts
    extends Algorithm_Boolean {
  /** create */
  public OneFlipHillClimberWithRestarts() {
    super();
  }

  /** {@inheritDoc} */
  @Override
  public final void solve(final WModel_SingleObjective<boolean[]> f,
      final Random random) {
    final int n = f.get_candidate_solution_length();

    final boolean[] solution = new boolean[n];
    int nextRestart = n >>> 1;
    restart: while (!(f.shouldTerminate())) {
      int withoutImprovementOrEqual = 0; // reset no-improvement counter

      // create the first, random solution
      Algorithm_Boolean.randomize(solution, random);
      int best = f.applyAsInt(solution);// and evaluate it

      while (!(f.shouldTerminate())) {
        final int index = random.nextInt(n); // choose a bit to flip
        solution[index] ^= true; // flip the bit
        // check new solution
        final int current = f.applyAsInt(solution);
        if (current > best) { // if it is worse...
          solution[index] ^= true; // ...flip bit again = undo

          if ((++withoutImprovementOrEqual) >= nextRestart) {
            // increase restart counter
            nextRestart = Math.max(nextRestart + 1,
                nextRestart + (nextRestart >>> 4));
            continue restart; // we need to do a restart
          }
        } else { // otherwise, remember objective value
          best = current;
          withoutImprovementOrEqual = 0; // reset no-improvement counter
        }
      }
    }
  }

  /** {@inheritDoc} */
  @Override
  public final String toString() {
    return "1-Flip Hill Climber with Restarts"; //$NON-NLS-1$
  }

  /** {@inheritDoc} */
  @Override
  public final String folderName() {
    return "1FlipHCrs"; //$NON-NLS-1$
  }

  /** {@inheritDoc} */
  @Override
  public final void printDescription(final BufferedWriter writer)
      throws IOException {//
    super.printDescription(writer);
    writer.write("# flips: 1");//$NON-NLS-1$
    writer.newLine();
    writer.write("# restarts: true");//$NON-NLS-1$
    writer.newLine();
    writer.write("# restart after: n/2 non-improving FEs");//$NON-NLS-1$
    writer.newLine();
    writer.write("# restarts increase: max(delay+1, delay+delay>>>4)");//$NON-NLS-1$
    writer.newLine();
  }
}
