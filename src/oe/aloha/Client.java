package oe.aloha;

import oe.aloha.NetværkController.ClientModule;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class Client {

	public static void start(InputStream in, PrintStream printer) {
		printer.println("Welcome to the chat! You can write to other people that is connected.");

		ClientModule clientModule = new ClientModule(printer);
		Thread clientThread = new Thread(clientModule);
		clientThread.start();

		printer.println("Use exit to exit the chat.");

		Scanner scanner = new Scanner(in);

		while (true) {
			String messageText = scanner.nextLine().trim();

			switch (messageText) {
				case "debug": {
					boolean debugMode = clientModule.toggleDebug();
					printer.println("Debug mode is now " + (debugMode ? "on" : "off") + ".");
					break;
				}
				case "exit": {
					printer.println("Exiting Chat...");
					scanner.close();
					System.exit(0);
					break;
				}
				default: {
					if (!messageText.isEmpty()) {
						try {
							clientModule.sendMessage(messageText);
						} catch (IOException e) {
							throw new RuntimeException(e);
						}
					}
				}
			}
		}
	}


	public static void main(String[] args) {
		start(System.in, System.out);
	}
}
