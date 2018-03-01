package cn.edu.hfuu.iao.WModel_Experiments.singleObjective.examples.base;

import java.util.Random;

import cn.edu.hfuu.iao.WModel.WModel_SingleObjective;
import cn.edu.hfuu.iao.WModel_Experiments.singleObjective.Algorithm_Boolean;

/**
 * An exhaustive enumeration method should be able to solve smaller
 * problems well. We start at a random point and just keep iterating over
 * the neighboring solutions.
 */
public final class Exhaustive_Enumerator extends Algorithm_Boolean {

  /** create */
  public Exhaustive_Enumerator() {
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

    while (!(f.shouldTerminate())) {
      f.applyAsInt(solution);
      for (int i = n; (--i) >= 0;) {
        // Toggle a bit. If the bit toggles to true, we continue toggling.
        // If it toggles to false, we stop.
        if (solution[i] ^= true) {
          break;
        }
      }
    }
  }

  /** {@inheritDoc} */
  @Override
  public final String toString() {
    return "Exhaustive Enumeration"; //$NON-NLS-1$
  }

  /** {@inheritDoc} */
  @Override
  public final String folderName() {
    return "ee"; //$NON-NLS-1$
  }
}
