import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class Main {
    public static void main(String[] args) throws Exception {
        String inputText = """
                          int a,b;
                          a = b = 15;
                          a + b;
                          a % b;
                          float c;
                          c = a + b;
                          c + a;
                          a = c;
                          c + 1.1;
                          c % a;
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
            EvalVisitor visitor = new EvalVisitor();
            visitor.visit(tree);
        }

    }
}
