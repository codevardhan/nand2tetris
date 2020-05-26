import java.io.*;

public class Translator {
    private String inputFile;
    private PrintWriter out;


    public Translator(String file) throws IOException {
        inputFile = file;
        String outputFile = inputFile.replaceAll("\\..*", "") + ".asm";
        out = new PrintWriter(new FileWriter(outputFile));
    }

    public void tlate() throws IOException {
        Parser parse = new Parser(inputFile);
        while (parse.advance()) {
            String[] outp;
            if (Parser.CommandType.MEM_COMMAND == parse.commandType()) {
                outp = parse.mem_commands();
                out.println("// " + parse.currentCommand);
                for (String s : outp) {
                    out.println(s);
                }
            } else if (Parser.CommandType.ART_COMMAND == parse.commandType()) {
                outp = parse.art_commands();
                out.println("// " + parse.currentCommand);
                for (String s : outp) {
                    out.println(s);
                }
            }
        }
        parse.close();
    }

    public void close() throws IOException {
        out.close();
    }
}