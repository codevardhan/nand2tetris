import java.io.*;

public class VMTranslator {
    public static void main(String[] args) {
        if (args.length != 1)
            Error.error("Please specify file to translate.");

        String inputFile = args[0];

        try {
            Translator translate = new Translator(inputFile);
            translate.tlate();
            translate.close();
        } catch (FileNotFoundException ex) {
            Error.error("file \'" + inputFile + "\' not found");
        } catch (IOException ex) {
            Error.error("an i/o exception occured");
        }

    }
}
