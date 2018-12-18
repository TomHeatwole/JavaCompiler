import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Generator {

    private AbstractSyntaxTree program;

    public Generator(AbstractSyntaxTree program) {
        this.program = program;
    }

    public boolean generate(String fileName) {
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(fileName));
            writer.write("LOL");
            writer.write("LOL");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
       return true;
    }
}

