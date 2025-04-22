import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.io.PrintWriter;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        String inputText = """
int i;
for (i = 0; i < 3; i = i + 1) {
  write i;
}
""";

        CharStream input = CharStreams.fromString(inputText);
        KSU_langLexer lexer = new KSU_langLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        KSU_langParser parser = new KSU_langParser(tokens);

        ErrorListener errorListener = new ErrorListener();
        parser.removeErrorListeners();
        parser.addErrorListener(errorListener);

        ParseTree tree = parser.prog();

        if (errorListener.hasError()) {
            System.err.println("Parsing failed. Exiting...");
            return;
        }

        TypeChecker checker = new TypeChecker();
        checker.visit(tree);

        if (checker.hasErrors()) {
            checker.printErrors();
            System.err.println("Type checking failed.");
        } else {
            EvalVisitor visitor = new EvalVisitor(checker.getSymbolTable());
            visitor.visit(tree);

            List<String> instructions = visitor.getInstructions();

            try (PrintWriter writer = new PrintWriter("input.txt")) {
                for (String instr : instructions) {
                    writer.println(instr);
                }
                System.out.println("Lab 9 output saved to input.txt (Lab 10 compatible)");
            } catch (Exception e) {
                System.err.println("Failed to write to input.txt");
                e.printStackTrace();
            }
        }
    }
}
