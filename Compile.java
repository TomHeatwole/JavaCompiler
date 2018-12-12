import java.util.Scanner;
import java.io.File;


public class Compile {

    public static void main (String[] args) {
        if (args.length == 0) {
            System.out.println("Invalid number of parameters.");
            System.out.println("Usage: java Compile <name_of_java_file>");
            return;
        }
        Scanner readFile;
        try {
            readFile = new Scanner(new File(args[0]));
        } catch (Exception e) {
            System.out.println("File: " + args[0] + " not found.");
            return;
        }
   }

}
