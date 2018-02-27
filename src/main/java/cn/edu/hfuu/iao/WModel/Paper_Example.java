package cn.edu.hfuu.iao.WModel;

/** Here we reproduce the example given in our paper. */
public class Paper_Example {

  /**
   * The main routine
   *
   * @param args
   *          the args (ignored)
   */
  public static final void main(final String[] args) {
    final boolean[] x = { false, true, false, false, false, true, true,
        false, false, false, false, false, true, true, true, false, true,
        false, false, false, false, };

    System.out.print("Start: "); //$NON-NLS-1$
    Paper_Example.__print(x);
    System.out.println();

    final boolean[] x_after_neutrality = new boolean[10];
    WModel_Boolean.neutrality(x, 2, x_after_neutrality);
    System.out.print("Neutrality: "); //$NON-NLS-1$
    Paper_Example.__print(x_after_neutrality);
    System.out.println();

    final boolean[] x_after_epistasis = new boolean[x_after_neutrality.length];
    WModel_Boolean.epistasis(x_after_neutrality, 4, x_after_epistasis);
    System.out.print("Epistasis: "); //$NON-NLS-1$
    Paper_Example.__print(x_after_epistasis);
    System.out.println();

    final boolean[][] x_after_multi_obj = new boolean[2][6];
    WModel_Boolean.multi_objectivity(x_after_epistasis, x_after_multi_obj);
    System.out.print("Multi-Objectivity: "); //$NON-NLS-1$
    Paper_Example.__print(x_after_multi_obj[0]);
    System.out.print(" / "); //$NON-NLS-1$
    Paper_Example.__print(x_after_multi_obj[1]);
    System.out.println();

    final long[] cases = WModel_TrainingCases.fromString(//
        "*10001", //$NON-NLS-1$
        "0101*0", //$NON-NLS-1$
        "1*0111", //$NON-NLS-1$
        "111*01", //$NON-NLS-1$
        "11*101"//$NON-NLS-1$
    );

    System.out.print("Training: "); //$NON-NLS-1$
    final int f1 = WModel_Boolean.f_training_cases(x_after_multi_obj[0],
        cases);
    final int f2 = WModel_Boolean.f_training_cases(x_after_multi_obj[1],
        cases);
    System.out.print(f1);
    System.out.print(" / "); //$NON-NLS-1$
    System.out.println(f2);

    final int[] ruggedness = WModel_TrainingCases.ruggedness(34, cases);
    System.out.print("Ruggedness: "); //$NON-NLS-1$
    System.out.print(ruggedness[f1]);
    System.out.print(" / "); //$NON-NLS-1$
    System.out.println(ruggedness[f2]);
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
