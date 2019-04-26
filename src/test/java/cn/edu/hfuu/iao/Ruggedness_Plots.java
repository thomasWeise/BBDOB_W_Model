package cn.edu.hfuu.iao;

import java.util.Arrays;

import org.junit.Ignore;

import cn.edu.hfuu.iao.WModel.WModel_Ruggedness;

/**
 * A class printing an R script for visualizing some selected gamma values
 * for n=16.
 */
@Ignore
public final class Ruggedness_Plots {

  /**
   * the main routine
   *
   * @param args
   *          the command line arguments
   */
  public static final void main(final String[] args) {
    final int n = 16;
    final int[] gammas = { 0, 10, 100, 108, 12, 120, 16, 2, 24, 32, 36, 4,
        48, 60, 64, 72, 8, 84, 96 };
    Arrays.sort(gammas);

    System.out.println(("n=" + n) + ';'); //$NON-NLS-1$
    System.out.println("plot(x=c(" + //$NON-NLS-1$
        0 + ",n+1L), y=c(-0.5,1.5+" //$NON-NLS-1$
        + (((gammas.length - 1) * 2))
        + "), type='n', yaxt='n', xlab=NA, ylab=NA, xaxs='i', yaxs='i');"); //$NON-NLS-1$
    System.out.print("axis(side=2, at=(0.5+2L*(0:" + (gammas.length - 1) + //$NON-NLS-1$
        ")), labels=c(");//$NON-NLS-1$
    boolean comma = false;
    for (final int gamma : gammas) {
      if (comma) {
        System.out.print(',');
      } else {
        comma = true;
      }
      System.out.print(gamma);
    }
    System.out.println("), las=1L);");//$NON-NLS-1$

    System.out.println("x<-0:(n+1);");//$NON-NLS-1$

    int i = 0;
    for (final int gamma : gammas) {
      comma = false;

      final int[] map = WModel_Ruggedness.ruggedness(gamma, n);

      System.out.print("y<-" + (2 * i) + //$NON-NLS-1$
          "+(c(");//$NON-NLS-1$

      comma = false;
      for (final int ii : map) {
        if (comma) {
          System.out.print(',');
        } else {
          comma = true;
        }
        System.out.print(ii);
      }
      System.out.println(")/n);");//$NON-NLS-1$

      int last = -1;
      comma = false;
      System.out.print("col<-c(");//$NON-NLS-1$
      for (final int ii : map) {
        if (comma) {
          System.out.print(',');
        } else {
          comma = true;
        }
        System.out.print(ii == (last + 1) ? "'black'" : //$NON-NLS-1$
            "'red'");//$NON-NLS-1$
        last = ii;
      }
      System.out.println(");");//$NON-NLS-1$

      last = -1;
      comma = false;
      System.out.print("lwd<-c(");//$NON-NLS-1$
      for (final int ii : map) {
        if (comma) {
          System.out.print(',');
        } else {
          comma = true;
        }
        System.out.print(ii == (last + 1) ? "1L" : //$NON-NLS-1$
            "2L");//$NON-NLS-1$
        last = ii;
      }
      System.out.println(");");//$NON-NLS-1$

      System.out.println(
          "segments(x0=x, y0=y, x1=(x+1L), y1=y, col=col, lwd=lwd);");//$NON-NLS-1$

      System.out.println(
          "segments(x0=x[2:(n+2)], y0=y[1:(n+1)], x1=x[2:(n+2)], y1=y[2:(n+2)], col=col[2L:(n+2L)], lwd=lwd[2L:(n+2L)]);");//$NON-NLS-1$

      i++;
    }

  }
}
