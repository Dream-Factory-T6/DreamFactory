package com.DreamFactory.DF;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class DfApplicationTests {

	@Test
	void main() {
		assertDoesNotThrow(() -> DfApplication.main(new String[]{}));
	}
}
