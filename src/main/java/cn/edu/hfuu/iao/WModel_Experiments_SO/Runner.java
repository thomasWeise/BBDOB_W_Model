package cn.edu.hfuu.iao.WModel_Experiments_SO;

import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.IntFunction;
import java.util.function.IntUnaryOperator;
import java.util.function.Supplier;

import cn.edu.hfuu.iao.WModel.WModel_Ruggedness;
import cn.edu.hfuu.iao.WModel.WModel_SingleObjective;
import cn.edu.hfuu.iao.WModel.WModel_SingleObjective_Boolean;
import cn.edu.hfuu.iao.utils.Compressor;
import cn.edu.hfuu.iao.utils.ConsoleIO;
import cn.edu.hfuu.iao.utils.IOUtils;
import cn.edu.hfuu.iao.utils.SimpleParallelExecutor;

/**
 * This class can be used to execute parallel replicable experiments with
 * the W-Model and logs the results to text files and a folder structure.
 * Logging includes all improvements that an algorithm makes and takes
 * place in-memory, thus not messing up the time measurements. We also log
 * the system setup and the random seed of each setup, as well as the
 * algorithm configuration and problem features. Up to {@code (max(1, p-1)}
 * runs can take place in a parallel on a machine with {@code p} virtual
 * processors. One processor is left unused in the hope that it can take
 * care of system things which would otherwise mess up the time
 * measurements.
 */
public final class Runner {

  /** the wrapped function thread local */
  private static final ThreadLocal<__Wrapped> WRAPPERS = //
      ThreadLocal.withInitial(() -> new __Wrapped());

  /**
   * the random number generator thread local (we cannot use
   * {@link java.util.concurrent.ThreadLocalRandom} because we need to set
   * the seed)
   */
  private static final ThreadLocal<Random> RANDOM = //
      ThreadLocal.withInitial(() -> new Random());

