import java.io.*;

public class Parser {
    private String inputFile;
    private PrintWriter out;
    static boolean isFirst = true;
    String init = "// bootsrapping code \n" + "@256 \n" + "D=A \n" + "@SP \n" + "M=D \n" + "@retAdd\n" + "D=A\n" +
            "@SP\n" + "A=M\n" + "M=D\n" + "@SP\n" + "M=M+1\n" + "@LCL\n" + "D=M\n" + "@SP\n" + "A=M\n" + "M=D\n" +
            "@SP\n" + "M=M+1\n" + "@ARG\n" + "D=M\n" + "@SP\n" + "A=M\n" + "M=D\n" + "@SP\n" + "M=M+1\n" + "@THIS\n" +
            "D=M\n" + "@SP\n" + "A=M\n" + "M=D\n" + "@SP\n" + "M=M+1\n" + "@THAT\n" + "D=M\n" + "@SP\n" + "A=M\n" +
            "M=D\n" + "@SP\n" + "M=M+1\n" + "@Sys.init\n" + "0;JMP\n" + "(retAdd)\n" + "0;JMP";


    public Parser(String file, boolean isFile, String folder) throws IOException {      // Handles the filenames and processing of files or directories
        inputFile = file;
        String outputFile;
        if (isFile) {
            outputFile = inputFile.replaceAll(".vm", ".asm");
        } else {
            outputFile = folder.replaceAll("//..", "").trim() + "/" + folder.replaceAll(".*/", "").trim() + ".asm";
        }
        if (isFirst & !isFile) {
            isFirst = false;
            out = new PrintWriter(new FileWriter(outputFile));
            out.println(init);
        } else if (isFirst) {
            isFirst = false;
            out = new PrintWriter(new FileWriter(outputFile));
        } else {
            out = new PrintWriter(new FileWriter(outputFile, true));
        }

    }

    public void parse() throws IOException {                          // invokes the CodeWriter class to translate the current command
        CodeWriter code = new CodeWriter(inputFile);
        while (code.advance()) {
            String[] outp;
            if (CodeWriter.CommandType.MEM_COMMAND == code.commandType()) {
                outp = code.mem_commands();
                out.println("// " + code.currentCommand);
                for (String s : outp) {
                    out.println(s);
                }
            } else if (CodeWriter.CommandType.ART_COMMAND == code.commandType()) {
                outp = code.art_commands();
                out.println("// " + code.currentCommand);
                for (String s : outp) {
                    out.println(s);
                }
            } else if (CodeWriter.CommandType.BRA_COMMAND == code.commandType()) {
                outp = code.bra_commands();
                out.println("// " + code.currentCommand);
                for (String s : outp) {
                    if (!s.equals(""))
                        out.println(s);
                }
            } else if (CodeWriter.CommandType.FUN_COMMAND == code.commandType()) {
                outp = code.fun_commands();
                out.println("// " + code.currentCommand);
                for (String s : outp) {
                    out.println(s);
                }
            } else {
                out.println("");
            }
        }
        code.close();
    }

    public void close() throws IOException {
        out.close();
    }
}