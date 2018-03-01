package cn.edu.hfuu.iao.WModel_Experiments.singleObjective.examples.hc;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Random;

import cn.edu.hfuu.iao.WModel.WModel_Permutation;
import cn.edu.hfuu.iao.WModel.WModel_SingleObjective;
import cn.edu.hfuu.iao.WModel_Experiments.singleObjective.Algorithm_Boolean;

/**
 * <p>
 * An {@code m}-flip Hill Climbing algorithm with restarts.
 * </p>
 * <p>
 * An {@code m}-flip Hill Climbing algorithm: In each iteration, group of
 * <code>1&le;m&le;n</code> bits are chosen for being flipped. If the
 * resulting solution is better than the old one, it is kept. Otherwise,
 * the bits are flipped again to revert the change. After a given bit has
 * been chosen for flipping, this bit will not be chosen again until every
 * other bit has been chosen. This eases implementation. The number
 * {@code m} is chosen randomly according to a pseudo-geometric
 * distribution (it is not really geometric, due to the limited range).
 * </p>
 * <p>
 * This algorithm will re-start if it did not find any solution equally
 * good or better than the optimum for {@code z} steps. {@code z} is
 * initialized to half of the length of a candidate solution and increased
 * by at least 6.25% at every restart.
 * </p>
 */
public final class MFlipHillClimberWithRestarts extends Algorithm_Boolean {

  /** create */
  public MFlipHillClimberWithRestarts() {
    super();
  }

  /** {@inheritDoc} */
  @Override
  public final void solve(final WModel_SingleObjective<boolean[]> f,
      final Random random) {
    final int n = f.get_candidate_solution_length();

    // create the first, random solution
    // create the first, random solution
    final boolean[] solution = new boolean[n];

    // create a permutation of the numbers from 0..1
    final int[] permutation = WModel_Permutation.canonical(n);
    int nextRestart = n >>> 1; // the restart counter

    restart: while (!(f.shouldTerminate())) {
      // main loop: independent restarts
      Algorithm_Boolean.randomize(solution, random);
      int best = f.applyAsInt(solution); // and evaluate it

      WModel_Permutation.shuffle(permutation, random); // shuffle it
      int permIndex = 0; // the index of the next index
      int withoutImprovementOrEqual = 0; // reset no-improvement counter

      while (!(f.shouldTerminate())) {// while we can continue

        // choose the number of bits to flip
        final int flipCount = MFlipHillClimberWithRestarts
            .__geometric((permutation.length - permIndex), random);
        for (int index = 0; index < flipCount; index++) { // flip them
          solution[permutation[permIndex + index]] ^= true;
        }

        final int current = f.applyAsInt(solution); // evaluate new
                                                    // solution
        if (current > best) { // if worse than before
          for (int index = 0; index < flipCount; index++) {
            solution[permutation[permIndex + index]] ^= true; // undo
          }
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

        permIndex += flipCount;// increase index in permutation
        if (permIndex >= permutation.length) {// after all indexes are used
          WModel_Permutation.shuffle(permutation, random); // shuffle it
          permIndex = 0;
        }
      }
    }
  }

  /**
   * This method returns a pseudo-geometrically distributed random number
   * with {@code p=0.5}. The value of this number will be in {@code 1..max}
   * . It is assumed that {@code max} is relatively small.
   *
   * @param max
   *          the maximum allowed result (the minimum is {@code 1})
   * @param rand
   *          the random number generator
   * @return the random number
   */
  private static final int __geometric(final int max, final Random rand) {
    int count;

    count = 1;
    while ((count < max) && rand.nextBoolean()) {
      count++;
    }
    return count;
  }

  /** {@inheritDoc} */
  @Override
  public final String toString() {
    return "m-Flip Hill Climber with Restarts"; //$NON-NLS-1$
  }

  /** {@inheritDoc} */
  @Override
  public final String folderName() {
    return "mFlipHCrs"; //$NON-NLS-1$
  }

  /** {@inheritDoc} */
  @Override
  public final void printDescription(final BufferedWriter writer)
      throws IOException {//
    super.printDescription(writer);
    writer.write("# flips: m");//$NON-NLS-1$
    writer.newLine();
    writer.write("# restarts: true");//$NON-NLS-1$
    writer.newLine();
    writer.write("# restart after: n/2 non-improving FEs");//$NON-NLS-1$
    writer.newLine();
    writer.write("# restarts increase: max(delay+1, delay+delay>>>4)");//$NON-NLS-1$
    writer.newLine();
  }
}
