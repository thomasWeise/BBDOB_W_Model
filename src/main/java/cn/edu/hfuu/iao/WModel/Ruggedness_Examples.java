package cn.edu.hfuu.iao.WModel;

/** Here we reproduce the example given in our paper. */
public class Ruggedness_Examples {

  /**
   * The main routine
   *
   * @param args
   *          the args (ignored)
   */
  public static final void main(final String[] args) {

    for (final int n : new int[] { 100, 500, 1000 }) {
      System.out.println("####################"); //$NON-NLS-1$
      System.out.print("n=" + n); //$NON-NLS-1$
      final int maxGamma = WModel_Ruggedness.max_gamma(n);
      System.out.println(", gamma in 0..." + maxGamma); //$NON-NLS-1$

      System.out.println("gamma0\tgamma\tgamma'\tr[0]\tr[1]\t"); //$NON-NLS-1$

      for (int gamma0I = 0; gamma0I <= 20; gamma0I++) {
        final double gamma0 = gamma0I / 20d;
        final int gamma = (int) (Math.round(//
            (maxGamma * gamma0I) / 20.0d));
        System.out.print(gamma0);
        System.out.print('\t');
        System.out.print(gamma);
        System.out.print('\t');
        System.out.print(
            WModel_Ruggedness.ruggedness_translate(gamma, n));
        for (final int i : WModel_Ruggedness.ruggedness(gamma,
            n)) {
          System.out.print('\t');
          System.out.print(i);
        }
        System.out.println();
      }
      System.out.println("####################"); //$NON-NLS-1$
    }
  }
}
