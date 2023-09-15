package oe.aloha.Entities;

import java.io.Serializable;

public abstract class Packet implements Serializable {
	private PacketType type;

	public Packet(PacketType type) {
		this.type = type;
	}

	public PacketType getType() {
		return type;
	}
}