  /**
   * perform one run of the algorithm
   *
   * @param <T>
   *          the representation
   * @param algorithm
   *          the algorithm
   * @param f
   *          the objective function
   * @param seed
   *          the random number seed
   * @param logFile
   *          the log file
   */
  @SuppressWarnings("unchecked")
  public static final <T> void run(final Algorithm<T> algorithm,
      final WModel_SingleObjective<T> f, final long seed,
      final Path logFile) {

    final Path useFile = IOUtils.canonicalize(logFile);

    try (final BufferedWriter writer = Files.newBufferedWriter(useFile)) {

      final long startTime = System.currentTimeMillis();

      writer.write("# Experiment with the W-Model"); //$NON-NLS-1$
      writer.newLine();
      writer.write("# http://github.com/thomasWeise/BBDOB_W_Model"); //$NON-NLS-1$
      writer.newLine();

      writer.write("# path: ");//$NON-NLS-1$
      writer.write(useFile.toString());
      writer.newLine();

      writer.write("# start time: ");//$NON-NLS-1$
      writer.write(Long.toString(startTime));
      writer.write(' ');
      writer.write(new Date(startTime).toString());
      writer.newLine();

      writer.write("# PROBLEM_INFO");//$NON-NLS-1$
      writer.newLine();
      writer.write("# candidate size: ");//$NON-NLS-1$
      writer.write(Integer.toString(f.get_candidate_solution_length()));
      writer.newLine();

      writer.write("# n: ");//$NON-NLS-1$
      final int n = f.get_n();
      writer.write(Integer.toString(n));
      writer.newLine();
      writer.write("# mu: ");//$NON-NLS-1$
      writer.write(Integer.toString(f.get_mu()));
      writer.newLine();
      writer.write("# nu: ");//$NON-NLS-1$
      final int nu = f.get_nu();
      writer.write(Integer.toString(nu));
      writer.newLine();
      writer.write("# nu_relative: ");//$NON-NLS-1$
      writer.write(Double.toString((nu - 2) / ((double) (n - 2))));
      writer.newLine();
      writer.write("# gamma: ");//$NON-NLS-1$
      final int gamma = f.get_gamma();
      writer.write(Integer.toString(gamma));
      writer.newLine();
      writer.write("# gamma_relative: ");//$NON-NLS-1$
      writer.write(Double.toString(gamma ///
          / ((double) (WModel_Ruggedness.max_gamma(n)))));
      writer.newLine();

      final __Wrapped wrapped = Runner.WRAPPERS.get();
      wrapped._setup(f);

      writer.write("# objective: "); //$NON-NLS-1$
      writer.write(f.getClass().toString());
      writer.newLine();
      writer.write("# maxFEs: "); //$NON-NLS-1$
      writer.write(Long.toString(wrapped.m_maxFEs));
      writer.newLine();

      writer.write('#');
      writer.newLine();
      writer.write("# ALGORITHM_INFO");//$NON-NLS-1$
      writer.newLine();
      writer.write("# algorithm: "); //$NON-NLS-1$
      writer.write(algorithm.toString());
      writer.newLine();
      writer.write("# algorithm short: "); //$NON-NLS-1$
      writer.write(algorithm.folderName());
      writer.newLine();
      writer.write("# random seed: 0x"); //$NON-NLS-1$
      writer.write(Long.toHexString(seed));
      writer.write('L');
      writer.newLine();
      writer.write("# algorithm_class: "); //$NON-NLS-1$
      writer.write(algorithm.getClass().toString());
      writer.newLine();
      try {
        algorithm.printDescription(writer);

        writer.write('#');
        writer.newLine();
        writer.write("# SYSTEM_INFO");//$NON-NLS-1$
        writer.newLine();
        writer.write("# cpu cores: ");//$NON-NLS-1$
        writer.write(
            Integer.toString(Runtime.getRuntime().availableProcessors()));
        writer.newLine();
        writer.write("# executing thread: ");//$NON-NLS-1$
        writer.write(Long.toString(Thread.currentThread().getId()));
        writer.newLine();
        writer.write("# total memory: ");//$NON-NLS-1$
        writer.write(Long.toString(Runtime.getRuntime().totalMemory()));
        writer.newLine();
        writer.write("# free memory: ");//$NON-NLS-1$
        writer.write(Long.toString(Runtime.getRuntime().freeMemory()));
        writer.newLine();
        writer.write("# java version: ");//$NON-NLS-1$
        writer.write(System.getProperty("java.version"));//$NON-NLS-1$
        writer.newLine();
        writer.write("# java vendor: ");//$NON-NLS-1$
        writer.write(System.getProperty("java.vendor"));//$NON-NLS-1$
        writer.newLine();
        writer.write("# operating system architecture: ");//$NON-NLS-1$
        writer.write(System.getProperty("os.arch"));//$NON-NLS-1$
        writer.newLine();
        writer.write("# operating system name: ");//$NON-NLS-1$
        writer.write(System.getProperty("os.name"));//$NON-NLS-1$
        writer.newLine();
        writer.write("# operating system version: ");//$NON-NLS-1$
        writer.write(System.getProperty("os.version"));//$NON-NLS-1$
        writer.newLine();

        writer.write('#');
        writer.newLine();
        writer.write("# RESULTS");//$NON-NLS-1$
        writer.newLine();
        writer.write("# Format: FEs consumedTimeMS f f/n");//$NON-NLS-1$
        writer.newLine();

        writer.flush();

        final Random random = Runner.RANDOM.get();
        random.setSeed(seed);
        algorithm.solve(wrapped, random);

        int i = wrapped.m_logSize;

        if ((wrapped.m_fes > 0L) && ((i <= 0)
            || (wrapped.m_log[i - 1].m_fes != wrapped.m_fes))) {
          // add an end point
          final __Wrapped._LogPoint p = wrapped.m_log[i++];
          p.m_fes = wrapped.m_fes;
          p.m_value = wrapped.m_best;
          p.m_time = System.currentTimeMillis();
        }

        if (i > 0) {
          final double div = n;
          for (final __Wrapped._LogPoint p : wrapped.m_log) {
            writer.write(Long.toString(p.m_fes));
            writer.write('\t');
            writer.write(Long.toString(p.m_time - startTime));
            writer.write('\t');
            writer.write(Integer.toString(p.m_value));
            writer.write('\t');
            writer.write(Double.toString(p.m_value / div));
            writer.newLine();
            if ((--i) <= 0) {
              break;
            }
          }
        }
      } catch (final Throwable error) {
        try {
          writer.write('#');
          writer.newLine();
          writer.write("# EXCEPTION");//$NON-NLS-1$
          writer.newLine();
          writer.write(
              "# An exception was caught, which will cause abnormal termination of the experiment.");//$NON-NLS-1$
          writer.newLine();
          writer.write("# We will first print the stack trace.");//$NON-NLS-1$
          writer.newLine();
          writer.flush();
          try (final PrintWriter pw = new PrintWriter(writer) {
            /** {@inheritDoc} */
            @Override
            public final void close() {
              super.flush();
            }
          }) {
            error.printStackTrace(pw);
          }
          writer.flush();
        } finally {
          ConsoleIO.stderr("An error occured while performing run " + //$NON-NLS-1$
              logFile, error);
        }
      }

      writer.write('#');
      writer.newLine();
      writer.write("# SUMMARY: ");//$NON-NLS-1$
      writer.write((wrapped.m_best <= 0) ? "success" : //$NON-NLS-1$
          "failure");//$NON-NLS-1$
      writer.newLine();
      writer.write("# best result: ");//$NON-NLS-1$
      writer.write(Integer.toString(wrapped.m_best));
      writer.newLine();

      writer.write("# consumed FEs: "); //$NON-NLS-1$
      writer.write(Long.toString(wrapped.m_fes));
      writer.newLine();

      final long endTime = System.currentTimeMillis();
      writer.write("# consumed time: "); //$NON-NLS-1$
      writer.write(Long.toString(endTime - startTime));
      writer.newLine();

      writer.write("# end time: ");//$NON-NLS-1$
      writer.write(Long.toString(endTime));
      writer.write(' ');
      writer.write(new Date(endTime).toString());
      writer.newLine();

      writer.write("# end of experiment"); //$NON-NLS-1$
      writer.newLine();

      writer.flush();
    } catch (final Throwable error) {
      ConsoleIO.stderr("An internal error occured while performing run " + //$NON-NLS-1$
          logFile, error);
    }
  }

