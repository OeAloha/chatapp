package oe.aloha.NetværkController;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import oe.aloha.Entities.Packet;
import oe.aloha.Entities.Session;
import oe.aloha.Entities.packet.Disconnect;
import oe.aloha.Entities.packet.Message;
import oe.aloha.Entities.packet.Ping;
import oe.aloha.Entities.packet.Pong;
import oe.aloha.Utils;

public class ServerModule implements Runnable {
	private ServerSocket serverSocket;
	private ArrayList<Session> sessions;
	private Thread pingThread;

	public ServerModule() {
		sessions = new ArrayList<Session>();
	}

	public void run() {
		try {
			serverSocket = new ServerSocket(8080);
			pingThread = new Thread(() -> {
				try {
					while (true) {
						Thread.sleep(10000);
						sendPacket(new Ping(), null);
						for (Session session : sessions) {
							Thread timeoutThread = new Thread(() -> {
								try {
									Thread.sleep(10000);
									removeSession(session);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							});
							session.setTimeoutThread(timeoutThread);
							timeoutThread.start();
						}
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			});
			pingThread.start();
			while (true) {
				Socket socket = serverSocket.accept();
				addSession(socket);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addSession(Socket socket) {
		try {
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			Session session = new Session(Utils.phoneticId(), socket, ois, oos);
			Thread thread = new Thread(() -> {
				try {
					while (true) {
						Packet packet = (Packet) ois.readObject();
						switch (packet.getType()) {
							case MESSAGE: {
								Message message = new Message(session.getId() + ": " + ((Message) packet).getMessage());
								sendPacket(message, session.getId());
								break;
							}
							case DISCONNECT: {
								removeSession(session);
								break;
							}
							case PING: {
								Pong pong = new Pong();
								oos.writeObject(pong);
								break;
							}
							case PONG: {
								session.clearTimeoutThread();
								break;
							}
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			});
			session.setReadThread(thread);
			sessions.add(session);
			session.getReadThread().start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void sendPacket(Packet packet, String senderId) {
		try {
			for (Session session : sessions) {
				if (session.getId().equals(senderId)) {
					continue;
				}
				session.getOos().writeObject(packet);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void removeSession(Session session) {
		try {
			session.getOis().close();
			session.getOos().close();
			session.getSocket().close();
			session.getReadThread().interrupt();
			session.clearTimeoutThread();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			sessions.remove(session);
		}
	}

	public void cleanup() {
		try {
			serverSocket.close();
			pingThread.interrupt();
			sendPacket(new Disconnect(), null);
			for (Session session : sessions) {
				removeSession(session);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int size() {
		return sessions.size();
	}
}
