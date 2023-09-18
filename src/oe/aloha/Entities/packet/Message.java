package oe.aloha.Entities.packet;

import oe.aloha.Entities.Packet;
import oe.aloha.Entities.PacketType;

/**
 * Packet for sending messages.
 */
public class Message extends Packet {
	private String message;

	public Message(String message) {
		super(PacketType.MESSAGE);
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