  /**
   * Run the given algorithm on the specified problem. The files names will
   * be created based on the setup and the random seed. If a file for a
   * given setup and seed already exists, we skip doing the run.
   *
   * @param <T>
   *          the representation
   * @param algorithm
   *          the algorithm
   * @param factory
   *          the objective factory
   * @param n
   *          the n
   * @param mu
   *          the mu
   * @param nu
   *          the nu
   * @param gamma
   *          the gamma
   * @param seed
   *          the random seed
   * @param baseFolder
   *          the base folder
   */
  public static final <T> void run(
      final WModel_SingleObjective.Factory<T> factory,
      final Algorithm<T> algorithm, final int n, final int mu,
      final int nu, final int gamma, final long seed,
      final Path baseFolder) {
    try {
      final Path base = IOUtils.makeDirs(baseFolder);
      final String algoName = algorithm.folderName().replace('.', '_')
          .replace('/', '_').replace('\\', '_');
      final Path algo = IOUtils.makeDirs(base.resolve(algoName));

      String name = "n=" + n; //$NON-NLS-1$
      String folder = name;

      name += "_mu=" + mu; //$NON-NLS-1$
      folder = folder + '/' + name;

      name += "_nu=" + nu; //$NON-NLS-1$
      folder = folder + '/' + name;

      name += "_gamma=" + gamma; //$NON-NLS-1$
      folder = folder + '/' + name;

      final Path objective = IOUtils.makeDirs(algo.resolve(folder));

      Path file = objective.resolve(algoName + '_' + name + "_seed="//$NON-NLS-1$
          + Long.toString(seed, Character.MAX_RADIX) + ".txt");//$NON-NLS-1$

      try {
        file = Files.createFile(file);
        if (file == null) {
          return;
        }
      } catch (@SuppressWarnings("unused") final Throwable error) {
        return;
      }

      Runner.run(algorithm, factory.create(n, mu, nu, gamma), seed, file);

    } catch (final Throwable error) {
      error.printStackTrace();
    }
  }

  /**
   * Apply the given algorithms to the default instances of the problems
   *
   * @param setups
   *          the list of algorithm/factory combinations
   * @param maxN
   *          the maximum for n
   * @param baseFolder
   *          the base folder
   */
  public static final void run(
      final Supplier<Spliterator<Map.Entry<Algorithm<?>, //
          WModel_SingleObjective.Factory<?>>>> setups, //
      final int maxN, final Path baseFolder) {
    Runner.run(setups, Runner.default_n(maxN), Runner.default_mu(),
        Runner.default_nu(), Runner.default_gamma(), baseFolder,
        Runner.default_runs());
  }

