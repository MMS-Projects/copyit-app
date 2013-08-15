package net.mms_projects.utils;

/**
 * This class contains all kinds of methods for operations on strings.
 *
 * @see <a href="http://stackoverflow.com/a/3657496">The source</a>
 */
public final class StringUtils {

    /**
     * A protected constructor because this is a utility class and it shoudn't
     * be constructible.
     */
    protected StringUtils() {
        // prevents calls from subclass
        throw new UnsupportedOperationException();
    }

    /**
     * A regular expression pattern to check for non thin characters.
     */
    private static final String NON_THIN_REGEX = "[^iIl1\\.,']";
    /**
     * The ellipsis character(s).
     */
    public static final String ELLIPSIS = "...";
    /**
     * The length of the ellipsis character(s).
     */
    private static final int ELLIPSIS_LENGTH = ELLIPSIS.length();

    /**
     * This method returns the width of the text by making sure that characters
     * that are not in the {@link NON_THIN_REGEX} get counted as a half.
     *
     * @param text
     *            The text to get the width of.
     * @return The width of the text.
     */
    private static int textWidth(final String text) {
        return (int) (text.length() - text.replaceAll(NON_THIN_REGEX, "")
                .length() / 2);
    }

    /**
     * Shortens a text and adds a ellipsis at the end. If the max length is
     * shorter then the ellipsis or the length is negative it will shorten the
     * ellipsis or return a empty string.
     *
     * @param text
     *            The text to ellipsize
     * @param max
     *            The max length of the text
     * @param useThinCharacters
     *            Whenever to take thin characters in account when calculating
     *            the length
     * @return The shortened string with a ellipsis
     */
    public static String ellipsize(final String text, final int max,
            final boolean useThinCharacters) {
        if (text == null) {
            throw new NullPointerException("No text specified to ellipsize");
        }
        /*
         * The max length is shorter then the length of the ellipsis.
         */
        if (max < ELLIPSIS_LENGTH) {
            if (max < 0) {
                /*
                 * The length is negative. Return a empty string.
                 */
                return "";
            } else {
                /*
                 * The length is shorter then the length of the ellipsis. Cut it
                 * off.
                 */
                return ELLIPSIS.substring(0, max);
            }
        }
        if (useThinCharacters) {
            if (textWidth(text) <= max) {
                return text;
            }
        } else {
            if (text.length() <= max) {
                return text;
            }
        }

        /*
         * Start by chopping off at the word before max This is an
         * over-approximation due to thin-characters...
         */
        int end = text.lastIndexOf(' ', max - ELLIPSIS_LENGTH);

        /*
         * Just one long word. Chop it off.
         */
        if (end == -1) {
            return text.substring(0, max - ELLIPSIS_LENGTH) + ELLIPSIS;
        }

        /*
         * Step forward as long as textWidth allows.
         */
        int newEnd = end;
        do {
            end = newEnd;
            newEnd = text.indexOf(' ', end + 1);

            /*
             * No more spaces.
             */
            if (newEnd == -1) {
                newEnd = text.length();
            }

        } while (((useThinCharacters) ? textWidth(text.substring(0, newEnd)
                + ELLIPSIS) : (text.substring(0, newEnd) + ELLIPSIS).length()) < max);

        return text.substring(0, end) + ELLIPSIS;
    }

    /**
     * Shortens a text and adds a ellipsis at the end. If the max length is
     * shorter then the ellipsis or the length is negative it will shorten the
     * ellipsis or return a empty string.
     *
     * @param text
     *            The text to ellipsize
     * @param max
     *            The max length of the text
     * @return The shortened string with a ellipsis
     */
    public static String ellipsize(final String text, final int max) {
        return ellipsize(text, max, false);
    }

}
