import java.lang.StringBuilder;
import java.io.IOException;

public class GeneratorIntel extends Generator {

    public GeneratorIntel(AbstractSyntaxTree input) {
        super(input);
    }

    protected void write(String command, String dest, String src) throws IOException {
        if (noOperandCommands.contains(command)) { // TODO: All 0 and 1 param commands necessary
            output.write(command + "\n");
            return;
        }
        StringBuilder line = new StringBuilder(command);
        for (;line.length() < 8; line.append(' ')); // uniform spacing for dest, src
        line.append(dest);
        if (!oneOperandCommands.contains(command)) {
            line.append(", ");
            line.append(src);
        } 
        line.append('\n');
        output.write(line.toString());
    }
}
