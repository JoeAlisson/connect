package com.connect.packet;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public abstract class AbstractWritablePacket<T extends Connection> implements WritablePacket<T> {
	
	
	public AbstractWritablePacket(T connection) {
		
	}
	
	protected void writeString(ByteBuffer buf, String str) {
		buf.put(str.getBytes(StandardCharsets.UTF_8));
		buf.put((byte)'\000');
	}
	
	@Override
	public String toString() {
		return "Writable Packet: " + getClass().getSimpleName(); 
	}
}
