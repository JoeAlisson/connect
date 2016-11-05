package com.connect.packet;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public abstract class AbstractReadablePacket<T extends Connection> implements ReadablePacket<T> {
	
	protected String readString(ByteBuffer buffer) {
		int position = buffer.position();
		int length = 0;
		while(buffer.get() != 0) 
			length++;
		byte[] dst = new byte[length];
		buffer.position(position);
		buffer.get(dst);
		buffer.get();
		return new String(dst, StandardCharsets.UTF_8);
	}
	
	@Override
	public String toString() {
		return "Readable Packet " + getClass().getSimpleName();
	}

}
