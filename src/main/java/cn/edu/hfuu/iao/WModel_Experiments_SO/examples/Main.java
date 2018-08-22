package cn.edu.hfuu.iao.WModel_Experiments_SO.examples;

import java.util.Map;

import cn.edu.hfuu.iao.WModel.WModel_SingleObjective;
import cn.edu.hfuu.iao.WModel_Experiments_SO.Algorithm;
import cn.edu.hfuu.iao.WModel_Experiments_SO.Runner;
import cn.edu.hfuu.iao.WModel_Experiments_SO.examples.base.Exhaustive_Enumerator;
import cn.edu.hfuu.iao.WModel_Experiments_SO.examples.base.Random_Sampling;
import cn.edu.hfuu.iao.WModel_Experiments_SO.examples.ea.EA;
import cn.edu.hfuu.iao.WModel_Experiments_SO.examples.ea.FFA_EA;
import cn.edu.hfuu.iao.WModel_Experiments_SO.examples.hc.MFlipHillClimberWithRestarts;
import cn.edu.hfuu.iao.WModel_Experiments_SO.examples.hc.OneFlipHillClimberWithRestarts;
import cn.edu.hfuu.iao.WModel_Experiments_SO.examples.hc.TwoFlipHillClimberWithRestarts;
import cn.edu.hfuu.iao.utils.ConsoleIO;

/** Run the examples. */
public class Main {

  /** The algorithm setups used. */
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public static final Map.Entry<Algorithm, WModel_SingleObjective.Factory>[] SETUPS = new Map.Entry[] {
      Runner.setup(new Exhaustive_Enumerator()), //
      Runner.setup(new Random_Sampling()), //
      //
      Runner.setup(new OneFlipHillClimberWithRestarts()), //
      Runner.setup(new TwoFlipHillClimberWithRestarts()), //
      Runner.setup(new MFlipHillClimberWithRestarts()), //
      //
      Runner.setup(new EA(8, 16, 0.7d)), //
      Runner.setup(new EA(32, 64, 0.7d)), //
      Runner.setup(new EA(256, 512, 0.7d)), //
      Runner.setup(new EA(8, 16, 0.3d)), //
      Runner.setup(new EA(32, 64, 0.3d)), //
      Runner.setup(new EA(256, 512, 0.3d)), ///
      //
      Runner.setup(new FFA_EA(8, 16, 0.7d)), //
      Runner.setup(new FFA_EA(32, 64, 0.7d)), //
      Runner.setup(new FFA_EA(256, 512, 0.7d)), //
      Runner.setup(new FFA_EA(8, 16, 0.3d)), //
      Runner.setup(new FFA_EA(32, 64, 0.3d)), //
      Runner.setup(new FFA_EA(256, 512, 0.3d)) };

  /**
   * Run!
   *
   * @param args
   *          the arguments (ignored)
   */
  public static final void main(final String[] args) {
    ConsoleIO.stdout("Running the Example Experiments"); //$NON-NLS-1$

    Runner.run(//
        // run experiments until n=100
        100, //
        // true==wait until all tasks are finished, then compress output
        // folder
        true, Main.//
            SETUPS);
    ConsoleIO.stdout("done.");//$NON-NLS-1$
  }
}
