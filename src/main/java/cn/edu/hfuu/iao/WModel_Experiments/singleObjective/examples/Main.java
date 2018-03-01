package cn.edu.hfuu.iao.WModel_Experiments.singleObjective.examples;

import cn.edu.hfuu.iao.WModel_Experiments.singleObjective.Runner;
import cn.edu.hfuu.iao.WModel_Experiments.singleObjective.examples.ea.EA;
import cn.edu.hfuu.iao.WModel_Experiments.singleObjective.examples.hc.MFlipHillClimber;
import cn.edu.hfuu.iao.WModel_Experiments.singleObjective.examples.hc.MFlipHillClimberWithRestarts;
import cn.edu.hfuu.iao.WModel_Experiments.singleObjective.examples.hc.OneFlipHillClimber;
import cn.edu.hfuu.iao.WModel_Experiments.singleObjective.examples.hc.OneFlipHillClimberWithRestarts;
import cn.edu.hfuu.iao.WModel_Experiments.singleObjective.examples.hc.TwoFlipHillClimber;
import cn.edu.hfuu.iao.WModel_Experiments.singleObjective.examples.hc.TwoFlipHillClimberWithRestarts;

/** Run the examples. */
public class Main {

  /**
   * Run!
   *
   * @param args
   *          the arguments (ignored)
   */
  @SuppressWarnings("unchecked")
  public static final void main(final String[] args) {
    Runner.log("Running the Example Experiments"); //$NON-NLS-1$

    Runner.run(128, //
        true, // wait until all tasks are finished, then compress output
              // folder
        Runner.setup(new OneFlipHillClimber()), //
        Runner.setup(new OneFlipHillClimberWithRestarts()), //
        Runner.setup(new TwoFlipHillClimber()), //
        Runner.setup(new TwoFlipHillClimberWithRestarts()), //
        Runner.setup(new MFlipHillClimber()), //
        Runner.setup(new MFlipHillClimberWithRestarts()), //
        Runner.setup(new EA(16, 32, 0.7d)), //
        Runner.setup(new EA(8, 16, 0.7d)), //
        Runner.setup(new EA(32, 64, 0.7d)), //
        Runner.setup(new EA(16, 32, 0.3d)), //
        Runner.setup(new EA(8, 16, 0.3d)), //
        Runner.setup(new EA(32, 64, 0.3d))//
    );
    Runner.log("done.");//$NON-NLS-1$
  }
}
