package oe.aloha.Entities;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import oe.aloha.Utils;

/**
 * Represents a session with a client. Includes everything needed to shutdown
 * the session.
 */
public class Session {
	private String id;
	private Socket socket;
	private Thread readThread;
	private Thread timeoutThread;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;

	public Session(String id, Socket socket, ObjectInputStream ois, ObjectOutputStream oos) {
		this.id = id;
		this.socket = socket;
		this.ois = ois;
		this.oos = oos;
	}

	public String getId() {
		return id;
	}

	public void setTimeoutThread(Thread timeoutThread) {
		this.timeoutThread = timeoutThread;
	}

	public void clearTimeoutThread(boolean debug, String session) {
		if (debug) {
			if (timeoutThread == null) {
				Utils.safeLog("Timeout not found");
			} else {
				Utils.safeLog("Timeout found for session " + session);
			}
		}
		if (timeoutThread != null) {
			timeoutThread.interrupt();
			timeoutThread = null;
		}
	}

	public void setReadThread(Thread readThread) {
		this.readThread = readThread;
	}

	public Thread getReadThread() {
		return readThread;
	}

	public Socket getSocket() {
		return socket;
	}

	public ObjectInputStream getOis() {
		return ois;
	}

	public ObjectOutputStream getOos() {
		return oos;
	}
}
