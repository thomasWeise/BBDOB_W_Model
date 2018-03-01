package cn.edu.hfuu.iao.WModel_Experiments.singleObjective.examples.base;

import java.util.Random;

import cn.edu.hfuu.iao.WModel.WModel_SingleObjective;
import cn.edu.hfuu.iao.WModel_Experiments.singleObjective.Algorithm_Boolean;

/**
 * Random sampling will keep generating random solutions.
 */
public final class Random_Sampling extends Algorithm_Boolean {

  /** create */
  public Random_Sampling() {
    super();
  }

  /** {@inheritDoc} */
  @Override
  public final void solve(final WModel_SingleObjective<boolean[]> f,
      final Random random) {
    final int n = f.get_candidate_solution_length();

    // create the first, random solution
    final boolean[] solution = new boolean[n];

    while (!(f.shouldTerminate())) {
      Algorithm_Boolean.randomize(solution, random);
      f.applyAsInt(solution);
    }
  }

  /** {@inheritDoc} */
  @Override
  public final String toString() {
    return "Random Sampling"; //$NON-NLS-1$
  }

  /** {@inheritDoc} */
  @Override
  public final String folderName() {
    return "rs"; //$NON-NLS-1$
  }
}