  /**
   * Run the given algorithm on the default problems
   *
   * @param maxN
   *          the maximum for n
   * @param baseFolder
   *          the base folder
   * @param setups
   *          a list of algorithm/objective function factory combinations
   */
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public static final void run(final int maxN, final Path baseFolder,
      final Map.Entry<Algorithm, //
          WModel_SingleObjective.Factory>... setups) {
    Runner.run(() -> Spliterators.spliterator(setups, 0), maxN,
        baseFolder);
  }

  /**
   * Run the given algorithm on the default problems and log to the
   * "./output" directory.
   *
   * @param maxN
   *          the maximum for n
   * @param postprocess
   *          if this is {@code true}, the method blocks until all tasks
   *          are finished and then attempts to create a tar.xz archive
   *          with all the log files.
   * @param setups
   *          a list of algorithm/objective function factory combinations
   */
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public static final void run(final int maxN, final boolean postprocess,
      final Map.Entry<Algorithm, //
          WModel_SingleObjective.Factory>... setups) {
    Path output = null;
    try {
      output = IOUtils.makeDirs(Paths.get("output")); //$NON-NLS-1$
    } catch (final Throwable error) {
      ConsoleIO.stderr("Error while creating the base output directory.", //$NON-NLS-1$
          error);
      return;
    }
    Runner.run(maxN, output, setups);
    if (postprocess) {
      SimpleParallelExecutor.waitForAll();
      Compressor.compress(output);
    }
  }

  /**
   * Create an algorithm setup from an algorithm and objective function
   * factory
   *
   * @param <T>
   *          the representation type
   * @param algorithm
   *          the algorithm
   * @param factory
   *          the objective function factory
   * @return the setup entry
   */
  @SuppressWarnings("rawtypes")
  public static final <T> Map.Entry<Algorithm, //
      WModel_SingleObjective.Factory> setup(final Algorithm<T> algorithm,
          final WModel_SingleObjective.Factory<T> factory) {
    return new AbstractMap.SimpleImmutableEntry<>(algorithm, factory);
  }

  /**
   * Create an algorithm setup from an algorithm using the
   * {@code boolean[]}-representation
   *
   * @param algorithm
   *          the algorithm
   * @return the setup entry
   */
  @SuppressWarnings("rawtypes")
  public static final Map.Entry<Algorithm, //
      WModel_SingleObjective.Factory> setup(
          final Algorithm<boolean[]> algorithm) {
    return Runner.setup(algorithm,
        WModel_SingleObjective_Boolean.factory());
  }

  /**
   * Run the given algorithm on the specified problem
   *
   * @param setups
   *          the list of algorithm/factory combinations
   * @param ns
   *          the n
   * @param mus
   *          the mu values
   * @param nus
   *          the nu values
   * @param gammas
   *          the gammas
   * @param baseFolder
   *          the base folder
   * @param runs
   *          the number of runs
   */
  public static final void run(
      final Supplier<Spliterator<Map.Entry<Algorithm<?>, WModel_SingleObjective.Factory<?>>>> setups, //
      final Supplier<Spliterator.OfInt> ns, //
      final IntFunction<Spliterator.OfInt> mus, //
      final IntFunction<Spliterator.OfInt> nus, //
      final IntFunction<Spliterator.OfInt> gammas, //
      final Path baseFolder, final int runs) {
    SimpleParallelExecutor.execute(() -> {
      Runner.__run(setups, //
          ns.get(), //
          mus, //
          nus, //
          gammas, //
          baseFolder, runs);
    });
  }

