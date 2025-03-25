package com.wharvex.gos;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileOutputStream;
import java.time.Instant;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OSSimulatorGUI extends JFrame {
  private final ExecutorService bootLoaderThread;
  private final ExecutorService kernelThread;
  private final ExecutorService userlandProcessThread;

  private final SingleThreadFactory bootloaderThreadFactory;
  private final SingleThreadFactory kernelThreadFactory;
  private final SingleThreadFactory userlandProcessThreadFactory;

  private JTextArea console;
  private JButton bootButton;
  private JButton kernelButton;
  private JButton launchAppButton;

  private static OSSimulatorGUI instance;

  private OSSimulatorGUI() {
    logMessage(Instant.now().toString(), true);
    bootloaderThreadFactory = new SingleThreadFactory("BootLoader");
    kernelThreadFactory = new SingleThreadFactory("Kernel");
    userlandProcessThreadFactory =
        new SingleThreadFactory("UserlandProcess");

    bootLoaderThread =
        Executors.newSingleThreadExecutor(bootloaderThreadFactory);

    kernelThread = Executors.newSingleThreadExecutor(kernelThreadFactory);

    userlandProcessThread = Executors.newSingleThreadExecutor(
        userlandProcessThreadFactory);

    initializeGUI();
  }

  public static OSSimulatorGUI getInstance() {
    if (instance == null) {
      instance = new OSSimulatorGUI();
    }
    return instance;
  }

  private void initializeGUI() {
    // Configure frame.
    setTitle("OS Simulator");
    setSize(600, 400);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout());

    // Create/configure components.
    console = new JTextArea();
    console.setEditable(false);
    JScrollPane scrollPane = new JScrollPane(console);
    bootButton = new JButton("Boot System");
    kernelButton = new JButton("Launch Kernel");
    launchAppButton = new JButton("Launch Application");
    kernelButton.setEnabled(false);
    launchAppButton.setEnabled(false);

    // Create/configure control panel.
    JPanel controlPanel = new JPanel();
    controlPanel.add(bootButton);
    controlPanel.add(kernelButton);
    controlPanel.add(launchAppButton);
    add(controlPanel, BorderLayout.NORTH);
    add(scrollPane, BorderLayout.CENTER);

    // Add event listeners.
    bootButton.addActionListener(e -> bootSystem());
    kernelButton.addActionListener(e -> launchKernel());
    launchAppButton.addActionListener(e -> launchUserApplication());

    // Handle clean shutdown.
    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        cleanupThreads();
      }
    });
  }

  private void bootSystem() {
    bootButton.setEnabled(false);
    submitTask(bootLoaderThread, new Bootloader());
    kernelButton.setEnabled(true);
  }

  private void launchKernel() {
    kernelButton.setEnabled(false);
    submitTask(kernelThread, new Kernel());
    launchAppButton.setEnabled(true);
  }

  private void launchUserApplication() {
    launchAppButton.setEnabled(false);
    submitTask(userlandProcessThread, new UserlandProcess());
    launchAppButton.setEnabled(true);
  }

  private void submitTask(ExecutorService executor, Runnable task) {
    CompletableFuture.runAsync(task, executor)
        .exceptionally(ex -> {
          SwingUtilities.invokeLater(() -> GOSLogger.logMain("bad"));
          return null;
        });
  }

  public void logMessage(String message, boolean isMain) {
    SwingUtilities.invokeLater(() -> {
      if (isMain) {
        console.append(message + "\n");
        console.setCaretPosition(console.getDocument().getLength());
      }
      try (FileOutputStream fileOutputStream = new FileOutputStream(
          "C:\\Users\\tgudl\\gos.log", true)) {
        fileOutputStream.write((message + "\n").getBytes());
      } catch (Exception e) {
        System.err.println("Error writing to file: " + e.getMessage());
      }
    });
  }

  private void cleanupThreads() {
    bootLoaderThread.shutdown();
    kernelThread.shutdown();
    userlandProcessThread.shutdown();
  }
}
