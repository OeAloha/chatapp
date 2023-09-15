package oe.aloha.Entities.packet;

import oe.aloha.Entities.Packet;
import oe.aloha.Entities.PacketType;

public class Ping extends Packet {
	public Ping() {
		super(PacketType.PING);
	}
}
