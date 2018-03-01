package cn.edu.hfuu.iao.WModel_Experiments_SO;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Random;

/**
 * An optimization algorithm using the {@code boolean[]} representation
 */
public abstract class Algorithm_Boolean extends Algorithm<boolean[]> {

  /**
   * Randomize a given array
   *
   * @param array
   *          the array
   * @param random
   *          the random number generator
   */
  protected static final void randomize(final boolean[] array,
      final Random random) {
    for (int i = array.length; (--i) >= 0;) {
      array[i] = random.nextBoolean();
    }
  }

  /** {@inheritDoc} */
  @Override
  public void printDescription(final BufferedWriter writer)
      throws IOException {//
    super.printDescription(writer);
    writer.write("# representation: boolean[]"); //$NON-NLS-1$
    writer.newLine();
  }
}
