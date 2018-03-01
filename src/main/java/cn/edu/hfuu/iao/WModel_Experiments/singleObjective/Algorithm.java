package cn.edu.hfuu.iao.WModel_Experiments.singleObjective;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Random;

import cn.edu.hfuu.iao.WModel.WModel_SingleObjective;

/**
 * An optimization algorithm
 *
 * @param <T>
 *          the representation used
 */
public abstract class Algorithm<T> {

  /**
   * Try to solve the given problem, i.e., run until
   * {@link WModel_SingleObjective#shouldTerminate()}} becomes
   * {@code true}. This method must be implemented in a thread-safe way.
   *
   * @param f
   *          the objective function
   * @param random
   *          the random number generator
   */
  public abstract void solve(final WModel_SingleObjective<T> f,
      final Random random);

  /**
   * Print the algorithm's description to the given writer. Override this
   * method to provide additional information. Each line should start with
   * '#' followed by a parameter name followed by ": ", followed by the
   * parameter value.
   *
   * @param writer
   *          the writer
   * @throws IOException
   *           if i/o fails
   */
  public void printDescription(final BufferedWriter writer)
      throws IOException {//
  }

  /**
   * Return the algorithm's name
   *
   * @return the algorithm name
   */
  @Override
  public abstract String toString();

  /**
   * get the name to be used for the algorithm folder
   *
   * @return the name to be used for the algorithm folder.
   */
  public String folderName() {
    return this.toString().replace(' ', '_');
  }
}
