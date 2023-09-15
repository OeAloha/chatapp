package oe.aloha.NetværkModuleController;

import java.util.ArrayList;
import oe.aloha.Entities.Session;

public class ServerModule {
	private ArrayList<Session> sessions;

	public ServerModule() {
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
