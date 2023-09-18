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

/**
 * Handles network communications for the Server. Runs in separate thread to
 * allow console control.
 */
public class ServerModule implements Runnable {
	/**
	 * The ServerSocket used to accept new connections.
	 */
	private ServerSocket serverSocket;
	/**
	 * The list of active sessions.
	 */
	private ArrayList<Session> sessions;
	/**
	 * The thread used to send pings to clients. Used to automatically clean out
	 * inactive sessions/non-compliant clients.
	 */
	private Thread pingThread;

	public ServerModule() {
		sessions = new ArrayList<Session>();
	}

	/**
	 * Starts the server. Implements Runnable's run method.
	 */
	@Override
	public void run() {
		try {
			serverSocket = new ServerSocket(8080);
			// Start ping thread. Sends pings to clients every 10 seconds, then cleans out
			// sessions that do not respond.
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

	/**
	 * Adds a new session to the list of active sessions.
	 * Creates input/output streams, and a separate thread to receive packets.
	 * This ensures that the main server thread isn't blocked by waiting for
	 * packets.
	 * 
	 * @param socket The socket used to communicate with the client.
	 */
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
								// If the packet is a message, send it to all clients, with a prefix displaying
								// the user's id.
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

	/**
	 * Sends a packet to all clients, except the one specified by senderId.
	 * 
	 * @param packet   The packet to send.
	 * @param senderId The id of the client that sent the packet.
	 */
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

	/**
	 * Removes a session from the list of active sessions.
	 * 
	 * @param session The session to remove.
	 */
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

	/**
	 * Closes the server socket, and cleans up all sessions.
	 */
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

	/**
	 * Returns the number of active sessions.
	 * 
	 * @return The number of active sessions.
	 */
	public int size() {
		return sessions.size();
	}
}
