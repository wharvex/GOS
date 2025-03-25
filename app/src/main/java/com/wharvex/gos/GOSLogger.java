package com.wharvex.gos;

import javax.swing.*;
import java.util.logging.Level;

public class GOSLogger {

  public static void logMain(String message) {
    OSSimulatorGUI.getInstance().logMessage(message);
  }

  public static void logDebug(String message) {
    GOSLoggerSource.getInstance().getDebugLogger().log(Level.INFO, message);
  }
}
