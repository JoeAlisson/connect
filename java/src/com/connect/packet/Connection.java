package com.connect.packet;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.connect.packet.exception.ConnectionException;


public abstract class Connection implements Closeable {
	
	private static final int BYTE_LENGTH = 8;
	
	public synchronized void send(byte[] data) throws IOException, ConnectionException {
		if(data == null || data.length == 0) 
			return;
		OutputStream output = getOutputStream();
		if(output == null)
			throw new ConnectionException("Connection OutputStream is null. Can't send data");
		output.write(data);
		output.flush();
	}

	
	public synchronized byte[] read(byte headerSize) throws IOException, ConnectionException {
		InputStream input = getInputStream();
		if(input == null) {
			throw new ConnectionException("Connection InputStream is null. Can't receive data");
		}
		if (!isConnected())
			return null;
		
		try {
			int dataSize = 0;
			for(int i = 0; i < headerSize; i++) {
				dataSize |= input.read() << (BYTE_LENGTH * i);
			}
			
			byte[] data = new byte[dataSize];
			int received = 0;
			
			do {
				received += input.read(data, received, dataSize - received);
			} while(received < dataSize);
			
			return data;
		} catch (NegativeArraySizeException e) {
			throw new ConnectionException("Received negative size of data trying to read");
		}
	}
	
	
	
	protected abstract InputStream getInputStream() throws IOException;
	protected abstract OutputStream getOutputStream() throws IOException;
	protected abstract boolean isConnected();
	

}