  /**
   * Run the given algorithm on the specified problem
   *
   * @param problems
   *          the list of algorithm/factor combinations
   * @param ns
   *          the n
   * @param mus
   *          the mu values
   * @param nus
   *          the nu values
   * @param gammas
   *          the gammas
   * @param baseFolder
   *          the base folder
   * @param runs
   *          the number of runs
   */
  private static final void __run(
      final Supplier<Spliterator<Map.Entry<Algorithm<?>, WModel_SingleObjective.Factory<?>>>> problems, //
      final Spliterator.OfInt ns, //
      final IntFunction<Spliterator.OfInt> mus, //
      final IntFunction<Spliterator.OfInt> nus, //
      final IntFunction<Spliterator.OfInt> gammas, //
      final Path baseFolder, final int runs) {
    ns.tryAdvance((final int n) -> {
      Runner.__run(problems, n, mus, nus, gammas, baseFolder, runs);
      Runner.__wait_run(problems, ns, mus, nus, gammas, baseFolder, runs,
          6);
    });
  }

  /**
   * Run the given algorithm on the specified problem
   *
   * @param setups
   *          the list of algorithm/factory combinations
   * @param n
   *          the n
   * @param mus
   *          the mu values
   * @param nus
   *          the nu values
   * @param gammas
   *          the gammas
   * @param baseFolder
   *          the base folder
   * @param runs
   *          the number of runs
   */
  @SuppressWarnings({ "unchecked", "rawtypes" })
  private static final void __run(
      final Supplier<Spliterator<Map.Entry<Algorithm<?>, WModel_SingleObjective.Factory<?>>>> setups, //
      final int n, //
      final IntFunction<Spliterator.OfInt> mus, //
      final IntFunction<Spliterator.OfInt> nus, //
      final IntFunction<Spliterator.OfInt> gammas, //
      final Path baseFolder, final int runs) {

    ConsoleIO.stdout("enqueing jobs for n=" + n); //$NON-NLS-1$

    SimpleParallelExecutor.executeMultiple((consumer) -> setups.get()
        .forEachRemaining((pair) -> consumer.accept(() -> Runner.__run(
            ((WModel_SingleObjective.Factory) (pair.getValue())), //
            ((Algorithm) (pair.getKey())), //
            n, mus, nus, gammas, baseFolder, runs))));
  }

  /**
   * create a spliterator for a sorted {@code int} array
   *
   * @param array
   *          the array
   * @return the spliterator
   */
  private static final Spliterator.OfInt __spliterator(final int[] array) {
    return Spliterators.spliterator(array,
        Spliterator.ORDERED | Spliterator.IMMUTABLE | Spliterator.SORTED);
  }

  /**
   * create an array for a collection and clear the collection
   *
   * @param list
   *          the collection
   * @return the array
   */
  private static final int[] __toArray(final Collection<Integer> list) {
    final int[] array = new int[list.size()];
    int i = (-1);
    for (final Integer k : list) {
      array[++i] = k.intValue();
    }
    list.clear();
    Arrays.sort(array);
    return array;
  }

  /**
   * get a supplier for the default n values
   *
   * @param max
   *          the maximum allowed n value
   * @return the supplier
   */
  public static final Supplier<Spliterator.OfInt> default_n(
      final int max) {
    HashSet<Integer> list = new HashSet<>();
    for (int i = 16; ((i >= 16) && (i < max)); i <<= 1) {
      list.add(Integer.valueOf(i));
    }
    for (int i = 10; ((i >= 10) && (i < max)); i *= 10) {
      list.add(Integer.valueOf(i));
    }
    list.add(Integer.valueOf(max));
    for (int i = max; i >= 100; i >>= 1) {
      list.add(Integer.valueOf(i));
    }

    final int[] array = Runner.__toArray(list);
    list = null;
    return () -> Runner.__spliterator(array);
  }

  /** the default m values */
  private static final int[] DEFAULT_MU = { 1, 2, 3, 4, };

  /** the default m spliterator */
  private static final IntFunction<Spliterator.OfInt> DEFAULT_MU_F = (
      final int n) -> Runner.__spliterator(Runner.DEFAULT_MU);

  /**
   * get a supplier for the default mu values
   *
   * @return the supplier
   */
  public static final IntFunction<Spliterator.OfInt> default_mu() {
    return Runner.DEFAULT_MU_F;
  }

  /** the default nu factors */
  private static final double[] DEFAULT_FACTORS = { 0.1, 0.2, 0.3, 0.4,
      0.5, 0.6, 0.7, 0.8, 0.9 };

  /**
   * get a supplier for the default min/max values for a given n
   *
   * @param min
   *          the minimum
   * @param max
   *          the maximum
   * @return the supplier
   */
  private static final Spliterator.OfInt __default(final int min,
      final int max) {
    return Runner.__default(min, max, (i) -> i);
  }

