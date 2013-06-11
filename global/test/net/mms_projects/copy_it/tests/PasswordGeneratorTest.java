package net.mms_projects.copy_it.tests;

import net.mms_projects.copyit.PasswordGenerator;
import junit.framework.TestCase;

public class PasswordGeneratorTest extends TestCase {

	private PasswordGenerator generator;

	protected void setUp() throws Exception {
		super.setUp();
		
		this.generator = new PasswordGenerator();
	}

	public void testLength() {
		assertEquals(26, this.generator.generatePassword().length());
	}

}
