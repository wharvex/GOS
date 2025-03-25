package com.wharvex.gos;

public class Bootloader implements Runnable, WorkSimulator {

  @Override
  public void run() {
    OSSimulatorGUI.getInstance().simulateWork("BIOS POST check", 500);

    OSSimulatorGUI.getInstance().simulateWork("Loading bootloader", 1000);

    OSSimulatorGUI.getInstance()
        .simulateWork("Bootloader initializing hardware", 1500);
  }

  @Override
  public void simulateWork(String operation, int duration) {
    OSSimulatorGUI.getInstance().logMessage(
        Thread.currentThread().getName() + " is performing: " + operation);
    try {
      Thread.sleep(duration);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      OSSimulatorGUI.getInstance()
          .logMessage("Operation interrupted: " + operation);
    }
  }
}
