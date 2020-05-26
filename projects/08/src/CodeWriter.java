import java.io.*;
import java.util.Arrays;


public class CodeWriter {

    public String currentCommand = "";
    public String inputFile;
    public int lineNumber = 0;
    public String currentLine;

    private int art2commands = 0;
    static private int functioncommands;
    static private int callcommands;

    String[] curCommand = new String[5];

    boolean isPush;

    String[] push = new String[]{"", "D=M", "", "D=D+A", "A=D", "D=M", "@SP", "A=M", "M=D", "@SP", "M=M+1"};
    String[] pop = new String[]{"", "D=M", "", "D=D+A", "@R13", "M=D", "@SP", "M=M-1", "A=M", "D=M", "@R13", "A=M", "M=D"};
    String[] pushC = new String[]{"", "D=A", "@SP", "A=M", "M=D", "@SP", "M=M+1"};
    String[] pushP = new String[]{"", "D=M", "@SP", "A=M", "M=D", "@SP", "M=M+1"};
    String[] popP = new String[]{"@SP", "M=M-1", "A=M", "D=M", "", "M=D"};
    String[] pushS = new String[]{"", "D=M", "@SP", "A=M", "M=D", "@SP", "M=M+1"};
    String[] popS = new String[]{"", "D=A", "@R13", "M=D", "@SP", "AM=M-1", "D=M", "@R13", "A=M", "M=D"};
    String[] art1 = new String[]{"@SP", "AM=M-1", "D=M", "A=A-1", ""};
    String[] art2 = new String[]{"@SP", "AM=M-1", "D=M", "A=A-1", "D=M-D", "", "", "@SP", "A=M-1", "M=-1", "", "0;JMP", "", "@SP", "A=M-1", "M=0", ""};
    String[] not = new String[]{"@SP", "A=M-1", "M=!M"};
    String[] neg = new String[]{"D=0", "@SP", "A=M-1", "M=D-M"};
    String[] fcall1 = new String[]{"", "D=A", "@SP", "A=M", "M=D", "@SP", "M=M+1"};
    String[] fcall2 = new String[]{"", "D=M", "@SP", "A=M", "M=D", "@SP", "M=M+1"};
    String[] fcall3 = new String[]{"D=M", "@5", "D=D-A", "", "D=D-A", "@ARG", "M=D", "@SP", "D=M", "@LCL", "M=D", "", "0;JMP", ""};
    String[] function1 = new String[]{"", "@SP", "A=M"};
    String[] function2 = new String[]{"M=0", "A=A+1"};
    String[] function3 = new String[]{"D=A", "@SP", "M=D"};
    String[] freturn1 = new String[]{"@LCL", "D=M", "@R14", "M=D", "@5", "A=D-A", "D=M", "@R15", "M=D", "@SP", "A=M-1", "D=M", "@ARG", "A=M", "M=D", "D=A+1", "@SP", "M=D"};
    String[] freturn2 = new String[]{"@R14", "AM=M-1", "D=M", "", "M=D"};
    String[] freturn3 = new String[]{"@R15", "A=M", "0;JMP"};

    private BufferedReader fileReader;


    enum CommandType {             // The four types of VM commands that can be expected
        MEM_COMMAND,
        ART_COMMAND,
        BRA_COMMAND,
        FUN_COMMAND,
    }

    private String[] arrayAdd(String[] arr1, String[] arr2) {     // This function appends one array to another
        int aLen = arr1.length;
        int bLen = arr2.length;
        String[] result = new String[aLen + bLen];

        System.arraycopy(arr1, 0, result, 0, aLen);
        System.arraycopy(arr2, 0, result, aLen, bLen);
        return result;
    }

    ;

    public CodeWriter(String file) throws FileNotFoundException {
        inputFile = file;
        fileReader = new BufferedReader(new FileReader(file));
        lineNumber = 0;
    }

    public boolean advance() throws IOException {
        while (true) {
            currentLine = fileReader.readLine();
            lineNumber++;
            if (currentLine == null)
                return false;
            currentCommand = currentLine.replaceAll("//.*$", "").trim();
            curCommand = currentCommand.split(" ");
            if (currentCommand.equals(""))
                continue;
            return true;
        }
    }

