package com.wharvex.gos;

public class UserlandProcess implements Runnable, WorkSimulator {
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

  @Override
  public void run() {
    simulateWork("Loading application", 1000);
    simulateWork("Initializing application", 1000);
    simulateWork("Application running", 500);
  }
}
