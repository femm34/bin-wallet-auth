package com.fecd.bin_wallet_auth;

import com.fecd.bin_wallet_auth.users.domain.model.User;
import com.fecd.bin_wallet_auth.users.repository.UserRepository;
import com.fecd.bin_wallet_auth.users.request.UserRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class BinWalletAuthApplicationTests {

	@Autowired
	private UserRepository userRepository;

	@Test
	void contextLoads() {
		User user = this.userRepository.findByUsername("ivanov67").orElseThrow();
		assertEquals(user.getEmail(),"ignizbones@gmail.com");
	}

}
