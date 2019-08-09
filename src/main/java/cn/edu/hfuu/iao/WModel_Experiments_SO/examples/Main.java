package cn.edu.hfuu.iao.WModel_Experiments_SO.examples;

import java.util.Map;

import cn.edu.hfuu.iao.WModel.WModel_SingleObjective;
import cn.edu.hfuu.iao.WModel_Experiments_SO.Algorithm;
import cn.edu.hfuu.iao.WModel_Experiments_SO.Runner;
import cn.edu.hfuu.iao.WModel_Experiments_SO.examples.base.Exhaustive_Enumerator;
import cn.edu.hfuu.iao.WModel_Experiments_SO.examples.base.Random_Sampling;
import cn.edu.hfuu.iao.WModel_Experiments_SO.examples.ea.EA;
import cn.edu.hfuu.iao.WModel_Experiments_SO.examples.ea.EA_with_Standard_Mutation;
import cn.edu.hfuu.iao.WModel_Experiments_SO.examples.ea.FFA_EA;
import cn.edu.hfuu.iao.WModel_Experiments_SO.examples.ea.FFA_EA_with_Standard_Mutation;
import cn.edu.hfuu.iao.WModel_Experiments_SO.examples.hc.MFlipHillClimber;
import cn.edu.hfuu.iao.WModel_Experiments_SO.examples.hc.MFlipHillClimberWithRestarts;
import cn.edu.hfuu.iao.WModel_Experiments_SO.examples.hc.OneFlipHillClimber;
import cn.edu.hfuu.iao.WModel_Experiments_SO.examples.hc.OneFlipHillClimberWithRestarts;
import cn.edu.hfuu.iao.WModel_Experiments_SO.examples.hc.StandardFlipHillClimber;
import cn.edu.hfuu.iao.WModel_Experiments_SO.examples.hc.StandardFlipHillClimberWithRestarts;
import cn.edu.hfuu.iao.WModel_Experiments_SO.examples.hc.TwoFlipHillClimber;
import cn.edu.hfuu.iao.WModel_Experiments_SO.examples.hc.TwoFlipHillClimberWithRestarts;
import cn.edu.hfuu.iao.WModel_Experiments_SO.examples.sa.OneFlipSimulatedAnnealingLog;
import cn.edu.hfuu.iao.WModel_Experiments_SO.examples.sa.StandardFlipSimulatedAnnealingLog;
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
      Runner.setup(new StandardFlipHillClimberWithRestarts()), //
      //
      Runner.setup(new OneFlipHillClimber()), //
      Runner.setup(new TwoFlipHillClimber()), //
      Runner.setup(new MFlipHillClimber()), //
      Runner.setup(new StandardFlipHillClimber()), //
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
      Runner.setup(new FFA_EA(256, 512, 0.3d)),
      //
      Runner.setup(new EA_with_Standard_Mutation(8, 16, 0.7d)), //
      Runner.setup(new EA_with_Standard_Mutation(32, 64, 0.7d)), //
      Runner.setup(new EA_with_Standard_Mutation(256, 512, 0.7d)), //
      Runner.setup(new EA_with_Standard_Mutation(8, 16, 0.3d)), //
      Runner.setup(new EA_with_Standard_Mutation(32, 64, 0.3d)), //
      Runner.setup(new EA_with_Standard_Mutation(256, 512, 0.3d)), ///
      //
      Runner.setup(new FFA_EA_with_Standard_Mutation(8, 16, 0.7d)), //
      Runner.setup(new FFA_EA_with_Standard_Mutation(32, 64, 0.7d)), //
      Runner.setup(new FFA_EA_with_Standard_Mutation(256, 512, 0.7d)), //
      Runner.setup(new FFA_EA_with_Standard_Mutation(8, 16, 0.3d)), //
      Runner.setup(new FFA_EA_with_Standard_Mutation(32, 64, 0.3d)), //
      Runner.setup(new FFA_EA_with_Standard_Mutation(256, 512, 0.3d)), //
      //
      Runner.setup(new OneFlipSimulatedAnnealingLog(2)), //
      Runner.setup(new OneFlipSimulatedAnnealingLog(5)), //
      Runner.setup(new OneFlipSimulatedAnnealingLog(10)), //
      Runner.setup(new OneFlipSimulatedAnnealingLog(20)), //
      //
      Runner.setup(new StandardFlipSimulatedAnnealingLog(2)), //
      Runner.setup(new StandardFlipSimulatedAnnealingLog(5)), //
      Runner.setup(new StandardFlipSimulatedAnnealingLog(10)), //
      Runner.setup(new StandardFlipSimulatedAnnealingLog(20)) //
  };

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
