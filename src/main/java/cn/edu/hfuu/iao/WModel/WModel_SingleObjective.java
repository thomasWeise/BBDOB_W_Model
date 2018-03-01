package cn.edu.hfuu.iao.WModel;

import java.util.function.ToIntFunction;

/**
 * A single objective function implementing the W-Model
 *
 * @param <T>
 *          the solution type
 */
public abstract class WModel_SingleObjective<T>
    implements ToIntFunction<T> {

  /** create the single-objective version of the W-Model */
  protected WModel_SingleObjective() {
    super();
  }

  /**
   * get the {@code n} parameter
   *
   * @return the {@code n} parameter
   */
  public abstract int get_n();

  /**
   * get the {@code mu} parameter
   *
   * @return the {@code mu} parameter
   */
  public abstract int get_mu();

  /**
   * get the {@code nu} parameter
   *
   * @return the {@code nu} parameter
   */
  public abstract int get_nu();

  /**
   * get the {@code gamma} parameter
   *
   * @return the {@code gamma} parameter
   */
  public abstract int get_gamma();

  /**
   * get the length of a candidate solution
   *
   * @return the length of a candidate solution
   */
  public abstract int get_candidate_solution_length();

  /**
   * Should we terminate?
   *
   * @return {@code true} if termination is required, {@code false}
   *         otherwise
   */
  @SuppressWarnings("static-method")
  public boolean shouldTerminate() {
    return false;
  }

  /**
   * An interface for creating objective functions
   *
   * @param <T>
   *          the representation
   */
  public static interface Factory<T> {
    /**
     * create the single-objective version of the W-Model
     *
     * @param _n
     *          the reduced size of a candidate solution
     * @param _mu
     *          the neutrality factor
     * @param _nu
     *          the epistasis nu
     * @param _gamma
     *          the ruggedness gamma
     * @return the instance
     */
    public abstract WModel_SingleObjective<T> create(final int _n,
        final int _mu, final int _nu, final int _gamma);
  }
}
