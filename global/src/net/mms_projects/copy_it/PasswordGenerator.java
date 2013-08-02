package net.mms_projects.copy_it;

import java.security.SecureRandom;
import java.math.BigInteger;

public final class PasswordGenerator {

	private SecureRandom random = new SecureRandom();

	public String generatePassword() {
		return new BigInteger(130, this.random).toString(32);
	}

}