package cn.edu.hfuu.iao.WModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;

import org.junit.Assert;

/**
 * Here we try to experimentally verify the worst-case complexity of the
 * ruggedness transformation generation process. It should need O(n) steps
 * and it turns out to be roughly need 3n, so that is good.
 */
public class WModel_Ruggedness_Complexity {

  /**
   * Create the ruggedness permutation where groups of rugged
   * transformations alternate with deceptive permutations for increasing
   * gamma.
   *
   * @param gamma
   *          the gamma value
   * @param q
   *          the maximum possible objective value (equivalent to {@code n}
   *          if no training-case based evaluation is applied)
   * @return the number of writes to the ruggedness permutation
   */
  public static long ruggedness_raw(final int gamma, final int q) {
    int j, k, start, max;
    long writes = 0L;

    final int[] r = new int[q + 1];

    max = WModel_Ruggedness.max_gamma(q);// (q * (q - 1)) >>> 1;

    if (gamma <= 0) {
      start = 0;
    } else {
      start = q - 1 - ((int) (0.5d + //
          Math.sqrt(0.25 + ((max - gamma) << 1))));
    }

    k = 0;
    for (j = 1; j <= start; j++) {
      r[j] = ((j & 1) != 0) ? (q - k) : (++k);
      ++writes;
    }
    for (; j <= q; j++) {
      ++k;
      r[j] = ((start & 1) != 0) ? (q - k) : k;
      ++writes;
    }

    final int upper = ((gamma - max)
        + (((q - start - 1) * (q - start)) >>> 1));
    --j; // in the paper, this incorrectly says j=start, while it is j=q=n
    for (int i = 1; i <= upper; i++) {
      final int t = r[--j];
      r[j] = r[q];
      r[q] = t;
      writes += 2L;
    }
    return writes;
  }

  /**
   * the main routine
   *
   * @param args
   *          ignored
   */
  public static final void main(final String[] args) {
    final HashSet<Integer> ns = new HashSet<>();
    final int threshold = 3 * 1024;

    System.out.println("computing different values of n to test"); //$NON-NLS-1$

    // pick important powers of 2
    for (int n = 1; n <= threshold; n *= 2) {
      ns.add(Integer.valueOf(n));
    }
    // pick important powers of 3
    for (int n = 1; n <= threshold; n *= 3) {
      ns.add(Integer.valueOf(n));
    }
    // pick important powers of 5
    for (int n = 1; n <= threshold; n *= 5) {
      ns.add(Integer.valueOf(n));
    }
    // pick important powers of 10
    for (int n = 1; n <= threshold; n *= 10) {
      ns.add(Integer.valueOf(n));
    }

    // pick random numbers
    final Random random = new Random(234885L);
    for (int i = 128; (--i) >= 0;) {
      ns.add(Integer.valueOf(random.nextInt(threshold) + 1));
    }

    // picḱ interesting divisions
    for (int i = 2; i <= 11; i++) {
      for (int j = 1; j < i; j++) {
        ns.add(Integer.valueOf(Math.max(1, (j * threshold) / i)));
        ns.add(Integer.valueOf(Math.max(1, (j * threshold) / i) + 1));
        ns.add(Integer.valueOf(Math.max(1, (j * threshold) / i) - 1));
      }
    }

    // pick all primes in range
    final boolean[] primes = new boolean[threshold + 1];
    Arrays.fill(primes, true);
    for (int i = 2; i < primes.length; i++) {
      if (primes[i]) {
        for (int j = 2; (i * j) < primes.length; j++) {
          primes[i * j] = false;
        }
      }
    }
    for (int i = 2; i < primes.length; i++) {
      if (primes[i]) {
        ns.add(Integer.valueOf(i));
      }
    }

    for (int i = 1; i < 64; i++) {
      ns.add(Integer.valueOf(i));
    }

    System.out.println("found " //$NON-NLS-1$
        + ns.size() + " different values of n: " //$NON-NLS-1$
        + Arrays.toString(
            ns.stream().mapToInt((i) -> i.intValue()).sorted().toArray()));

    final ArrayList<long[]> results = new ArrayList<>();
    final Collection<long[]> synch = Collections
        .synchronizedCollection(results);

    ns.stream().mapToInt((i) -> i.intValue()).parallel().forEach((n) -> {
      final int maxGamma = WModel_Ruggedness.max_gamma(n);

      long maxSteps = -1L;
      int hardestGamma = -1;
      for (int gamma = 0; gamma <= maxGamma; gamma++) {
        final long steps = WModel_Ruggedness_Complexity
            .ruggedness_raw(gamma, n);
        if (steps < n) {
          Assert.fail();
        }
        if (steps > maxSteps) {
          maxSteps = steps;
          hardestGamma = gamma;
        }
      }
      if (hardestGamma < 0) {
        Assert.fail();
      }
      synch.add(new long[] { n, maxGamma, hardestGamma, maxSteps });
    });

    System.out.println();
    System.out.println(
        "n,maxGamma,hardestGamma,numberOfWritesToArray,numberOfWritesToArray/n"); //$NON-NLS-1$

    results.sort((l1, l2) -> Long.compare(l1[0], l2[0]));
    double max = -1d;
    long[] maxL = null;
    for (final long[] l : results) {
      System.out.print(l[0]);
      System.out.print(',');
      System.out.print(l[1]);
      System.out.print(',');
      System.out.print(l[2]);
      System.out.print(',');
      System.out.print(l[3]);
      System.out.print(',');
      final double d = (((double) (l[3])) / ((double) (l[0])));
      System.out.print(d);
      if (d > max) {
        max = d;
        maxL = l;
      }
    }
    System.out.println();
    System.out.println();
    System.out.println();
    System.out.println("max ratio: " + max);//$NON-NLS-1$
    System.out.println(" |- n: " + maxL[0]);//$NON-NLS-1$
    System.out.println(" |- gamma: " + maxL[2]);//$NON-NLS-1$
    System.out.println(" |- numberOfWritesToArray: " + maxL[3]);//$NON-NLS-1$
  }
}
