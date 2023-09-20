package oe.aloha.NetværkController;

import oe.aloha.Entities.Packet;
import oe.aloha.Entities.packet.Message;
import oe.aloha.Entities.packet.Pong;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

/**
 * Handles network communications for the Client. Runs in separate thread to
 * allow ?
 */
public class ClientModule implements Runnable {
	private Socket socket;
	private ObjectInputStream input;
	private ObjectOutputStream output;

	private boolean debug;

	public ClientModule() {

	}

	@Override
	public void run() {
		try {
			socket = new Socket("localhost", 8080);
			output = new ObjectOutputStream(socket.getOutputStream());
			input = new ObjectInputStream(socket.getInputStream());

			receiveMessage();

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void sendMessage(String messageText) throws IOException {
		Message message = new Message(messageText);
		output.writeObject(message);
	}

	public void receiveMessage() {
		try {
			while (true) {
				Packet packet = (Packet) input.readObject();

				if(debug){
					System.out.println(packet.getType());
				}

				switch (packet.getType()) {
					case MESSAGE:
						System.out.println(((Message) packet).getMessage());
						break;
					case DISCONNECT:
						break;
					case PING:
						Pong pong = new Pong();
						output.writeObject(pong);
						break;
					case PONG:
						break;
				}

			}
		} catch (SocketException e) {
			System.out.println("Server is shutting down");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	public boolean toggleDebug() {
		debug = !debug;
		return debug;
	}
}
