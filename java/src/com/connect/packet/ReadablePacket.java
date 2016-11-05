package com.connect.packet;

import java.nio.ByteBuffer;

public interface ReadablePacket<T extends Connection> {
	
	void read(T connection, ByteBuffer buffer);
	
	void process(T connection);

}
