package oe.aloha.Entities;

import java.net.Socket;

public class Session {
	private String id;
	private Socket socket;

	public Session(Socket socket) {
		this.socket = socket;
	}

	public String getId() {
		return id;
	}

	public Socket getSocket() {
		return socket;
	}
}
