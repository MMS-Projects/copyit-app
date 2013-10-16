package net.mms_projects.copy_it.tests;

import junit.framework.TestCase;
import net.mms_projects.utils.InlineSwitch;

public class UtilsInlineSwitchTest extends TestCase {

	public void testEmpty() {
		InlineSwitch<Integer, String> switcher = new InlineSwitch<Integer, String>();

		assertNull(switcher.runSwitch(1));
		assertNull(switcher.runSwitch(2));
		assertNull(switcher.runSwitch(3));
	}
	
	public void testHasNoDefault() {
		InlineSwitch<Integer, String> switcher = new InlineSwitch<Integer, String>();
		switcher.addClause(1, "Line 1");
		switcher.addClause(2, "Line 2");
		switcher.addClause(3, "Line 3");

		assertEquals("Line 1", switcher.runSwitch(1));
		assertEquals("Line 2", switcher.runSwitch(2));
		assertEquals("Line 3", switcher.runSwitch(3));
		assertNull(switcher.runSwitch(4));
	}
	
	public void testHasDefault() {
		InlineSwitch<Integer, String> switcher = new InlineSwitch<Integer, String>();
		switcher.addClause(1, "Line 1");
		switcher.addClause(2, "Line 2");
		switcher.addClause(3, "Line 3");
		switcher.setDefault("None");

		assertEquals("Line 1", switcher.runSwitch(1));
		assertEquals("Line 2", switcher.runSwitch(2));
		assertEquals("Line 3", switcher.runSwitch(3));
		assertEquals("None", switcher.runSwitch(4));
	}

}
