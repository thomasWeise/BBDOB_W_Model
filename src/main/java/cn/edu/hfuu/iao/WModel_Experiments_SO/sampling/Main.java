package cn.edu.hfuu.iao.WModel_Experiments_SO.sampling;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import cn.edu.hfuu.iao.WModel.WModel_Ruggedness;
import cn.edu.hfuu.iao.WModel.WModel_SingleObjective;
import cn.edu.hfuu.iao.WModel_Experiments_SO.Algorithm;
import cn.edu.hfuu.iao.WModel_Experiments_SO.Runner;
import cn.edu.hfuu.iao.WModel_Experiments_SO.Runner.WrappedObjectiveFunction;
import cn.edu.hfuu.iao.utils.ConsoleIO;
import cn.edu.hfuu.iao.utils.IOUtils;
import cn.edu.hfuu.iao.utils.SimpleParallelExecutor;

/** Run the examples. */
public class Main {

  /** The algorithm setups used. */
  @SuppressWarnings({ "rawtypes" })
  public static final Map.Entry<Algorithm, WModel_SingleObjective.Factory>[] SETUPS = //
      cn.edu.hfuu.iao.WModel_Experiments_SO.examples.Main.SETUPS;

  /**
   * Run!
   *
   * @param args
   *          the arguments (ignored)
   */
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public static final void main(final String[] args) {
    Runner.setWrapper(() -> new __WrappedObjectiveFunction());
    ConsoleIO.stdout(
        "Running Selected Example Experiments and Collecting All Results"); //$NON-NLS-1$

    final int maxRuns = 10;
    final int[] n = new int[] { 10, 32, 75, 100 };
    final double[] nu1 = new double[] { 0, 0.5, 1 };
    final double[] gamma1 = new double[] { 0, 0.25, 0.5, 0.75, 1 };
    final int[] mu = new int[] { 1, 3 };

    Path baseFolder = null;
    try {
      baseFolder = IOUtils.makeDirs(Paths.get("outputFull")); //$NON-NLS-1$
    } catch (final Throwable error) {
      ConsoleIO.stderr("Error while creating the base output directory.", //$NON-NLS-1$
          error);
      return;
    }

    // we make it final so that we can use it in the experiments
    final Path output = baseFolder;

    for (final int _n : n) {
      final int maxgamma = WModel_Ruggedness.max_gamma(_n);
      for (final double _nu1 : nu1) {
        final int _nu = Math.max(2,
            Math.min(_n, ((int) (Math.round(2 + ((_n - 2) * _nu1))))));
        for (final double _gamma1 : gamma1) {
          final int _gamma = Math.max(0, Math.min(maxgamma,
              ((int) (Math.round(_gamma1 * maxgamma)))));
          for (final int _mu : mu) {
            final long[] seeds = Runner.createSeeds(_n, _mu, _nu, _gamma,
                maxRuns);
            for (int _run = maxRuns; (--_run) >= 0;) {
              final long _seed = seeds[_run];
              SimpleParallelExecutor.executeMultiple((consumer) -> {
                for (final Map.Entry<Algorithm, WModel_SingleObjective.Factory> setup : Main.SETUPS) {

                  consumer.accept(() -> {
                    Runner.run(setup.getValue(), setup.getKey(), _n, _mu,
                        _nu, _gamma, _seed, output);
                  });
                }
              });
            }
          }
        }
      }
    }

    SimpleParallelExecutor.waitForAll();
    ConsoleIO.stdout("done.");//$NON-NLS-1$
  }

  /**
   * The internal wrapped objective function logs the algorithm progress in
   * memory.
   */
  private static final class __WrappedObjectiveFunction
      extends WrappedObjectiveFunction {
    /** the hex chars */
    private static final char[] HEX_CHARS = { '0', '1', '2', '3', '4', '5',
        '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
    /** the log */
    private final char[] m_log;
    /** the buffer index */
    private int m_index;
    /** we did not solve */
    private boolean m_solved;

    /** create the logging evaluator */
    __WrappedObjectiveFunction() {
      super();
      this.m_log = new char[9 * Runner.MAX_FES];
    }

    /** {@inheritDoc} */
    @Override
    protected void setup(final WModel_SingleObjective wrap,
        final BufferedWriter writer, final long startTime) {
      this.m_index = 0;
      this.m_solved = false;
      super.setup(wrap, writer, startTime);
    }

    /** {@inheritDoc} */
    @Override
    public final int applyAsInt(final Object value) {
      final int res = this.m_wrapped.applyAsInt(value);
      // we directly store the text in our buffer
      if ((++this.m_fes) <= Runner.MAX_FES) {
        if (this.m_index > 0) {
          this.m_log[this.m_index++] = '\n';
        }
        boolean print = false;
        for (int shift = 28; shift >= 0; shift -= 4) {
          final int ch = ((res >>> shift) & 0xf);
          if (print || (ch != 0)) {
            print = true;
            this.m_log[this.m_index++] = __WrappedObjectiveFunction.HEX_CHARS[ch];
          }
        }
        if (!(print)) {
          this.m_log[this.m_index++] = '0';
          this.m_solved = true;
        }
      }
      return res;
    }

    /**
     * Flush the results of the experiment to the log
     *
     * @throws IOException
     *           if i/o fails
     */
    @Override
    protected final void flush() throws IOException {

      if (this.m_fes > 0) {
        this.m_writer.write(
            "# Format: all results in hexadecimal notation, one number per line, each line = 1 FE");//$NON-NLS-1$
        this.m_writer.newLine();
        this.m_writer.write(this.m_log, 0, this.m_index);
        this.m_writer.newLine();
      }
      this.m_writer.write("# SUMMARY: ");//$NON-NLS-1$
      this.m_writer.write(this.m_solved ? "success" : //$NON-NLS-1$
          "failure");//$NON-NLS-1$
      this.m_writer.newLine();

      this.m_writer.write("# consumed FEs: "); //$NON-NLS-1$
      this.m_writer.write(Long.toString(this.m_fes));
      this.m_writer.newLine();
      if (this.m_fes > Runner.MAX_FES) {
        this.m_writer.write(//
            "# The algorithm performed more than the permitted maximum FEs. All results above the limit have been ignored."); //$NON-NLS-1$
        this.m_writer.newLine();
      }
      super.flush();
    }
  }
}
