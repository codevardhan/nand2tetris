class Error {                            // This class handles all the errors regarding filereading and produces a suitable output
    public static void error(String message, String fileName, int lineNum, String line) {
        System.err.println(fileName + ":" + lineNum + ": Error: " + message);
        System.err.println("\t" + line);
        System.exit(1);
    }

    public static void error(String message, String fileName, int lineNum) {
        System.err.println(fileName + ":" + lineNum + ": Error: " + message);
        System.exit(1);
    }

    public static void error(String message) {
        System.err.println("Error: " + message);
        System.exit(1);
    }
}
