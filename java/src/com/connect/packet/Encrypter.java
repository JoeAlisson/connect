package com.connect.packet;

public interface Encrypter {
	
	byte[] encrypt(byte[] data);
	
	byte[] decrypt(byte[] data);

}
