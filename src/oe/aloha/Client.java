package oe.aloha;

import oe.aloha.NetværkController.ClientModule;
import oe.aloha.NetværkController.ServerModule;

import java.io.IOException;
import java.util.Scanner;

public class Client {
	public static void main(String[] args) throws IOException {
		ClientModule clientModule = new ClientModule();
		Thread clientThread = new Thread(clientModule);
		clientThread.start();

		Scanner scanner = new Scanner(System.in); // scanner burde ikke være i controller laget
		String messageText = scanner.nextLine();

		ClientModule cm = new ClientModule();
		cm.sendMessage(messageText);
	}
}