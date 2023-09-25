package oe.aloha;

import oe.aloha.NetværkController.ClientModule;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class Client {

	public static void start(InputStream in, PrintStream print) {
		print.println("Welcome to the chat! You can write to other people that is connected.");

		ClientModule clientModule = new ClientModule();
		Thread clientThread = new Thread(clientModule);
		clientThread.start();

		print.println("Use exit to exit the chat.");

		Scanner scanner = new Scanner(in);

		while (true) {
			String messageText = scanner.nextLine().trim();

			switch (messageText) {
				case "debug": {
					boolean debugMode = clientModule.toggleDebug();
					System.out.println("Debug mode is now " + (debugMode ? "on" : "off") + ".");
					break;
				}
				case "exit": {
					System.out.println("Exiting Chat...");
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


		System.out.println("Welcome to the chat! You can write to other people that is connected.");
		ClientModule clientModule = new ClientModule();
		Thread clientThread = new Thread(clientModule);
		clientThread.start();

		System.out.println("Use exit to exit the chat.");

		Scanner scanner = new Scanner(System.in);
		while (true) {
			String messageText = scanner.nextLine().trim();

			switch (messageText) {
				case "debug": {
					boolean debugMode = clientModule.toggleDebug();
					System.out.println("Debug mode is now " + (debugMode ? "on" : "off") + ".");
					break;
				}
				case "exit": {
					System.out.println("Exiting Chat...");
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
}
