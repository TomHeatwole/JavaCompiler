import java.lang.StringBuilder;
import java.io.IOException;

public class GeneratorIntel extends Generator {

    public GeneratorIntel(AbstractSyntaxTree input) {
        super(input);
    }

    public void write(String command, String dest, String src) throws IOException {
        StringBuilder line = new StringBuilder(command);
        for (;line.length() < 8; line.append(' ')); // uniform spacing for dest, src
        line.append(dest);
        line.append(", ");
        line.append(src);
        line.append('\n');
        output.write(line.toString());
    }
}
