package oe.aloha.Entities;

import java.net.Socket;

public class Session {
	private String id;
	private Socket socket;
	private Thread thread;

	public Session(Socket socket, Thread thread) {
		this.socket = socket;
		this.thread = thread;
	}

	public String getId() {
		return id;
	}

	public Thread getThread() {
		return thread;
	}

	public Socket getSocket() {
		return socket;
	}
}
