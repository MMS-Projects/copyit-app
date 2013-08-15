package net.mms_projects.utils;

/**
 * This class contains methods to check on which operating system the
 * application is running.
 *
 * @see <a
 *      href="http://www.mkyong.com/java/how-to-detect-os-in-java-systemgetpropertyosname/">Source</a>
 */
public class OSValidator {

    /**
     * A protected constructor because this is a utility class and it shoudn't
     * be constructible.
     */
    protected OSValidator() {
        // prevents calls from subclass
        throw new UnsupportedOperationException();
    }

    /**
     * This method checks if the current operating system is Mac OS X.
     *
     * @return True if the operating system is Mac OS X
     */
    public static boolean isMac() {
        String os = System.getProperty("os.name").toLowerCase();
        return (os.indexOf("mac") >= 0);
    }

    /**
     * This method checks if the current operating system is Solaris.
     *
     * @return True if the operating system is Solaris
     */
    public static boolean isSolaris() {
        String os = System.getProperty("os.name").toLowerCase();
        return (os.indexOf("sunos") >= 0);

    }

    /**
     * This method checks if the current operating system is UNIX or Linux.
     *
     * @return True if the operating system is UNIX
     */
    public static boolean isUnix() {
        String os = System.getProperty("os.name").toLowerCase();
        return (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0);

    }

    /**
     * This method checks if the current operating system is Windows.
     *
     * @return True if the operating system is Windows
     */
    public static boolean isWindows() {
        String os = System.getProperty("os.name").toLowerCase();
        return (os.indexOf("win") >= 0);

    }

    public static void main(final String[] args) {
        if (isWindows()) {
            System.out.println("This is Windows");
        } else if (isMac()) {
            System.out.println("This is Mac");
        } else if (isUnix()) {
            System.out.println("This is Unix or Linux");
        } else if (isSolaris()) {
            System.out.println("This is Solaris");
        } else {
            System.out.println("Your OS is not support!!");
        }
    }

}
