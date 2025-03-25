package com.wharvex.gos;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileOutputStream;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

public class OSSimulatorGUI extends JFrame {
  private final ExecutorService bootLoaderThread;
  private final ExecutorService kernelThread;
  private final ExecutorService userlandProcessThread;

  private final SingleThreadFactory bootloaderThreadFactory;

  private final Logger mainLogger;

  private JTextArea console;
  private JButton bootButton;
  private JButton kernelButton;
  private JButton shutdownButton;
  private JButton launchAppButton;

  private static OSSimulatorGUI instance;

  private OSSimulatorGUI() {
    bootloaderThreadFactory = new SingleThreadFactory("BootLoader");
    bootLoaderThread =
        Executors.newSingleThreadExecutor(bootloaderThreadFactory);

    kernelThread = Executors.newSingleThreadExecutor(r -> {
      Thread t = new Thread(r, "Kernel");
      t.setPriority(Thread.MAX_PRIORITY - 1);
      return t;
    });

    userlandProcessThread = Executors.newSingleThreadExecutor(r -> {
      Thread t = new Thread(r, "UserlandProcess");
      t.setPriority(Thread.NORM_PRIORITY);
      return t;
    });

    initializeGUI();

    mainLogger = Logger.getLogger("Main");
    mainLogger.setUseParentHandlers(false);
    var mainOutputLoggerStreamHandler =
        new StreamHandler(new OutputStreamExt(console),
            new SimpleFormatterExt());
    mainLogger.addHandler(mainOutputLoggerStreamHandler);
  }

  public static OSSimulatorGUI getInstance() {
    if (instance == null) {
      instance = new OSSimulatorGUI();
    }
    return instance;
  }

  private void initializeGUI() {
    setTitle("OS Simulator");
    setSize(600, 400);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout());

    // Create UI components
    console = new JTextArea();
    console.setEditable(false);
    JScrollPane scrollPane = new JScrollPane(console);

    bootButton = new JButton("Boot System");
    kernelButton = new JButton("Launch Kernel");
    shutdownButton = new JButton("Shutdown");
    launchAppButton = new JButton("Launch Application");
    kernelButton.setEnabled(false);
    shutdownButton.setEnabled(false);
    launchAppButton.setEnabled(false);

    // Control panel
    JPanel controlPanel = new JPanel();
    controlPanel.add(bootButton);
    controlPanel.add(kernelButton);
    controlPanel.add(shutdownButton);
    controlPanel.add(launchAppButton);

    add(controlPanel, BorderLayout.NORTH);
    add(scrollPane, BorderLayout.CENTER);

    // Add event listeners
    bootButton.addActionListener(e -> bootSystem());
    kernelButton.addActionListener(e -> launchKernel());
    shutdownButton.addActionListener(e -> shutdownSystem());
    launchAppButton.addActionListener(e -> launchUserApplication());

    // Handle clean shutdown
    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        cleanupThreads();
      }
    });
  }

  private void bootSystem() {
    bootButton.setEnabled(false);
    logMessage("Initiating boot sequence...");
    var bootLoader = new Bootloader();

    // Run boot sequence on bootLoaderThread
    submitTask(bootLoaderThread, bootLoader);

//         -> {
//      // Simulate BIOS/bootloader operations
//      simulateWork("BIOS POST check", 500);
//      updateProgress(10);
//
//      simulateWork("Loading bootloader", 1000);
//      updateProgress(20);
//
//      simulateWork("Bootloader initializing hardware", 1500);
//      updateProgress(30);

    // Hand off to kernel thread
//      submitTask(kernelThread, () -> {
//        // Simulate kernel boot
//        simulateWork("Loading kernel", 2000);
//        updateProgress(40);
//
//        simulateWork("Initializing device drivers", 1000);
//        updateProgress(50);
//
//        simulateWork("Starting system services", 1500);
//        updateProgress(70);
//
//        simulateWork("Mounting filesystems", 1000);
//        updateProgress(90);
//
//        simulateWork("System ready", 500);
//        updateProgress(100);
//
//        // Update UI on EDT
//        SwingUtilities.invokeLater(() -> {
//          logMessage("System booted successfully!");
//          shutdownButton.setEnabled(true);
//          launchAppButton.setEnabled(true);
//        });
//      });
//    });
    kernelButton.setEnabled(true);
  }

  private void shutdownSystem() {
    shutdownButton.setEnabled(false);
    launchAppButton.setEnabled(false);
    logMessage("Initiating shutdown sequence...");

    // Run shutdown on kernelThread
    submitTask(kernelThread, () -> {
      simulateWork("Stopping user processes", 1000);
      simulateWork("Unmounting filesystems", 1000);
      simulateWork("Stopping system services", 1000);
      simulateWork("Powering off", 500);

      // Update UI on EDT
      SwingUtilities.invokeLater(() -> {
        logMessage("System shutdown complete");
        bootButton.setEnabled(true);
      });
    });
  }

  private void launchKernel() {
    kernelButton.setEnabled(false);
    logMessage("Launching Kernel...");

    submitTask(kernelThread, () -> {
      simulateWork("Loading kernel", 2000);

      simulateWork("Initializing device drivers", 1000);

      simulateWork("Starting system services", 1500);

      simulateWork("Mounting filesystems", 1000);

      simulateWork("System ready", 500);

      SwingUtilities.invokeLater(() -> {
        logMessage("System booted successfully!");
        shutdownButton.setEnabled(true);
        launchAppButton.setEnabled(true);
      });
    });
  }

  private void launchUserApplication() {
    logMessage("Launching user application...");

    // Run user application on userlandProcessThread
    submitTask(userlandProcessThread, () -> {
      simulateWork("Loading application", 1000);
      simulateWork("Initializing application", 1000);
      simulateWork("Application running", 500);
    });
  }

  // Helper methods
  private void submitTask(ExecutorService executor, Runnable task) {
    CompletableFuture.runAsync(task, executor)
        .exceptionally(ex -> {
          SwingUtilities.invokeLater(() ->
              logMessage("ERROR: " + ex.getMessage()));
          return null;
        });
  }

  public void simulateWork(String operation, long durationMs) {
    logMessage(
        Thread.currentThread().getName() + " is performing: " + operation);
//    logMessage("Performing: " + operation);
    try {
      Thread.sleep(durationMs); // Simulate work
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      logMessage("Operation interrupted: " + operation);
    }
  }

  public void logMessage(String message) {
    SwingUtilities.invokeLater(() -> {
      console.append(message + "\n");
      console.setCaretPosition(console.getDocument().getLength());
      try (FileOutputStream fileOutputStream = new FileOutputStream(
          "C:\\Users\\tgudl\\gos.log", true)) {
        fileOutputStream.write((message + "\n").getBytes());
      } catch (Exception e) {
        System.err.println("Error writing to file: " + e.getMessage());
      }
    });
  }

  private void logMessage2(String message) {
    mainLogger.log(Level.INFO, message);
  }

  private void cleanupThreads() {
    bootLoaderThread.shutdown();
    kernelThread.shutdown();
    userlandProcessThread.shutdown();
  }
}
