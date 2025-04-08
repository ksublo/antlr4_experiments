import org.antlr.v4.runtime.*;

public class ErrorListener extends BaseErrorListener {
    private boolean errorFound = false;

    public boolean hasError() {
        return errorFound;
    }

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer,
                            Object offendingSymbol,
                            int line,
                            int charPositionInLine,
                            String msg,
                            RecognitionException e) {
        errorFound = true;
        System.err.printf("Syntax error at line %d:%d - %s%n", line, charPositionInLine, msg);
    }
}

