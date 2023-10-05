package test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

import oe.aloha.Client;

public class ClientTest {
  private PrintStream toClient;
  private Scanner fromClient;

  @Before
  public void setUp() throws IOException {
    PipedInputStream toClientInput = new PipedInputStream();
    PipedOutputStream toClientOutput = new PipedOutputStream(toClientInput);
    toClient = new PrintStream(toClientOutput);
    PipedInputStream fromClientInput = new PipedInputStream();
    fromClient = new Scanner(fromClientInput);
    PipedOutputStream toTest = new PipedOutputStream(fromClientInput);
    PrintStream printer = new PrintStream(toTest);
    Client.start(toClientInput, printer);
  }
  
  @Test
  public void test() {
    toClient.println("Hello World!");
    assertEquals(1, 1);
  }
}