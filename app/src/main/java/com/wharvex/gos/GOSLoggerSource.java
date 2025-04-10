package com.wharvex.gos;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

// Singleton
public class GOSLoggerSource {
  private final Logger mainLogger;
  private final Logger debugLogger;
  private final String logFilePath;

  private static GOSLoggerSource instance = null;

  private GOSLoggerSource() {
    logFilePath = "C:\\Users\\tgudl\\gos.log";
    mainLogger = Logger.getLogger("Main");
    debugLogger = Logger.getLogger("Debug");

    try {
      var fileHandler = new FileHandler(logFilePath);
      fileHandler.setFormatter(new XMLFormatterExt());
      mainLogger.addHandler(fileHandler);
      debugLogger.addHandler(fileHandler);
    } catch (IOException e) {
      System.out.println("Failed to create log files -- exiting");
      System.exit(-1);
    }
  }

  public static GOSLoggerSource getInstance() {
    if (instance == null) {
      instance = new GOSLoggerSource();
    }
    return instance;
  }

  public Logger getMainLogger() {
    return mainLogger;
  }

  public Logger getDebugLogger() {
    return debugLogger;
  }
}
