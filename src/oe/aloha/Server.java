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
			String input = scanner.nextLine();
			if (input.equals("num")) {
				System.out.println("There are " + serverModule.size() + " connected clients.");
			} else if (input.equals("exit")) {
				System.out.println("Exiting server...");
				serverThread.interrupt();
				serverModule.cleanup();
				scanner.close();
				System.exit(0);
			} else {
				System.out.println("Unknown command.");
			}
			System.out.print("> ");
		}
	}
}