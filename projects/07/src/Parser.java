import java.io.*;

public class Parser {

    public String currentCommand = "";
    public String inputFile;
    public int lineNumber = 0;
    public String currentLine;

    private int art2commands=0;

    String[] arr = new String[5];

    boolean isPush;

    String[] push = new String[]{"", "D=M", "", "D=D+A", "A=D", "D=M", "@SP", "A=M", "M=D", "@SP", "M=M+1"};
    String[] pop = new String[]{"", "D=M", "", "D=D+A", "@R13", "M=D", "@SP", "M=M-1", "A=M", "D=M", "@R13", "A=M", "M=D"};
    String[] pushC = new String[]{"", "D=A", "@SP", "A=M", "M=D", "@SP", "M=M+1"};
    String[] pushP = new String[]{"", "D=M", "@SP", "A=M", "M=D", "@SP", "M=M+1"};
    String[] popP = new String[]{"@SP", "M=M-1", "A=M", "D=M", "", "M=D"};
    String[] pushS = new String[]{"", "D=M", "@SP", "A=M", "M=D", "@SP", "M=M+1"};
    String[] popS = new String[]{"@SP", "AM=M-1", "D=M", "", "M=D"};
    String[] art1 = new String[]{"@SP", "AM=M-1", "D=M", "A=A-1", ""};
    String[] art2 = new String[]{"@SP", "AM=M-1", "D=M", "A=A-1", "D=M-D", "", "", "@SP", "A=M-1", "M=-1", "", "0;JMP", "", "@SP", "A=M-1", "M=0", ""};
    String[] not = new String[]{"@SP", "A=M-1", "M=!M"};
    String[] neg = new String[]{"D=0", "@SP", "A=M-1", "M=D-M"};


    private BufferedReader fileReader;


    enum CommandType {
        MEM_COMMAND,
        ART_COMMAND,
    }

    public Parser(String file) throws FileNotFoundException {
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
            arr = currentCommand.split(" ");
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
        } else if (currentCommand.contains("add") || currentCommand.contains("sub") || currentCommand.contains("neg") || currentCommand.contains("not") || currentCommand.contains("lt") || currentCommand.contains("gt") || currentCommand.contains("eq") || currentCommand.contains("and") || currentCommand.contains("or")) {
            return CommandType.ART_COMMAND;
        } else {
            return null;
        }
    }

    public String[] mem_commands() {              //for local, argument, this, that, constants, temp, pointers and static
        String[] output = new String[15];
        if (currentCommand.contains("local")) {
            if (isPush) {
                output = push.clone();
            } else {
                output = pop.clone();
            }
            output[2] = "@" + arr[2];
            output[0] = "@LCL";
            return output;
        } else if (currentCommand.contains("argument")) {
            if (isPush) {
                output = push.clone();
            } else {
                output = pop.clone();
            }
            output[2] = "@" + arr[2];
            output[0] = "@ARG";
            return output;
        } else if (currentCommand.contains("this")) {
            if (isPush) {
                output = push.clone();
            } else {
                output = pop.clone();
            }
            output[2] = "@" + arr[2];
            output[0] = "@THIS";
            return output;
        } else if (currentCommand.contains("that")) {
            if (isPush) {
                output = push.clone();
            } else {
                output = pop.clone();
            }
            output[2] = "@" + arr[2];
            output[0] = "@THAT";
            return output;
        } else if (currentCommand.contains("constant")) {
            output = pushC.clone();
            output[0] = "@" + arr[2];
            return output;
        } else if (currentCommand.contains("temp")) {
            if (isPush) {
                output = push.clone();
            } else {
                output = pop.clone();
            }
            output[0] = "@" + arr[2];
            output[1] = "D=A";
            output[2] = "@5";
            return output;
        } else if (currentCommand.contains("pointer")) {
            if (isPush && arr[2].equals("0")) {
                output = pushP.clone();
                output[0] = "@" + "3";
            } else if (isPush && arr[2].equals("1")) {
                output = pushP.clone();
                output[0] = "@" + "4";
            } else if (!isPush && arr[2].equals("0")) {
                output = popP.clone();
                output[4] = "@" + "3";
            } else {
                output = popP.clone();
                output[4] = "@" + "4";
            }
            return output;
        } else if (currentCommand.contains("static")) {
            if (isPush) {
                output = pushS.clone();
                int val = 16 + Integer.parseInt(arr[2]);
                output[0] = "@" + val;
            } else {
                output = popS.clone();
                int val = 16 + Integer.parseInt(arr[2]);
                output[3] = "@" + val;
            }
            return output;
        } else {
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
                output[5]="@IFFALSE"+art2commands;
                output[12]="(IFFALSE"+art2commands+")";
                output[10]="@CONT"+art2commands;
                output[16]="(CONT"+art2commands+")";
                output[6] = "D;JGE";
                break;
            case "gt":
                art2commands++;
                output = art2.clone();
                output[5]="@IFFALSE"+art2commands;
                output[12]="(IFFALSE"+art2commands+")";
                output[10]="@CONT"+art2commands;
                output[16]="(CONT"+art2commands+")";
                output[6] = "D;JLE";
                break;
            case "eq":
                art2commands++;
                output = art2.clone();
                output[5]="@IFFALSE"+art2commands;
                output[12]="(IFFALSE"+art2commands+")";
                output[10]="@CONT"+art2commands;
                output[16]="(CONT"+art2commands+")";
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


    public void close() throws IOException {
        fileReader.close();
    }
}