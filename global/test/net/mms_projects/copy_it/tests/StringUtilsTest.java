package net.mms_projects.copy_it.tests;

import junit.framework.TestCase;
import net.mms_projects.utils.StringUtils;

/**
 * A class to unit test the {@link StringUtils}.
 */
public class StringUtilsTest extends TestCase {

    /**
     * The text to test the ellipsize method with.
     */
    private static final String ELLIPSIS_STRING = "Hello there I'm a "
            + "very long string! Yay very nice good";
    /**
     * The length of the ellipsize method. Used to do a full text test.
     */
    private static final int ELLIPSIS_FULL_TEXT_LENGTH = ELLIPSIS_STRING
            .length();

    public final void testEllipsize() {
        assertEquals("Hello there I'm a" + StringUtils.ELLIPSIS,
                StringUtils.ellipsize(ELLIPSIS_STRING, 20));
        assertEquals("Hello there I'm a very long string! Yay"
                + StringUtils.ELLIPSIS,
                StringUtils.ellipsize(ELLIPSIS_STRING, 40));
        assertEquals(ELLIPSIS_STRING, StringUtils.ellipsize(ELLIPSIS_STRING,
                ELLIPSIS_FULL_TEXT_LENGTH));
        assertEquals(ELLIPSIS_STRING, StringUtils.ellipsize(ELLIPSIS_STRING,
                ELLIPSIS_FULL_TEXT_LENGTH + 10));
    }

}