  /**
   * get a supplier for the default min/max values for a given n
   *
   * @param min
   *          the minimum
   * @param max
   *          the maximum
   * @param fixer
   *          the fixer
   * @return the supplier
   */
  private static final Spliterator.OfInt __default(final int min,
      final int max, final IntUnaryOperator fixer) {
    HashSet<Integer> list = new HashSet<>();

    int fixed = fixer.applyAsInt(min);
    if ((fixed >= min) && (fixed <= max)) {
      list.add(Integer.valueOf(fixed));
    }
    fixed = fixer.applyAsInt(max);
    if ((fixed >= min) && (fixed <= max)) {
      list.add(Integer.valueOf(fixed));
    }

    for (final double factor : Runner.DEFAULT_FACTORS) {
      final double res = min + (factor * (max - min));
      fixed = fixer.applyAsInt(((int) (Math.round(res))));
      if ((fixed >= min) && (fixed <= max)) {
        list.add(Integer.valueOf(fixed));
      }
    }

    for (int i = 10; ((i >= 10) && (i < max)); i *= 10) {
      fixed = fixer.applyAsInt(i);
      if ((fixed >= min) && (fixed <= max)) {
        list.add(Integer.valueOf(fixed));
      }
    }
    for (int i = 2; ((i >= 2) && (i < max)); i *= 2) {
      fixed = fixer.applyAsInt(i);
      if ((fixed >= min) && (fixed <= max)) {
        list.add(Integer.valueOf(fixed));
      }
    }

    final int[] array = Runner.__toArray(list);
    list = null;
    return Runner.__spliterator(array);
  }

  /**
   * get a supplier for the default nu values for a given n
   *
   * @return the supplier
   */
  public static final IntFunction<Spliterator.OfInt> default_nu() {
    return (final int n) -> Runner.__default(2, n, (i) -> {
      // Epistasis mappings where nu has the form 2+4v behave odd.
      // Hence, we try to avoid them
      if (i > 2) {
        final int k = (i - 2) / 4;
        if (((k * 4) + 2) == i) {
          if (i < n) {
            return i + 1;
          }
          if (i > 2) {
            return i - 1;
          }
        }
      }
      return i;
    });
  }

  /**
   * get a supplier for the default nu values for a given n
   *
   * @return the supplier
   */
  public static final IntFunction<Spliterator.OfInt> default_gamma() {
    return (final int n) -> Runner.__default(0,
        WModel_Ruggedness.max_gamma(n));
  }

  /**
   * the default number of runs
   *
   * @return the default number of runs
   */
  public static final int default_runs() {
    return 100;
  }

  /**
   * Run the given algorithm on the specified problem
   *
   * @param <T>
   *          the representation
   * @param algorithm
   *          the algorithm
   * @param factory
   *          the objective factory
   * @param n
   *          the n
   * @param mus
   *          the mu values
   * @param nus
   *          the nu values
   * @param gammas
   *          the gammas
   * @param baseFolder
   *          the base folder
   * @param runs
   *          the number of runs
   */
  private static final <T> void __run(
      final WModel_SingleObjective.Factory<T> factory, //
      final Algorithm<T> algorithm, //
      final int n, //
      final IntFunction<Spliterator.OfInt> mus, //
      final IntFunction<Spliterator.OfInt> nus, //
      final IntFunction<Spliterator.OfInt> gammas, //
      final Path baseFolder, final int runs) {

    ConsoleIO.stdout("enqueing jobs for n=" + n //$NON-NLS-1$
        + " and algorithm=" + algorithm.toString());//$NON-NLS-1$

    SimpleParallelExecutor.executeMultiple(
        (consumer) -> mus.apply(n).forEachRemaining((final int mu) -> //
        consumer.accept(() -> //
        Runner.__run(factory, algorithm, n, mu, nus, gammas, baseFolder, runs))));
  }