    public CommandType commandType() {
        if (currentCommand.startsWith("push")) {
            isPush = true;
            return CommandType.MEM_COMMAND;
        } else if (currentCommand.startsWith("pop")) {
            isPush = false;
            return CommandType.MEM_COMMAND;
        } else if (currentCommand.equals("add") || currentCommand.equals("sub") || currentCommand.equals("neg") || currentCommand.equals("not") || currentCommand.equals("lt") || currentCommand.equals("gt") || currentCommand.equals("eq") || currentCommand.equals("and") || currentCommand.equals("or")) {
            return CommandType.ART_COMMAND;
        } else if (currentCommand.startsWith("goto") || currentCommand.startsWith("if-goto") || currentCommand.startsWith("label")) {
            return CommandType.BRA_COMMAND;
        } else if (currentCommand.startsWith("function") || currentCommand.startsWith("call") || currentCommand.startsWith("return")) {
            return CommandType.FUN_COMMAND;
        } else {
            return null;
        }
    }

    public String[] mem_commands() {              //for local, argument, this, that, constants, temp, pointers and static
        String[] output = new String[15];
        switch (curCommand[1]) {
            case "local":
                if (isPush) {
                    output = push.clone();
                } else {
                    output = pop.clone();
                }
                output[2] = "@" + curCommand[2];
                output[0] = "@LCL";
                return output;
            case "argument":
                if (isPush) {
                    output = push.clone();
                } else {
                    output = pop.clone();
                }
                output[2] = "@" + curCommand[2];
                output[0] = "@ARG";
                return output;
            case "this":
                if (isPush) {
                    output = push.clone();
                } else {
                    output = pop.clone();
                }
                output[2] = "@" + curCommand[2];
                output[0] = "@THIS";
                return output;
            case "that":
                if (isPush) {
                    output = push.clone();
                } else {
                    output = pop.clone();
                }
                output[2] = "@" + curCommand[2];
                output[0] = "@THAT";
                return output;
            case "constant":
                output = pushC.clone();
                output[0] = "@" + curCommand[2];
                return output;
            case "temp":
                if (isPush) {
                    output = push.clone();
                } else {
                    output = pop.clone();
                }
                output[0] = "@" + curCommand[2];
                output[1] = "D=A";
                output[2] = "@5";
                return output;
            case "pointer":
                if (isPush && curCommand[2].equals("0")) {
                    output = pushP.clone();
                    output[0] = "@" + "3";
                } else if (isPush && curCommand[2].equals("1")) {
                    output = pushP.clone();
                    output[0] = "@" + "4";
                } else if (!isPush && curCommand[2].equals("0")) {
                    output = popP.clone();
                    output[4] = "@" + "3";
                } else {
                    output = popP.clone();
                    output[4] = "@" + "4";
                }
                return output;
            case "static":
                if (isPush) {
                    output = pushS.clone();
                    output[0] = "@" + inputFile.replaceAll(".vm", "").replaceAll(".*/", "").trim() + "." + curCommand[2];
                } else {
                    output = popS.clone();
                    output[0] = "@" + inputFile.replaceAll(".vm", "").replaceAll(".*/", "").trim() + "." + curCommand[2];
                }
                return output;
            default:
                return output;

        }
    }

    String[] art_commands() {    //for add, sub, neg, and, or, not, eq, lt, gt
        String[] output = new String[15];
        switch (currentCommand) {
            case "add":
                output = art1.clone();
                output[4] = "M=M+D";
                break;
            case "sub":
                output = art1.clone();
                output[4] = "M=M-D";
                break;
            case "and":
                output = art1.clone();
                output[4] = "M=M&D";
                break;
            case "or":
                output = art1.clone();
                output[4] = "M=M|D";
                break;
            case "lt":
                art2commands++;
                output = art2.clone();
                output[5] = "@IFFALSE" + art2commands;
                output[12] = "(IFFALSE" + art2commands + ")";
                output[10] = "@CONT" + art2commands;
                output[16] = "(CONT" + art2commands + ")";
                output[6] = "D;JGE";
                break;
            case "gt":
                art2commands++;
                output = art2.clone();
                output[5] = "@IFFALSE" + art2commands;
                output[12] = "(IFFALSE" + art2commands + ")";
                output[10] = "@CONT" + art2commands;
                output[16] = "(CONT" + art2commands + ")";
                output[6] = "D;JLE";
                break;
            case "eq":
                art2commands++;
                output = art2.clone();
                output[5] = "@IFFALSE" + art2commands;
                output[12] = "(IFFALSE" + art2commands + ")";
                output[10] = "@CONT" + art2commands;
                output[16] = "(CONT" + art2commands + ")";
                output[6] = "D;JNE";
                break;
            case "not":
                output = not.clone();
                break;
            case "neg":
                output = neg.clone();
                break;
            default:
                break;
        }
        return output;
    }

