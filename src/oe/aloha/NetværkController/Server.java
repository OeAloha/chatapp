package oe.aloha.NetværkController;

import java.util.ArrayList;
import oe.aloha.Entities.Session;

public class Server {
	private ArrayList<Session> sessions;

	public Server() {
		sessions = new ArrayList<Session>();
	}

	public void addSession(Session session) {
		sessions.add(session);
		session.getSocket();
	}

	public void removeSession(Session session) {
		sessions.remove(session);
	}

}
