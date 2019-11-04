package cn.edu.hfuu.iao.WModel;

import java.util.function.Supplier;

/**
 * With this class, you can obtain a stream of default instances
 * of the W-Model for single-objective optimization
 */
public class WModel_SingleObjective_DefaultInstances {
  /** the fixed n, m, nu, and gamma values */
  private static final int[][] __n_m_nu_gamma = {
      { 10, 2, 6, 10 }, { 10, 2, 6, 18 }, { 16, 1, 5, 72 },
      { 16, 3, 9, 72 }, { 25, 1, 23, 90 }, { 32, 1, 2, 397 },
      { 32, 4, 11, 0 }, { 32, 4, 14, 0 }, { 32, 4, 8, 128 },
      { 50, 1, 36, 245 }, { 50, 2, 21, 256 }, { 50, 3, 16, 613 },
      { 64, 2, 32, 256 }, { 64, 3, 21, 16 }, { 64, 3, 21, 256 },
      { 64, 3, 21, 403 }, { 64, 4, 52, 2 }, { 75, 1, 60, 16 },
      { 75, 2, 32, 4 },

  };

  /**
   * Obtain a sequence of suppliers for creating the default
   * instances of the W-Model objective function in the
   * {@code boolean[]} representation
   *
   * @return the sequence of suppliers
   */
  @SuppressWarnings("unchecked")
  public static final Supplier<WModel_SingleObjective_Boolean>[]
      default_instances_boolean() {
    final Supplier<WModel_SingleObjective_Boolean>[] result =
        new Supplier[WModel_SingleObjective_DefaultInstances.__n_m_nu_gamma.length];

    for (int i = result.length; (--i) >= 0;) {
      final int[] params =
          WModel_SingleObjective_DefaultInstances.__n_m_nu_gamma[i];
      result[i] =
          (() -> ((WModel_SingleObjective_Boolean) (WModel_SingleObjective_Boolean
              .factory().create(params[0], params[1], params[2],
                  params[3]))));
    }

    return result;
  }

  public static final void main(final String[] params) {
    for (final Supplier<
        WModel_SingleObjective_Boolean> s : WModel_SingleObjective_DefaultInstances
            .default_instances_boolean()) {
      final WModel_SingleObjective_Boolean f = s.get();
      System.out.println("n: " + f.get_n() + //$NON-NLS-1$
          ", m: " + f.get_mu() + //$NON-NLS-1$
          ", nu: " + f.get_nu() + //$NON-NLS-1$
          ", gamma: " + f.get_gamma());//$NON-NLS-1$
    }
  }
}
