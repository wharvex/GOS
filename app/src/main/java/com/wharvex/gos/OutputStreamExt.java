package com.wharvex.gos;

import javax.swing.*;
import java.io.IOException;
import java.io.OutputStream;

public class OutputStreamExt extends OutputStream {
  private final JTextArea textArea;

  public OutputStreamExt(JTextArea textArea) {
    this.textArea = textArea;
  }

  @Override
  public void write(int b) throws IOException {
    textArea.append((char) b + "\n");
    textArea.setCaretPosition(textArea.getDocument().getLength());
  }
}
