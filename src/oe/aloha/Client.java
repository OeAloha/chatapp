package oe.aloha;

import oe.aloha.Entities.Packet;
import oe.aloha.Entities.packet.Message;
import oe.aloha.Entities.packet.Pong;
import oe.aloha.NetværkController.ClientModule;

import java.io.IOException;
import java.util.Scanner;

public class Client {
	public static void main(String[] args) {
		ClientModule clientModule = new ClientModule();
		Thread clientThread = new Thread(clientModule);
		clientThread.start();

		while(true){
			Scanner scanner = new Scanner(System.in);
			String messageText = scanner.nextLine();

			try {
				clientModule.sendMessage(messageText);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
}

