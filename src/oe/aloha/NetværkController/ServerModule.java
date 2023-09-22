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
	/**
	 * Whether or not to print debug messages.
	 */
	private boolean debug;

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
		} catch (IOException e) {
		}
		// Start ping thread. Sends pings to clients every 10 seconds, then cleans out
		// sessions that do not respond.
		pingThread = new Thread(() -> {
			while (true) {
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
				}
				Utils.safeLog("Sent PING to all clients.");
				for (Session session : sessions) {
					Thread timeoutThread = new Thread(() -> {
						try {
							Thread.sleep(10000);
						} catch (InterruptedException e) {
							return;
						}
						if (Thread.interrupted()) {
							return;
						}
						if (debug) {
							Utils.safeLog("Session " + session.getId() + " timed out, and is " + (Thread.interrupted() ? "" : "not ")
									+ "interrupted.");
						}
						removeSession(session);
					});
					Utils.safeLog("Set TimeoutThread for session " + session.getId() + ".");
					session.setTimeoutThread(timeoutThread);
					timeoutThread.start();
				}
				sendPacket(new Ping(), null);
			}

		});
		pingThread.start();
		while (true) {
			try {
				Socket socket = serverSocket.accept();
				addSession(socket);
			} catch (IOException e) {
			}
		}
	}

	public boolean toggleDebug() {
		debug = !debug;
		return debug;
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
						if (debug) {
							Utils.safeLog("Received packet of type " + packet.getType() + " from session " + session.getId());
						}
						switch (packet.getType()) {
							case MESSAGE: {
								// If the packet is a message, send it to all clients, with a prefix displaying
								// the user's id.
								Message message = new Message(session.getId() + ": " + ((Message) packet).getMessage());
								sendPacket(message, session.getId());
								break;
							}
							case DISCONNECT: {
								if (debug) {
									Utils.safeLog("Session " + session.getId() + " sent disconnect message.");
								}
								removeSession(session);
								break;
							}
							case PING: {
								Pong pong = new Pong();
								oos.writeObject(pong);
								break;
							}
							case PONG: {
								session.clearTimeoutThread(debug, session.getId());
								break;
							}
						}
					}
				} catch (IOException e) {
					if (debug) {
						Utils.safeLog("Session " + session.getId() + " disconnected without explicit Disconnect Packet.");
					}
					removeSession(session);
				} catch (ClassNotFoundException e) {
					if (debug) {
						Utils.safeLog(
								"Class Not Found Exception Captured. Ensure that the client is running same Packet Entity Protocol.");
					}
					removeSession(session);
				}
			});
			session.setReadThread(thread);
			sessions.add(session);
			session.getReadThread().start();
		} catch (IOException e) {
		}
	}

	/**
	 * Sends a packet to all clients, except the one specified by senderId.
	 * 
	 * @param packet   The packet to send.
	 * @param senderId The id of the client that sent the packet.
	 */
	private void sendPacket(Packet packet, String senderId) {
		for (int i = 0; i < sessions.size(); i++) {
			Session session = sessions.get(i);
			if (session.getId().equals(senderId)) {
				continue;
			}
			try {
				session.getOos().writeObject(packet);
			} catch (IOException e) {
				if (debug) {
					Utils.safeLog(
							"Session " + session.getId() + " disconnected without explicit Disconnect Packet when sending packet.");
				}
				removeSession(session);
			}
		}
	}

	/**
	 * Removes a session from the list of active sessions.
	 * 
	 * @param session The session to remove.
	 */
	public void removeSession(Session session) {
		try {
			session.getOos().writeObject(new Disconnect());
		} catch (IOException e) {
		}
		try {
			session.getOis().close();
		} catch (IOException e) {
		}
		try {
			session.getOos().close();
		} catch (IOException e) {
		}
		try {
			session.getSocket().close();
		} catch (IOException e) {
		}

		session.getReadThread().interrupt();
		session.clearTimeoutThread(debug, session.getId());
		sessions.remove(session);
	}

	/**
	 * Closes the server socket, and cleans up all sessions.
	 */
	public void cleanup() {
		try {
			serverSocket.close();
		} catch (IOException e) {
		}
		pingThread.interrupt();
		sendPacket(new Disconnect(), null);
		if (debug) {
			Utils.safeLog("Cleaning up " + sessions.size() + " sessions.");
		}
		for (Session session : sessions) {
			removeSession(session);
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
