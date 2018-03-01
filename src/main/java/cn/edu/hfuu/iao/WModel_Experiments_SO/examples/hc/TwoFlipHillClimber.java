package cn.edu.hfuu.iao.WModel_Experiments_SO.examples.hc;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Random;

import cn.edu.hfuu.iao.WModel.WModel_SingleObjective;
import cn.edu.hfuu.iao.WModel_Experiments_SO.Algorithm_Boolean;

/**
 * A Hill Climbing algorithm which tries to flip one or two bits in each
 * iteration and accepts the new solution if it is better.
 */
public final class TwoFlipHillClimber extends Algorithm_Boolean {

  /** create */
  public TwoFlipHillClimber() {
    super();
  }

  /** {@inheritDoc} */
  @Override
  public final void solve(final WModel_SingleObjective<boolean[]> f,
      final Random random) {
    final int n = f.get_candidate_solution_length();

    // create the first, random solution
    final boolean[] solution = new boolean[n];
    Algorithm_Boolean.randomize(solution, random);
    int best = f.applyAsInt(solution);// and evaluate it

    while (!(f.shouldTerminate())) {
      final int indexA = random.nextInt(n); // choose a bit to flip
      final int indexB = random.nextInt(n); // choose another bit to flip
      solution[indexA] ^= true; // flip the bit
      if (indexA != indexB) {
        solution[indexB] ^= true; // flip the other bit
      }
      final int current = f.applyAsInt(solution); // check solution
      if (current > best) { // compare
        solution[indexA] ^= true; // ...flip bit again = undo
        if (indexA != indexB) {
          solution[indexB] ^= true; // ...flip other bit again = undo
        }
      } else { // otherwise, remember objective value
        best = current;
      }
    }
  }

  /** {@inheritDoc} */
  @Override
  public final String toString() {
    return "2-Flip Hill Climber"; //$NON-NLS-1$
  }

  /** {@inheritDoc} */
  @Override
  public final String folderName() {
    return "2FlipHC"; //$NON-NLS-1$
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
  }
}
