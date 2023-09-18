package oe.aloha;

import java.util.Scanner;
import oe.aloha.NetværkController.ServerModule;

public class Server {
	public static void main(String[] args) {
		System.out.println("Welcome to ChatApp Server v0.1!");
		ServerModule serverModule = new ServerModule();
		Thread serverThread = new Thread(serverModule);
		serverThread.start();
		System.out.println(
				"Server has now started! Use num to get the number of connected clients, and exit to exit the server.");
		Scanner scanner = new Scanner(System.in);
		while (true) {
			System.out.print("> ");
			String input = scanner.nextLine();
			switch (input) {
				case "num": {
					System.out.println("There are " + serverModule.size() + " connected clients.");
					break;
				}
				case "debug": {
					boolean debugMode = serverModule.toggleDebug();
					System.out.println("Debug mode is now " + (debugMode ? "on" : "off") + ".");
					break;
				}
				case "exit": {
					System.out.println("Exiting server...");
					serverThread.interrupt();
					serverModule.cleanup();
					scanner.close();
					System.exit(0);
					break;
				}
				default: {
					System.out.println("Unknown command.");
					break;
				}
			}
		}
	}
}