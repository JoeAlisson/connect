package com.connect.packet;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

import com.connect.packet.exception.ConnectionException;

public abstract class PacketHandler<T extends Connection> {

	private static final int WRITE_BUFFER_SIZE = 64 * 1024;
	private static final byte HEADER_SIZE = 2;

	private T connection;
	private ByteBuffer writerBuffer;
	private ByteBuffer readerBuffer;
	private Encrypter encrypter;
	
	public PacketHandler() {
	}

	public PacketHandler(T connection) {
		this.connection = connection;
		writerBuffer = ByteBuffer.wrap(new byte[WRITE_BUFFER_SIZE]).order(ByteOrder.LITTLE_ENDIAN);
		readerBuffer = ByteBuffer.wrap(new byte[WRITE_BUFFER_SIZE]).order(ByteOrder.LITTLE_ENDIAN);
	}

	public void sendPacket(WritablePacket<T> packet) throws IOException, ConnectionException {
		if (packet == null) 
			return;
		
		byte[] data;
		int dataSize;
		synchronized (writerBuffer) {
			dataSize = prepareWriterBuffer(packet);
			data = new byte[dataSize];
			writerBuffer.get(data);
		}
		connection.send(data);
	}
	
	private int prepareWriterBuffer(WritablePacket<T> packet) {
		writerBuffer.clear();
		writerBuffer.position(HEADER_SIZE);
		writerBuffer.putShort(packet.getCode());
		packet.write(connection, writerBuffer);
		
		encrypt();
		
		int dataSize = writerBuffer.position();
		writerBuffer.position(0);
		writeHeader(writerBuffer, dataSize);
		writerBuffer.position(dataSize);
		writerBuffer.flip();
		return dataSize;
	}

	private void encrypt() {
		if(encrypter != null) {
			byte[] underlyingData = writerBuffer.array();
			byte[] encrypted = encrypter.encrypt(Arrays.copyOfRange(underlyingData, HEADER_SIZE, writerBuffer.position()));
			writerBuffer.position(HEADER_SIZE);
			writerBuffer.put(encrypted);
		}
	}
	
	
	private void writeHeader(ByteBuffer writerBuffer, int dataSize) {
		switch (HEADER_SIZE) {
		case 4:
			writerBuffer.putInt((int)(dataSize - HEADER_SIZE));
			break;
		case 8:
			writerBuffer.putLong((long)(dataSize - HEADER_SIZE));
		case 2:
		default:
			writerBuffer.putShort((short)(dataSize - HEADER_SIZE));
			break;
		}
		
	}
	
	public void readAndProcessPacket() throws IOException, ConnectionException {
		ReadablePacket<T> packet = readPacket();
		packet.process(connection);
	}

	protected ReadablePacket<T> readPacket() throws IOException, ConnectionException {
		byte[] data = connection.read(HEADER_SIZE);

		if(encrypter != null) 
			data = encrypter.decrypt(data);
		
		ReadablePacket<T> packet;
		synchronized (readerBuffer) {
			readerBuffer.clear();
			readerBuffer.put(data);
			readerBuffer.flip();
			short packetCode = readerBuffer.getShort();
			packet = getReadablePacket(packetCode);
			packet.read(connection, readerBuffer);
		}
		return packet;
		
	}
	
	protected abstract  ReadablePacket<T> getReadablePacket(short packetCode);

}
