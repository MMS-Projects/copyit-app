package net.mms_projects.copy_it;

import java.security.SecureRandom;
import java.math.BigInteger;

/**
 * This class is used to generate random passwords.
 */
public final class PasswordGenerator {

    private SecureRandom random = new SecureRandom();

    /**
     * This method generates a random password.
     * @return A random password.
     */
    public String generatePassword() {
        return new BigInteger(130, this.random).toString(32);
    }

}
