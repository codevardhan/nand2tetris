import java.io.*;

public class VMTranslator {
    public static void main(String[] args) {
        if (args.length != 1)
            Error.error("Please specify file to translate.");
        String inputFile = args[0];
        // String[] arg = new String[5];
        // arg[0] = "/home/codevardhan/StaticsTest";
        // String inputFile = arg[0];
        File inFile = new File(inputFile);

        FilenameFilter filter = (inFile1, name) -> name.endsWith(".vm");

        if (inFile.isFile() && inputFile.contains(".vm")) {
            try {
                Parser translate = new Parser(inputFile, true, "");
                translate.parse();
                translate.close();
            } catch (FileNotFoundException ex) {
                Error.error("file '" + inputFile + "' not found");
            } catch (IOException ex) {
                Error.error("an i/o exception occured");
            }
        } else {

            File[] files = inFile.listFiles(filter);
            assert files != null;
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".vm")) {
                    inputFile = file.getAbsolutePath();
                    try {
                        Parser translate = new Parser(inputFile, false, args[0]);
                        translate.parse();
                        translate.close();
                    } catch (FileNotFoundException ex) {
                        Error.error("file '" + inputFile + "' not found");
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        Error.error("An i/o exception occured");

                    }
                }
            }
        }
    }
}
