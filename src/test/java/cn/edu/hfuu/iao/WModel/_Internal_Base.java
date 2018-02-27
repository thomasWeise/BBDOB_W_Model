package cn.edu.hfuu.iao.WModel;

import java.util.function.Consumer;

import org.junit.Ignore;

/** An internal base class for testing */
@Ignore
abstract class _Internal_Base {

  /** should we use fast tests? */
  public static final boolean FAST_TESTS;

  static {
    boolean fast = false;
    try {
      fast = Boolean.parseBoolean(System.getenv("fast-tests")); //$NON-NLS-1$
    } catch (@SuppressWarnings("unused") final Throwable ignore) {
      //
    }
    FAST_TESTS = fast;
    if (_Internal_Base.FAST_TESTS) {
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
  static final boolean _bit(final int x, final int index) {
    return ((x & (1 << index)) != 0);
  }

  /**
   * transform a boolean value to a char
   *
   * @param value
   *          the value
   * @return the bit
   */
  static final char _bit(final boolean value) {
    return (value ? '1' : '0');
  }

  /**
   * transform a char value to a boolean value
   *
   * @param value
   *          the value
   * @return the bit
   */
  static final boolean _bit(final char value) {
    return (value == '1');
  }

  /**
   * get the bit at the given index of the optimum
   *
   * @param index
   *          the index
   * @return the bit
   */
  static final boolean _optimum(final int index) {
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
  static final void _exhaustive_iteration(final int n,
      final Consumer<char[]> consumer) {
    final char[] bits = new char[n];
    for (int i = (1 << n); (--i) >= 0;) {
      for (int j = n; (--j) >= 0;) {
        bits[j] = _Internal_Base._bit(_Internal_Base._bit(i, j));
      }
      consumer.accept(bits);
    }
  }
}
