package com.wharvex.gos;


import java.util.concurrent.ThreadFactory;

public class SingleThreadFactory implements ThreadFactory {
  String name;
  Thread thread;

  public SingleThreadFactory(String name) {
    this.name = name;
  }

  @Override
  public Thread newThread(Runnable r) {
    if (thread == null) {
      thread = new Thread(r, name);
      thread.setPriority(Thread.MAX_PRIORITY);
    }
    return thread;
  }
}
