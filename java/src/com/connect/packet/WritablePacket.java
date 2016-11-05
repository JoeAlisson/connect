package com.connect.packet;

import java.nio.ByteBuffer;

public interface WritablePacket<T extends Connection>{

	short getCode();

	void write(T connection, ByteBuffer buffer);

	
}
