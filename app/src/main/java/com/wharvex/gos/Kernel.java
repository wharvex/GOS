package com.wharvex.gos;

public class Kernel implements Runnable, WorkSimulator {
  @Override
  public void run() {
    simulateWork("Loading kernel", 2000);

    simulateWork("Initializing device drivers", 1000);

    simulateWork("Starting system services", 1500);

    simulateWork("Mounting filesystems", 1000);

    simulateWork("System ready", 500);
  }

  @Override
  public void simulateWork(String operation, int duration) {
    GOSLogger.logMain(
        Thread.currentThread().getName() + " is performing: " + operation);
    try {
      Thread.sleep(duration);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      GOSLogger.logMain("bad");
    }
  }
}
