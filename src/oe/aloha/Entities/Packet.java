package oe.aloha.Entities;

import java.io.Serializable;

/**
 * Abstract class for packets.
 * Implements Serializable to allow sending over sockets.
 * All packets must extend this class.
 */
public abstract class Packet implements Serializable {
	private PacketType type;

	public Packet(PacketType type) {
		this.type = type;
	}

	public PacketType getType() {
		return type;
	}
}
