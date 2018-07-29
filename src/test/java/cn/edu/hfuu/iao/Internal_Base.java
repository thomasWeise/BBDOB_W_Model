package cn.edu.hfuu.iao;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

import org.junit.Assert;
import org.junit.Ignore;

/** An internal base class for testing */
@Ignore
public abstract class Internal_Base {

  /** should we use fast tests? */
  public static final boolean FAST_TESTS;

  static {
    boolean fast = false;
    try {
      fast = Boolean.parseBoolean(System.getenv("FASTTESTS")); //$NON-NLS-1$
    } catch (@SuppressWarnings("unused") final Throwable ignore) {
      //
    }
    FAST_TESTS = fast;
    if (Internal_Base.FAST_TESTS) {
      System.out.println("Fast test execution was chosen.");//$NON-NLS-1$
    }
  }

  /**
   * get the value of the bit in {@code x} at index {@code index}
   *
   * @param x
   *          the bit
   * @param index
   *          the index
   * @return the value
   */
  protected static final boolean bit(final long x, final int index) {
    return ((x & (1L << index)) != 0L);
  }

  /**
   * transform a boolean value to a char
   *
   * @param value
   *          the value
   * @return the bit
   */
  protected static final char bit(final boolean value) {
    return (value ? '1' : '0');
  }

  /**
   * transform a char value to a boolean value
   *
   * @param value
   *          the value
   * @return the bit
   */
  protected static final boolean bit(final char value) {
    return (value == '1');
  }

  /**
   * get the bit at the given index of the optimum
   *
   * @param index
   *          the index
   * @return the bit
   */
  protected static final boolean optimum(final int index) {
    return ((index & 1) != 0);
  }

  /**
   * Exhaustively iterate over all values of bit strings of length n.
   *
   * @param n
   *          the number of bits
   * @param consumer
   *          the consumer for character arrays of 0 and 1 of length n
   */
  protected static final void exhaustive_iteration(final int n,
      final Consumer<char[]> consumer) {
    final char[] bits = new char[n];

    if (n >= 63) {
      Assert.fail("n=" + n); //$NON-NLS-1$
    }
    for (long i = (1L << n); (--i) >= 0L;) {
      for (int j = n; (--j) >= 0;) {
        bits[j] = Internal_Base.bit(Internal_Base.bit(i, j));
      }
      consumer.accept(bits);
    }
  }

  /**
   * get a random string
   *
   * @param n
   *          the length
   * @param random
   *          the random number generator
   * @return the string
   */
  protected static final String random(final int n,
      final ThreadLocalRandom random) {
    final char[] text = new char[n];
    for (int i = text.length; (--i) >= 0;) {
      text[i] = Internal_Base.bit(random.nextBoolean());
    }
    return String.valueOf(text);
  }
}
