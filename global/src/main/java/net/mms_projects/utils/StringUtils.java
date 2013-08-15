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
     * Shortens and string a adds a ellipsis at the end.
     *
     * @param text
     *            The text to ellipsize
     * @param max
     *            The max length of the text
     * @return The shortened string with a ellipsis
     */
    public static String ellipsize(final String text, final int max) {
        if (textWidth(text) <= max) {
            return text;
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

        } while (textWidth(text.substring(0, newEnd) + ELLIPSIS) < max);

        return text.substring(0, end) + ELLIPSIS;
    }

}
