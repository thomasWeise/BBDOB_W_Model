package cn.edu.hfuu.iao.WModel;

import java.util.Arrays;

/** Here we reproduce the example given in our paper. */
public class Paper_Example_2 {

  /**
   * The main routine
   *
   * @param args
   *          the args (ignored)
   */
  public static final void main(final String[] args) {
    final boolean[] x = { false, true, false, true, false, true, true,
        false, false, false, false, false, true, true, true, false, true,
        false, false, true, false, false, true, false, false, false, false,
        true };

    System.out.print("Start: "); //$NON-NLS-1$
    Paper_Example_2.__print(x);
    System.out.println();

    final int mu = 2;
    final boolean[] x_after_neutrality = new boolean[x.length / mu];
    WModel_Boolean.neutrality(x, 2, x_after_neutrality);
    System.out.print("Neutrality: ol="); //$NON-NLS-1$
    System.out.print(x.length);
    System.out.print(", nl="); //$NON-NLS-1$
    System.out.print(x_after_neutrality.length);
    System.out.print(": "); //$NON-NLS-1$
    Paper_Example_2.__print(x_after_neutrality);
    System.out.println();

    final boolean[] x_after_epistasis = new boolean[x_after_neutrality.length];
    WModel_Boolean.epistasis(x_after_neutrality, 4, x_after_epistasis);
    System.out.print("Epistasis: "); //$NON-NLS-1$
    Paper_Example_2.__print(x_after_epistasis);
    System.out.println();

    System.out.print("Objective: "); //$NON-NLS-1$
    final int f = WModel_Boolean.f(x_after_epistasis,
        x_after_epistasis.length);
    System.out.println(f);

    final int gamma = 15;
    final int gammaPrime = WModel_Ruggedness.ruggedness_translate(gamma,
        x_after_epistasis.length);
    System.out.print("Gamma "); //$NON-NLS-1$
    System.out.print(gamma);
    System.out.print(": "); //$NON-NLS-1$
    System.out.println(Arrays.toString(WModel_Ruggedness
        .ruggedness_raw(gamma, x_after_epistasis.length)));

    final int[] perm = WModel_Ruggedness.ruggedness(gamma,
        x_after_epistasis.length);
    System.out.print("Gamma' "); //$NON-NLS-1$
    System.out.print(gammaPrime);
    System.out.print(": "); //$NON-NLS-1$
    System.out.println(Arrays.toString(perm));

    System.out.print("f': "); //$NON-NLS-1$
    System.out.println(perm[f]);

    /*
     * for(int i =0; i < perm.length; i++){ System.out.print(i);
     * System.out.print('\t'); System.out.println(perm[i]);
     * System.out.print(i+1); System.out.print('\t');
     * System.out.println(perm[i]); }
     */
  }

  /**
   * print the boolean vector
   *
   * @param x
   *          the parameter
   */
  private static final void __print(final boolean[] x) {
    int i = 0;
    for (final boolean b : x) {
      System.out.print(b ? '1' : '0');
      if (((++i) % 4) == 0) {
        System.out.print(' ');
      }
    }
  }
}
