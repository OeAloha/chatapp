package oe.aloha;

import oe.aloha.NetværkController.ClientModule;

import java.io.IOException;
import java.util.Scanner;

public class Client {
	public static void main(String[] args) {
		ClientModule clientModule = new ClientModule();
		Thread clientThread = new Thread(clientModule);
		clientThread.start();

		Scanner scanner = new Scanner(System.in);
		while (true) {
			String messageText = scanner.nextLine();

			switch (messageText) {
				case "debug": {
					boolean debugMode = clientModule.toggleDebug();
					System.out.println("Debug mode is now " + (debugMode ? "on" : "off") + ".");
					break;
				}
				case "exit": {
					System.out.println("Exiting Client...");
					scanner.close();
					System.exit(0);
					break;
				}
			}

			try {
				clientModule.sendMessage(messageText);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
