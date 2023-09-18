package oe.aloha.Entities.packet;

import oe.aloha.Entities.Packet;
import oe.aloha.Entities.PacketType;

/**
 * Packet for acknowledging a ping.
 */
public class Pong extends Packet {
	public Pong() {
		super(PacketType.PONG);
	}
}
