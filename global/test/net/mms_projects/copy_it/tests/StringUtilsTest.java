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
        int maxLength = 10;
        String result = StringUtils.ellipsize(ELLIPSIS_STRING, maxLength);
        assertEquals("Hello" + StringUtils.ELLIPSIS, result);
        lengthCheck(maxLength, result.length());

        maxLength = 20;
        result = StringUtils.ellipsize(ELLIPSIS_STRING, maxLength);
        assertEquals("Hello there I'm a" + StringUtils.ELLIPSIS, result);
        lengthCheck(maxLength, result.length());

        maxLength = 30;
        result = StringUtils.ellipsize(ELLIPSIS_STRING, maxLength);
        assertEquals("Hello there I'm a very long" + StringUtils.ELLIPSIS,
                result);
        lengthCheck(maxLength, result.length());

        maxLength = 35;
        result = StringUtils.ellipsize(ELLIPSIS_STRING, maxLength);
        assertEquals("Hello there I'm a very long" + StringUtils.ELLIPSIS,
                result);
        lengthCheck(maxLength, result.length());

        maxLength = 40;
        result = StringUtils.ellipsize(ELLIPSIS_STRING, maxLength);
        assertEquals("Hello there I'm a very long string!"
                + StringUtils.ELLIPSIS, result);
        lengthCheck(maxLength, result.length());

        maxLength = ELLIPSIS_FULL_TEXT_LENGTH;
        result = StringUtils.ellipsize(ELLIPSIS_STRING, maxLength);
        assertEquals(ELLIPSIS_STRING, result);
        lengthCheck(maxLength, result.length());

        maxLength = ELLIPSIS_FULL_TEXT_LENGTH + 10;
        result = StringUtils.ellipsize(ELLIPSIS_STRING, maxLength);
        assertEquals(ELLIPSIS_STRING, result);
        lengthCheck(maxLength, result.length());

        /*
         * A check with a very long word. It should be cut off.
         */
        maxLength = 10;
        result = StringUtils.ellipsize("Verylongwobblyword", maxLength);
        assertEquals("Verylon" + StringUtils.ELLIPSIS, result);
        lengthCheck(maxLength, result.length());

        /*
         * If the max length is shorter then the length of the ellipsis it
         * should cut it off.
         */
        maxLength = StringUtils.ELLIPSIS.length() - 1;
        result = StringUtils.ellipsize(ELLIPSIS_STRING, maxLength);
        assertEquals(StringUtils.ELLIPSIS.substring(0, maxLength), result);
        lengthCheck(maxLength, result.length());

        /*
         * A check with negative maximal length. It should return a empty
         * string.
         */
        maxLength = -10;
        result = StringUtils.ellipsize(ELLIPSIS_STRING, maxLength);
        assertEquals("", result);
        assertEquals(0, result.length());

        /*
         * A test with a null string. It should throw a NullPointerException in
         * the ellipsize method.
         */
        try {
            StringUtils.ellipsize(null, 2);

            fail("A NullPointerException should have been thrown on the text.");
        } catch (NullPointerException exception) {
            /*
             * The exception should be thrown in the ellipsize method itself.
             */
            assertEquals(
                    "NullPointerException not thrown by the ellipsize method",
                    "ellipsize", exception.getStackTrace()[0].getMethodName());
        }
    }

    public final void testEllipsizeThin() {
        /*
         * A test with a null string. It should throw a NullPointerException in
         * the ellipsize method.
         */
        try {
            StringUtils.ellipsize(null, 2);

            fail("A NullPointerException should have been thrown on the text.");
        } catch (NullPointerException exception) {
            /*
             * The exception should be thrown in the ellipsize method itself.
             */
            assertEquals(
                    "NullPointerException not thrown by the ellipsize method",
                    "ellipsize", exception.getStackTrace()[0].getMethodName());
        }
    }

    private void lengthCheck(final int maxLength, final int actualLength) {
        assertTrue("The max length is " + maxLength
                + " but the actual length is " + actualLength,
                actualLength <= maxLength);
    }

}
