package com.connect.packet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketImplFactory;
import java.net.UnknownHostException;

public class SocketConnection extends Connection {
	
	private Socket socket;
	
	public SocketConnection(String host, short port) throws UnknownHostException, IOException {
		socket = new Socket(host, port);
	}
	
	public SocketConnection(InetAddress address, short port) throws IOException {
		socket = new Socket(address, port);
	}
	
	public SocketConnection(String host, short port, InetAddress localAddress, short localPort) throws IOException {
		socket = new Socket(host, port, localAddress, localPort);
	}
	
	/**
	 * @see Socket#setSocketImplFactory(SocketImplFactory)
	 * 
	 * @param fac
	 * @throws IOException
	 */
	public static void setSocketFactory(SocketImplFactory fac) throws IOException {
		Socket.setSocketImplFactory(fac);
	}

	/**
	 * @see Socket#getInputStream()
	 */
	@Override
	protected InputStream getInputStream() throws IOException {
		return socket.getInputStream();
	}

	/**
	 * @see Socket#getOutputStream()
	 */
	@Override
	protected OutputStream getOutputStream() throws IOException {
		return socket.getOutputStream();
	}

	@Override
	protected boolean isConnected() {
		return socket.isConnected();
	}

	public void close() {
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}
}
