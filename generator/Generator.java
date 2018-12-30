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
            write("mov", "eax", "rax");
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public abstract void write(String command, String dest, String src) throws IOException;
}

