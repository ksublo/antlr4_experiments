import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.util.logging.Logger;
import java.util.logging.Level;

public class REPL {
    private static final Logger logger = Logger.getLogger(REPL.class.getName());

    public static void main(String[] args) {
        EvalVisitor visitor = new EvalVisitor();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        logger.info("PLC Expr REPL – zadej výraz nebo přiřazení (Ctrl+C pro ukončení):");

        while (true) {
            try {
                System.out.print(">> "); // Keeping prompt via console
                String line = reader.readLine();

                if (line == null || line.trim().isEmpty()) {
                    continue;
                }

                CharStream input = CharStreams.fromString(line + "\n");
                KSU_langLexer lexer = new KSU_langLexer(input);
                CommonTokenStream tokens = new CommonTokenStream(lexer);
                KSU_langParser parser = new KSU_langParser(tokens);

                ParseTree tree = parser.stat();
                Object result = visitor.visit(tree);

                if (result != null) {
                    logger.info(result.toString());
                }

            } catch (Exception e) {
                logger.log(Level.SEVERE, "Chyba: " + e.getMessage(), e);
            }
        }
    }
}

