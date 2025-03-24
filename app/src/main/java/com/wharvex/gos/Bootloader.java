package com.wharvex.gos;

public class Bootloader implements Runnable {

  @Override
  public void run() {
    OSSimulatorGUI.getInstance().simulateWork("BIOS POST check", 500);

    OSSimulatorGUI.getInstance().simulateWork("Loading bootloader", 1000);

    OSSimulatorGUI.getInstance()
        .simulateWork("Bootloader initializing hardware", 1500);
  }
}
