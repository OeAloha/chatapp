package test;

import static org.junit.Assert.assertEquals;

import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

public class Client {
  private Scanner fromClient;
  private PrintStream print;
  private PipedOutputStream toClient;

  @Before
  public void setUp() {
    toClient = new PipedOutputStream();
  }
  private void writeStringToStream(String string) {
    try {
      toClient.write(string.getBytes());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  private String readFromInputStream(PipedInputStream stream) {
    
    return stream.toString();
  }
  
  @Test
  public void test() {
    PrintStream print = new PrintStream(new PipedOutputStream());
    assertEquals(1, 1);
  }
}