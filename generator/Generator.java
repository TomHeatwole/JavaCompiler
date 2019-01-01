import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public abstract class Generator {

    private AbstractSyntaxTree input;

    protected BufferedWriter output;

    public Generator(AbstractSyntaxTree input) {
        this.input = input;
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
                System.out.println("Error: Only classes with main methods are currently supported.");
                return false;
            } 
            // write main header
            // write method's Block
            writeLine("global _main\nsection .text\n_main:");
            Block b = null;
            try {
                b = (Block)(m.getChildren()[0]);
            } catch (Exception e) {
                System.out.println("Error: No body found for main method.");
                return false;
            }
            Statement s = null;
            try {
                s = (Statement)(b.getChildren()[0]);
            } catch (Exception e) {
                System.out.println("Error: body found for main method.");
                return false;
            }
            write("mov", "eax", "" + s.val);
            write("ret", "", "");
            output.close();
        } catch (IOException e) {
            System.out.println("Error: Failed to open file: " + fileName + " for reading.");
            return false;
        }
        return true;
    }

    protected abstract void write(String command, String dest, String src) throws IOException;

    protected void writeLine(String line) throws IOException {
        output.write(line);
        output.write("\n");
    }
}