  /**
   * Run the given algorithm on the specified problem
   *
   * @param <T>
   *          the representation
   * @param algorithm
   *          the algorithm
   * @param factory
   *          the objective factory
   * @param n
   *          the n
   * @param mu
   *          the mu value
   * @param nus
   *          the nu values
   * @param gammas
   *          the gammas
   * @param baseFolder
   *          the base folder
   * @param runs
   *          the number of runs
   */
  private static final <T> void __run(
      final WModel_SingleObjective.Factory<T> factory, //
      final Algorithm<T> algorithm, //
      final int n, final int mu, //
      final IntFunction<Spliterator.OfInt> nus, //
      final IntFunction<Spliterator.OfInt> gammas, //
      final Path baseFolder, final int runs) {
    SimpleParallelExecutor.executeMultiple((consumer) -> //
    nus.apply(n).forEachRemaining((final int nu) -> //
    consumer.accept(() -> Runner.__run(factory, algorithm, n, mu, nu,
        gammas, baseFolder, runs))));
  }

  /**
   * Run the given algorithm on the specified problem
   *
   * @param <T>
   *          the representation
   * @param algorithm
   *          the algorithm
   * @param factory
   *          the objective factory
   * @param n
   *          the n
   * @param mu
   *          the mu value
   * @param nu
   *          the nu value
   * @param gammas
   *          the gammas
   * @param baseFolder
   *          the base folder
   * @param runs
   *          the number of runs
   */
  private static final <T> void __run(
      final WModel_SingleObjective.Factory<T> factory, //
      final Algorithm<T> algorithm, //
      final int n, final int mu, final int nu, //
      final IntFunction<Spliterator.OfInt> gammas, //
      final Path baseFolder, final int runs) {
    SimpleParallelExecutor.executeMultiple((consumer) -> //
    gammas.apply(n).forEachRemaining((final int gamma) -> //
    consumer.accept(() -> Runner.__run(factory, algorithm, n, mu, nu,
        gamma, baseFolder, runs))));
  }

  /**
   * Run the given algorithm on the specified problem
   *
   * @param <T>
   *          the representation
   * @param algorithm
   *          the algorithm
   * @param factory
   *          the objective factory
   * @param n
   *          the n
   * @param mu
   *          the mu value
   * @param nu
   *          the nu value
   * @param gamma
   *          the gamma
   * @param baseFolder
   *          the base folder
   * @param runs
   *          the number of runs
   */
  private static final <T> void __run(
      final WModel_SingleObjective.Factory<T> factory, //
      final Algorithm<T> algorithm, //
      final int n, final int mu, final int nu, final int gamma,
      final Path baseFolder, final int runs) {

    // We try to create unique seeds for each run.
    // We therefore first generate a hopefully unique key from the
    // parameters of the function by involving big enough constants to
    // cover the full 64 bit of long.
    final long baseHash = ((n + 1L) + (2387589247823488619L * //
        ((mu + 1L) + (3135784609L * //
            ((nu + 1L) + (645623846134681L * //
                (gamma + 1L)))))));

    // In order to ensure that all seeds are unique, we combine the bash
    // key with the run index.
    // We then check for collisions and keep changing keys that collide.
    final long[] seeds = new long[runs];
    for (int run = runs; (--run) >= 0;) {
      long nextSeed = (baseHash + (2387589247823488563L * (run + 1L)));
      outerCheck: for (;;) {
        innerCheck: {
          // We use the ">=" instead of ">" in order to also avoid 0L
          // seeds, if they should occur.
          for (int check = runs; (--check) >= run;) {
            // collision?
            if (seeds[check] == nextSeed) {
              break innerCheck;
            }
          }
          break outerCheck;
        }
        // Move the seed using a run-index depending number in order to
        // avoid repetition.
        nextSeed += Math.max((run + 1L), 31L * Long.hashCode(nextSeed));
      }
      seeds[run] = nextSeed;
    }

    // Now we can enqueue one job for every run.
    SimpleParallelExecutor.executeMultiple((consumer) -> {
      for (int run = runs; (--run) >= 0;) {
        final long seed = seeds[run];
        consumer.accept(() -> Runner.run(factory, algorithm, n, mu, nu,
            gamma, seed, baseFolder));
      }
    });
  }

