package oe.aloha.Entities.packet;

import oe.aloha.Entities.Packet;
import oe.aloha.Entities.PacketType;

/**
 * Packet for sending a ping.
 */
public class Ping extends Packet {
	public Ping() {
		super(PacketType.PING);
	}
}
