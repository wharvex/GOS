package com.wharvex.gos;

public class Bootloader implements Runnable {
  public Thread getThread() {
    return thread;
  }

  public void setThread(Thread thread) {
    this.thread = thread;
  }

  private Thread thread;

  @Override
  public void run() {

  }
}