  /**
   * The idea is that we submit the tasks to the executor's queue. First,
   * one which pulls an n value and submits the task for the next pulling,
   * which should wait until the previous n value has been completed. Then
   * each such task is expanded to one task per algorithm.
   *
   * @param problems
   *          the list of algorithm/factor combinations
   * @param ns
   *          the n
   * @param mus
   *          the mu values
   * @param nus
   *          the nu values
   * @param gammas
   *          the gammas
   * @param baseFolder
   *          the base folder
   * @param runs
   *          the number of runs
   * @param wait
   *          the number of times we have to wait and re-submit
   */
  private static final void __wait_run(
      final Supplier<Spliterator<Map.Entry<Algorithm<?>, WModel_SingleObjective.Factory<?>>>> problems, //
      final Spliterator.OfInt ns, //
      final IntFunction<Spliterator.OfInt> mus, //
      final IntFunction<Spliterator.OfInt> nus, //
      final IntFunction<Spliterator.OfInt> gammas, //
      final Path baseFolder, final int runs, final int wait) {

    // finally, enqueue a task for going to the next n
    if (wait > 0) {
      SimpleParallelExecutor.execute(() -> {
        Runner.__wait_run(problems, ns, mus, nus, gammas, baseFolder, runs,
            wait - 1);
      });
    } else {
      Runner.__run(problems, ns, mus, nus, gammas, baseFolder, runs);
    }
  }

  /**
   * The internal wrapped objective function logs the algorithm progress in
   * memory.
   */
  @SuppressWarnings("rawtypes")
  private static final class __Wrapped extends WModel_SingleObjective {
    /** the wrapped objective function */
    private WModel_SingleObjective m_wrapped;

    /** the log */
    _LogPoint[] m_log;
    /** the log size */
    int m_logSize;
    /** the best-so-far solution */
    int m_best;
    /** the consumed FEs */
    long m_fes;
    /** the maximum FEs */
    long m_maxFEs;

    /** create the logging evaluator */
    __Wrapped() {
      super();

      this.m_log = new _LogPoint[100];
      for (int i = this.m_log.length; (--i) >= 0;) {
        this.m_log[i] = new _LogPoint();
      }
    }

    /**
     * wrap a given single objective function
     *
     * @param wrap
     *          the function to wrap
     */
    final void _setup(final WModel_SingleObjective wrap) {
      final int n = wrap.get_n() + 2;
      final int oldLength = this.m_log.length;
      if (oldLength < n) {
        final _LogPoint[] log = new _LogPoint[Math.max(n, n << 1)];
        System.arraycopy(this.m_log, 0, log, 0, oldLength);
        this.m_log = log;
        for (int i = log.length; (--i) >= oldLength;) {
          log[i] = new _LogPoint();
        }
      }

      this.m_best = Integer.MAX_VALUE;
      this.m_wrapped = wrap;
      this.m_logSize = 0;
      this.m_fes = 0L;
      final int cl = this.get_candidate_solution_length();
      this.m_maxFEs = Math.max(1_000_000L, cl * (cl * 200L));
    }

    /** {@inheritDoc} */
    @Override
    public final int get_n() {
      return this.m_wrapped.get_n();
    }

    /** {@inheritDoc} */
    @Override
    public final int get_mu() {
      return this.m_wrapped.get_mu();
    }

    /** {@inheritDoc} */
    @Override
    public final int get_nu() {
      return this.m_wrapped.get_nu();
    }

    /** {@inheritDoc} */
    @Override
    public final int get_gamma() {
      return this.m_wrapped.get_gamma();
    }

    /** {@inheritDoc} */
    @Override
    public final int get_candidate_solution_length() {
      return this.m_wrapped.get_candidate_solution_length();
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    @Override
    public final int applyAsInt(final Object value) {
      final int res = this.m_wrapped.applyAsInt(value);
      ++this.m_fes;
      if (res < this.m_best) {
        this.m_best = res;
        final _LogPoint p = this.m_log[this.m_logSize++];
        p.m_fes = this.m_fes;
        p.m_time = System.currentTimeMillis();
        p.m_value = res;
      }
      return res;
    }

    /** {@inheritDoc} */
    @Override
    public final boolean shouldTerminate() {
      return ((this.m_best <= 0) || (this.m_fes >= this.m_maxFEs));
    }

    /** a log point */
    static final class _LogPoint {
      /** the consumed fes */
      long m_fes;
      /** the current time */
      long m_time;
      /** the achieved objective value */
      int m_value;

      /** create a log point */
      _LogPoint() {
        super();
      }
    }
  }
}