    String[] bra_commands() {               // for if-goto, goto and label
        String[] output = new String[7];
        Arrays.fill(output, "");
        if (curCommand[0].equals("if-goto")) {
            output[0] = "@SP";
            output[1] = "M=M-1";
            output[2] = "A=M";
            output[3] = "D=M";
            output[4] = "@" + curCommand[1];
            output[5] = "D;JNE";
            return output;
        } else if (curCommand[0].equals("goto")) {
            output[0] = "@" + curCommand[1];
            output[1] = "0;JMP";
            return output;
        } else {
            output[0] = "(" + curCommand[1] + ")";
            return output;
        }
    }

    String[] fun_commands() {       // for function call, return and function commands
        int nArgs;
        String[] output = new String[70];
        Arrays.fill(output, "");
        if (curCommand[0].equals("call")) {
            callcommands++;
            nArgs = Integer.parseInt(curCommand[2]);
            fcall1[0] = "@returnAdd" + callcommands;
            output = fcall1.clone();
            for (int i = 0; i < 4; i++) {
                if (i == 0) {
                    fcall2[0] = "@LCL";
                } else if (i == 1) {
                    fcall2[0] = "@ARG";
                } else if (i == 2) {
                    fcall2[0] = "@THIS";
                } else {
                    fcall2[0] = "@THAT";
                }
                output = arrayAdd(output, fcall2);
            }
            fcall3[3] = "@" + nArgs;
            fcall3[11] = "@" + curCommand[1];
            fcall3[13] = "(" + "returnAdd" + callcommands + ")";
            output = arrayAdd(output, fcall3);
            return output;
            // String[] fcall1 = new String[]{"", "D=A","@SP","A=M", "M=D","@SP","M=M+1"};
            // String[] fcall2 = new String[]{"", "D=M", "@SP", "A=M", "M=D", "@SP", "M=M+1"};
            // String[] fcall3 = new String[]{"D=M", "@5", "D=D-A", "", "D=D-A", "@ARG", "M=D", "@SP", "D=M", "@LCL", "M=D", "", "0;JMP", ""};
        } else if (curCommand[0].equals("function")) {
            functioncommands++;
            int nVars;
            nVars = Integer.parseInt(curCommand[2]);
            function1[0] = "(" + curCommand[1] + ")";
            output = function1.clone();
            for (int i = 0; i < nVars; i++) {
                output = arrayAdd(output, function2);
            }
            output = arrayAdd(output, function3);
            return output;
            // String[] function1 = new String[]{"", "@SP","A=M"};
            // String[] function2 = new String[]{"M=0","A=A+1"};
            // String[] function3 = new String[]{"D=A","@SP","M=D"};
        } else {
            output = freturn1.clone();
            for (int i = 0; i < 4; i++) {
                if (i == 0) {
                    freturn2[3] = "@THAT";
                } else if (i == 1) {
                    freturn2[3] = "@THIS";
                } else if (i == 2) {
                    freturn2[3] = "@ARG";
                } else {
                    freturn2[3] = "@LCL";
                }
                output = arrayAdd(output, freturn2);
            }
            output = arrayAdd(output, freturn3);
            return output;
            // String[] freturn1 = new String[]{"@LCL", "D=M", "@R14", "M=D", "@5", "A=D-A", "D=M", "@R15", "M=D", "@SP", "A=M-1", "D=M", "@ARG", "A=M", "M=D", "D=A+1", "@SP", "M=D"};
            // String[] freturn2 = new String[]{"@R14", "AM=M-1", "D=M", "", "M=D"};
            // String[] freturn3 = new String[]{"@R15", "A=M", "0;JMP"};
        }
    }


    public void close() throws IOException {
        fileReader.close();
    }
}