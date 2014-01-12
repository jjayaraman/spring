package com.jai.spring.security.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class NounceGenerator {

	public static void main(String[] args) {

		try {
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
			System.out.println(new String(secureRandom.generateSeed(16)));

			byte[] nonce = new byte[16];

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

}
