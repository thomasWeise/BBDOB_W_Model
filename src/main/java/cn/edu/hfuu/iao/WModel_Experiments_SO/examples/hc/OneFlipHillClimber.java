package cn.edu.hfuu.iao.WModel_Experiments_SO.examples.hc;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Random;

import cn.edu.hfuu.iao.WModel.WModel_SingleObjective;
import cn.edu.hfuu.iao.WModel_Experiments_SO.Algorithm_Boolean;

/**
 * A simple Hill Climbing algorithm which tries to flip a single bit in
 * each iteration and accepts the new solution if it is better.
 */
public final class OneFlipHillClimber extends Algorithm_Boolean {

  /** create */
  public OneFlipHillClimber() {
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
      final int index = random.nextInt(n); // choose a bit to flip
      solution[index] ^= true; // flip the bit
      final int current = f.applyAsInt(solution); // check the new solution
      if (current > best) { // if it is worse...
        solution[index] ^= true; // ...flip bit again = undo
      } else { // if it is better (or equal)
        best = current; // keep it and remember
      }
    }
  }

  /** {@inheritDoc} */
  @Override
  public final String toString() {
    return "1-Flip Hill Climber"; //$NON-NLS-1$
  }

  /** {@inheritDoc} */
  @Override
  public final String folderName() {
    return "1FlipHC"; //$NON-NLS-1$
  }

  /** {@inheritDoc} */
  @Override
  public final void printDescription(final BufferedWriter writer)
      throws IOException {//
    super.printDescription(writer);
    writer.write("# flips: 1");//$NON-NLS-1$
    writer.newLine();
    writer.write("# restarts: false");//$NON-NLS-1$
    writer.newLine();
  }
}
