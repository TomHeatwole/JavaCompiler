import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

public abstract class Generator {

    private AbstractSyntaxTree input;

    protected BufferedWriter output;

    protected static HashSet<String> noOperandCommands;
    protected static HashSet<String> oneOperandCommands;

    static {
        noOperandCommands = new HashSet<>();
        oneOperandCommands = new HashSet<>();

        noOperandCommands.add("ret");

        oneOperandCommands.add("neg");
        oneOperandCommands.add("not");
        oneOperandCommands.add("push");
        oneOperandCommands.add("pop");
        oneOperandCommands.add("idiv");
   }

    public Generator(AbstractSyntaxTree input) {
        this.input = input;
    }

    protected abstract void write(String command, String dest, String src) throws IOException;
    protected abstract void write(String command, String dest) throws IOException;
    protected abstract void write(String command) throws IOException;

    protected void writeLine(String line) throws IOException {
        output.write(line);
        output.write("\n");
    }

    public boolean generate(String fileName) {
        try {
            output = new BufferedWriter(new FileWriter(fileName));
            // find main method
            Class c = (Class)(input.children[0]);
            Method m = null;
            try {
                m = (Method)(c.getChildren()[c.getMainIndex()]);
            } catch (Exception e) {
                return notifyError("Only classes with main methods are currently supported.");
            } 
            // write main header
            // write method's Block
            writeLine("global _main\nsection .text\n_main:");
            Block b = null;
            try {
                b = (Block)(m.getChildren()[0]);
            } catch (Exception e) {
                return notifyError("No body found for main method.");
            }
            Statement s = null;
            try {
                s = (Statement)(b.getChildren()[0]);
            } catch (Exception e) {
                return notifyError("No body found for main method.");
            }
            if (!processExpression((Expression)(s.getChildren()[0]))) {
                return false;
            }
            write("ret");
            output.close();
        } catch (IOException e) {
            return notifyError("Failed to open file: " + fileName + " for reading.");
        }
        return true;
    }

    private boolean processExpression(Expression exp) throws IOException {
        System.out.println(exp.getReturnType());
        switch (exp.getType()) {
            case PARENS:
                // TODO: Handle casting
                // TODO: Handle function params
                processExpression((Expression)(exp.getChildren()[0]));
                break;
            case INT_LITERAL:
                write("mov", "eax", exp.getValue());
                break;
            case UNARY:
                if (!processExpression((Expression)(exp.getChildren()[0]))) {
                    return false;
                }
                switch (exp.getValue()) {
                    case "~":
                        if (!exp.getReturnType().equals("int")) {
                            return notifyError("Unary operator: ~ not valid on type: " + exp.getReturnType() + " on line TODO");
                        }
                        write("not", "eax");
                        break;
                    case "-":
                        // TODO: HashSet of numeric types instead of manual checking
                        if (!exp.getReturnType().equals("int") && !exp.getReturnType().equals("float")) {
                            return notifyError("Unary operator: - not valid on type: " + exp.getReturnType() + " on line TODO");
                        }
                        write("neg", "eax");
                        break;
                    case "!":
                        if (!exp.getReturnType().equals("boolean")) {
                            return notifyError("Unary operator: ! not valid on type: " + exp.getReturnType() + " on line TODO");
                        }
                        // TODO: Handle !
                        break;
                }
                break;
            case BINARY:
                Expression e1 = (Expression)(exp.getChildren()[0]);
                Expression e2 = (Expression)(exp.getChildren()[1]);
                if (!processExpression(e2)) {
                    return false;
                }
                write("push", "rax");
                if (!processExpression(e1)) {
                    return false;
                }
                write("pop", "rcx");
                switch (exp.getValue()) {
                    case "+":
                        write("add", "eax", "ecx");
                        break;
                    case "-":
                        write("sub", "eax", "ecx");
                        break;
                    case "*":
                        write("imul", "eax", "ecx");
                        break;
                    case "/":
                        write("mov", "edx", "0");
                        write("idiv", "rcx");
                        break;
                    case "%":
                        write("mov", "edx", "0");
                        write("idiv", "rcx");
                        write("mov", "eax", "edx");
                        break;
                }
        }
        return true;
    }

    private boolean notifyError(String s) {
        System.out.println("Error: " + s);
        return false;
    }

}

