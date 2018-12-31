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
                e.printStackTrace();
                System.out.println("Error: Only classes with main methods are currently supported.");
                return false;
            }
            write("mov", "eax", "rax");
            output.close();
        } catch (IOException e) {
            System.out.println("Error: Failed to open file: " + fileName + " for reading.");
            return false;
        }
        return true;
    }

    public abstract void write(String command, String dest, String src) throws IOException;
}

