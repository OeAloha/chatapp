package oe.aloha.Entities.packet;

import oe.aloha.Entities.Packet;
import oe.aloha.Entities.PacketType;

/**
 * Packet for sending a disconnection state.
 */
public class Disconnect extends Packet {
	public Disconnect() {
		super(PacketType.DISCONNECT);
	}
}
