package com.wharvex.gos;

public class Bootloader implements Runnable, WorkSimulator {

  @Override
  public void run() {
    simulateWork("BIOS POST check", 500);

    simulateWork("Loading bootloader", 1000);

    simulateWork("Bootloader initializing hardware", 1500);
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
