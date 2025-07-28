package com.DreamFactory.DF;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ActiveProfiles("test")
class DfApplicationTests {

	@Test
	void main() {
		assertDoesNotThrow(() -> DfApplication.main(new String[]{}));
	}
}
