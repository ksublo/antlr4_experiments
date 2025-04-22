import java.util.*;
import java.io.*;

public class StackInterpreter {

    private final Deque<Object> stack = new ArrayDeque<>();
    private final Map<String, Object> memory = new HashMap<>();

    public void interpret(List<String> instructions) {
        Map<String, Integer> labelMap = new HashMap<>();
        for (int i = 0; i < instructions.size(); i++) {
            String[] parts = instructions.get(i).trim().split(" ");
            if (parts.length >= 2 && parts[0].equals("LABEL")) {
                labelMap.put(parts[1], i);
            }
        }

        for (int pc = 0; pc < instructions.size(); pc++) {
            String line = instructions.get(pc);
            if (line.isBlank()) continue;
            String[] parts = line.trim().split(" ");
            if (parts.length < 1) continue;

            switch (parts[0]) {
                case "PUSH" -> {
                    if (parts.length < 3) throw new RuntimeException("Invalid PUSH format: " + line);
                    String type = parts[1];

                    switch (type) {
                        case "I" -> stack.push(Integer.parseInt(parts[2]));
                        case "F" -> stack.push(Float.parseFloat(parts[2]));
                        case "B" -> stack.push(Boolean.parseBoolean(parts[2]) ? 1 : 0);
                        case "S" -> {
                            StringBuilder sb = new StringBuilder();
                            for (int i = 2; i < parts.length; i++) {
                                if (i > 2) sb.append(" ");
                                sb.append(parts[i]);
                            }
                            stack.push(sb.toString());
                        }
                        default -> throw new RuntimeException("Unsupported PUSH type: " + type);
                    }
                }

                case "LOAD" -> {
                    if (parts.length < 2) throw new RuntimeException("Invalid LOAD format: " + line);
                    String varName = parts[1];
                    if (!memory.containsKey(varName)) throw new RuntimeException("Variable not found: " + varName);
                    stack.push(memory.get(varName));
                }

                case "SAVE" -> {
                    if (parts.length == 2) {
                        String varName = parts[1];
                        if (stack.isEmpty()) throw new RuntimeException("Stack underflow on SAVE: " + line);
                        Object value = stack.pop();
                        if (value instanceof Integer || value instanceof Float || value instanceof String || value instanceof Boolean) {
                            memory.put(varName, value);
                        } else {
                            throw new RuntimeException("Unsupported inferred SAVE type for: " + varName);
                        }
                    } else if (parts.length == 3) {
                        String type = parts[1];
                        String varName = parts[2];
                        Object value = stack.pop();

                        switch (type) {
                            case "I" -> memory.put(varName, ((Number) value).intValue());
                            case "F" -> memory.put(varName, ((Number) value).floatValue());
                            case "B" -> memory.put(varName, ((Number) value).intValue() == 0 ? 0 : 1);
                            case "S" -> memory.put(varName, value.toString());
                            default -> throw new RuntimeException("Unsupported SAVE type: " + type);
                        }
                    } else {
                        throw new RuntimeException("Invalid SAVE format: " + line);
                    }
                }

                case "PRINT" -> {
                    if (parts.length < 2) throw new RuntimeException("Invalid PRINT format: " + line);
                    String type = parts[1];
                    Object value = stack.pop();

                    switch (type) {
                        case "I" -> System.out.println(((Number) value).intValue());
                        case "F" -> System.out.println(((Number) value).floatValue());
                        case "B" -> System.out.println(((Integer) value) == 1 ? "true" : "false");
                        case "S" -> System.out.println(value.toString());
                        default -> throw new RuntimeException("Unsupported PRINT type: " + type);
                    }
                }

                case "ADD", "SUB", "MUL", "DIV" -> {
                    if (stack.size() < 2) {
                        throw new RuntimeException("Not enough values on stack for: " + line);
                    }
                    Object b = stack.pop();
                    Object a = stack.pop();
                    boolean isFloat = a instanceof Float || b instanceof Float;
                    float fa = ((Number) a).floatValue();
                    float fb = ((Number) b).floatValue();
                    float result = switch (parts[0]) {
                        case "ADD" -> fa + fb;
                        case "SUB" -> fa - fb;
                        case "MUL" -> fa * fb;
                        case "DIV" -> {
                            if (fb == 0) throw new RuntimeException("Division by zero");
                            yield fa / fb;
                        }
                        default -> throw new RuntimeException("Unknown operator");
                    };
                    stack.push(isFloat ? result : (int) result);
                }

                case "MOD" -> {
                    if (stack.size() < 2) {
                        throw new RuntimeException("Not enough values on stack for: " + line);
                    }

                    Object b = stack.pop();
                    Object a = stack.pop();

                    if (a instanceof Float) a = ((Float) a).intValue();
                    if (b instanceof Float) b = ((Float) b).intValue();

                    if (!(a instanceof Integer) || !(b instanceof Integer)) {
                        throw new RuntimeException("MOD supports only integers");
                    }

                    stack.push((Integer) a % (Integer) b);
                }

                case "CMPEQ" -> {
                    Object b = stack.pop();
                    Object a = stack.pop();
                    stack.push(Objects.equals(a, b) ? 1 : 0);
                }

                case "CMPNE" -> {
                    Object b = stack.pop();
                    Object a = stack.pop();
                    stack.push(!Objects.equals(a, b) ? 1 : 0);
                }

                case "CMPLT" -> {
                    Number b = (Number) stack.pop();
                    Number a = (Number) stack.pop();
                    stack.push(a.doubleValue() < b.doubleValue() ? 1 : 0);
                }

                case "CMPGT" -> {
                    Number b = (Number) stack.pop();
                    Number a = (Number) stack.pop();
                    stack.push(a.doubleValue() > b.doubleValue() ? 1 : 0);
                }

                case "JMP" -> {
                    String label = parts[1];
                    if (!labelMap.containsKey(label)) throw new RuntimeException("Unknown label: " + label);
                    pc = labelMap.get(label);
                    continue;
                }

                case "JZ" -> {
                    String label = parts[1];
                    Object condition = stack.pop();
                    boolean jump = condition instanceof Integer && ((Integer) condition) == 0;
                    if (jump) {
                        if (!labelMap.containsKey(label)) throw new RuntimeException("Unknown label: " + label);
                        pc = labelMap.get(label);
                        continue;
                    }
                }

                case "LABEL" -> {
                    continue;
                }

                default -> throw new RuntimeException("Unknown instruction: " + line);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        File file = new File("input.txt");

        if (!file.exists()) {
            System.err.println("File not found: input.txt");
            return;
        }

        List<String> input = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                input.add(line);
            }
        }

        StackInterpreter vm = new StackInterpreter();
        vm.interpret(input);
    }
}
