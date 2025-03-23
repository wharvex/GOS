package com.wharvex.gos;

import java.util.concurrent.ThreadFactory;

public class BootloaderThreadFactory implements ThreadFactory {
  @Override
  public Thread newThread(Runnable r) {
    Thread t = new Thread(r, "BootLoader");
    ((Bootloader) r).setThread(t);
    return t;
  }
}
