package cn.edu.hfuu.iao.utils;

import java.util.function.Consumer;

/**
 * This is a trivial, low-overhead parallel engine working according to the
 * FIFO principle. It allows you to submit a couple of tasks and to run
 * them in parallel. Different from
 * {@link java.util.concurrent.ExecutorService}, it allows you to wait
 * until all tasks are done and then, if necessary, submit new ones.
 */
public final class SimpleParallelExecutor {

  /** the task queue head */
  private static volatile __Task s_queue_head;
  /** the task queue tail */
  private static volatile __Task s_queue_tail;

  /** the number of running tasks */
  private static volatile int s_running;
  /** the queue synchronizer */
  private static final Object SYNCH = new Object();

  /** the available parallelism */
  private static final int PARALLELISM;

  static {
    PARALLELISM = Math.max(1,
        (Runtime.getRuntime().availableProcessors() - 1));
    if (SimpleParallelExecutor.PARALLELISM <= 1) {
      ConsoleIO.stdout(//
          "Setting up for direct execution, no parallel computation."); //$NON-NLS-1$
    } else {
      ConsoleIO.stdout(//
          "Setting up " + //$NON-NLS-1$
              SimpleParallelExecutor.PARALLELISM + " parallel workers."); //$NON-NLS-1$
      for (int i = SimpleParallelExecutor.PARALLELISM; (--i) >= 0;) {
        final Thread thread = new Thread(
            () -> SimpleParallelExecutor.__executeNext());
        thread.setDaemon(true);
        thread.start();
      }
    }
  }

  /**
   * push the given task into the queue
   *
   * @param task
   *          the taks
   */
  private static final void __push(final __Task task) {
    if (SimpleParallelExecutor.s_queue_head == null) {
      SimpleParallelExecutor.s_queue_head = task;
      SimpleParallelExecutor.s_queue_tail = task;
    } else {
      SimpleParallelExecutor.s_queue_tail.m_next = task;
      SimpleParallelExecutor.s_queue_tail = task;
    }
  }

  /**
   * Execute the task
   *
   * @param task
   *          the task
   */
  public static final void execute(final Runnable task) {
    if (SimpleParallelExecutor.PARALLELISM <= 1) {
      task.run();
    } else {
      final __Task t = new __Task(task);
      synchronized (SimpleParallelExecutor.SYNCH) {
        SimpleParallelExecutor.__push(t);
        SimpleParallelExecutor.SYNCH.notifyAll();
      }
    }
  }

  /**
   * get the parallelism
   *
   * @return the degree of parallelism
   */
  public static final int getParallelism() {
    return SimpleParallelExecutor.PARALLELISM;
  }

  /**
   * Execute the task
   *
   * @param tasks
   *          the task
   */
  public static final void execute(final Runnable[] tasks) {
    if (SimpleParallelExecutor.PARALLELISM <= 1) {
      for (final Runnable task : tasks) {
        task.run();
      }
    } else {
      __Task add, next;
      add = next = null;
      for (final Runnable task : tasks) {
        final __Task temp = new __Task(task);
        if (next == null) {
          add = next = temp;
        } else {
          next.m_next = temp;
          next = temp;
        }
      }

      synchronized (SimpleParallelExecutor.SYNCH) {
        SimpleParallelExecutor.__push(add);
        SimpleParallelExecutor.SYNCH.notifyAll();
      }
    }
  }

  /**
   * Execute multiple tasks: The task factory's {@code accept} method will
   * be called exactly once, providing it with a consumer for new tasks. It
   * can then call this consumer's {@code accept} method arbitrarily often,
   * each time providing a new task. The advantage of this method over
   * {@link #execute(Runnable)} is that it will create the tasks all in a
   * single synchronized block, right after which the task processing
   * begins. This should reduce overhead of synchronization.
   *
   * @param taskFactory
   *          the task factory
   */
  public static final void executeMultiple(
      final Consumer<Consumer<Runnable>> taskFactory) {
    if (SimpleParallelExecutor.PARALLELISM <= 1) {
      taskFactory.accept((task) -> task.run());
    } else {
      synchronized (SimpleParallelExecutor.SYNCH) {
        taskFactory.accept(
            (task) -> SimpleParallelExecutor.__push(new __Task(task)));
        SimpleParallelExecutor.SYNCH.notifyAll();
      }
    }
  }

  /** wait for all tasks to finish */
  public static final void waitForAll() {
    if (SimpleParallelExecutor.PARALLELISM <= 1) {
      return;
    }
    int interruptedCount = 0;
    wait: for (;;) {
      try {
        synchronized (SimpleParallelExecutor.SYNCH) {
          if (SimpleParallelExecutor.s_queue_head == null) {
            if (SimpleParallelExecutor.s_running <= 0) {
              break wait;
            }
          }
          SimpleParallelExecutor.SYNCH.wait();
        }
      } catch (final InterruptedException ie) {
        // we can ignore this, but if it happens too often, let's crash to
        // be on the safe side
        if ((++interruptedCount) > 10000) {
          throw new Error((//
          "Waiting for tasks has been interrupted "//$NON-NLS-1$
              + interruptedCount + //
              " times. There may be something wrong, we better escalate this as an Error."), //$NON-NLS-1$
              ie);
        }
      }
    }
  }

  /** execute the next task */
  private static final void __executeNext() {
    __Task next;
    boolean justFinished;

    justFinished = false;
    try {
      for (;;) {
        synchronized (SimpleParallelExecutor.SYNCH) {
          next = SimpleParallelExecutor.s_queue_head;
          if (next != null) {
            final __Task set = next.m_next;
            SimpleParallelExecutor.s_queue_head = set;
            if (set == null) {
              SimpleParallelExecutor.s_queue_tail = null;
            }
            if (!justFinished) {
              ++SimpleParallelExecutor.s_running;
              justFinished = true;
            }
          } else {
            if (justFinished) {
              justFinished = false;
              if ((--SimpleParallelExecutor.s_running) <= 0) {
                SimpleParallelExecutor.SYNCH.notifyAll();
                continue;
              }
            }
            SimpleParallelExecutor.SYNCH.wait();
            continue;
          }
        }
        next.m_task.run();
      }
    } catch (final InterruptedException ie) {
      ie.printStackTrace();
    }
  }

  /** the task class */
  private static final class __Task {
    /** the next individual task */
    __Task m_next;
    /** the task */
    final Runnable m_task;

    /**
     * create
     *
     * @param task
     *          the task
     */
    __Task(final Runnable task) {
      super();
      this.m_task = task;
    }
  }
}
