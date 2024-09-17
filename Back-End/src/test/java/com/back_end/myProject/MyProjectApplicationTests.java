package com.back_end.myProject;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@SpringBootTest
class MyProjectApplicationTests {

	@Test
	void contextLoads() throws NoSuchAlgorithmException {
		String password = "123456";
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(password.getBytes());
		byte[] digest = md.digest();
		System.out.println(digest);
	}




}
