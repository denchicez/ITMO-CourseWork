package expression;

import expression.exceptions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class Main {
    public static HashMap<Character, Integer> signsPriority = new HashMap<>();

    public static void main(String[] args) {
        //Scanner scanner = new Scanner(System.in);
        //x = scanner.nextInt();
        // x^2-2x+1
        //int answer = new Add(new Subtract(
        //        new Multiply(
        //                new Variable("x"),
        //                new Variable("x")
        //        ),
        //        new Multiply(
        //                new Const(2),
        //                new Variable("x")
        //        )
        //), new Const(1)).evaluate(x, 1, 1);
        UniteExpression answer = parse("+");
        // need -> -((-(z) / (z + y)))
        System.err.println(answer.toString());
        System.err.println(answer.toMiniString());
        System.err.println(answer.evaluate(0));
    }

    public static boolean isVariable(char symbol) {
        if (symbol == 'x' || symbol == 'y' || symbol == 'z') return true;
        else return false;
    }

    public static boolean isUnary(String operation) {
        if (operation.equals("$-") || operation.equals("l0") || operation.equals("t0")) return true;
        else return false;
    }

    public static UniteExpression parse(String express) {
        System.err.println(express);
        HashMap<String, Integer> signsPriority = new HashMap<>();
        Stack<String> stack = new Stack<>();
        ArrayList<String> out = new ArrayList<>();
        boolean may_unary = true;
        boolean last_was_digit = false;
        signsPriority.put("min", 0);
        signsPriority.put("max", 0);
        signsPriority.put("-", 1);
        signsPriority.put("+", 1);
        signsPriority.put("*", 2);
        signsPriority.put("/", 2);
        signsPriority.put("$-", 3);
        signsPriority.put("l0", 3);
        signsPriority.put("t0", 3);
        signsPriority.put("m", 666);
        for (int i = 0; i < express.length(); i++) {
            char symbol = express.charAt(i);
            if (Character.isDigit(symbol) || isVariable(symbol)) {
                String number_was = "";
                String number_now = Character.toString(symbol);
                may_unary = false;
                if (last_was_digit) {
                    number_was = out.get(out.size() - 1);
                    out.remove(out.size() - 1);
                    number_now = number_was + number_now;
                }
                if (!stack.isEmpty() && stack.peek().equals("$-") && number_now.charAt(0) != '-') {
                    stack.pop();
                    number_now = "-" + number_now;
                }
                last_was_digit = true;
                out.add(number_now);
            } else if (symbol == '(') {
                may_unary = true;
                last_was_digit = false;
                stack.push("(");
            } else if (symbol == ')') {
                may_unary = false;
                last_was_digit = false;
                boolean found_end = false;
                while (!stack.isEmpty() && !stack.peek().equals("(")) {
                    out.add(stack.pop());
                }
                if (!stack.isEmpty() && stack.peek().equals("(")) found_end = true;
                if (!found_end) {
                    throw new OverflowException();
                }
                stack.pop();
            } else if (signsPriority.containsKey(Character.toString(symbol))) {
                last_was_digit = false;
                String symbol_str = Character.toString(symbol);
                if (symbol == '-' && may_unary) {
                    symbol_str = "$-";
                }
                if (symbol == 'm') {
                    i++;
                    symbol_str = symbol_str + express.charAt(i);
                    i++;
                    symbol_str = symbol_str + express.charAt(i);
                    if (i + 1 < express.length() && express.charAt(i + 1) != '(' && express.charAt(i + 1) != ' ') {
                        throw new OverflowException();
                    }
                }
                while (!stack.isEmpty() && signsPriority.getOrDefault(stack.peek(), -1) >= signsPriority.getOrDefault(symbol_str, 0) && signsPriority.getOrDefault(symbol_str, 0) != 3) {
                    out.add(stack.pop());
                }
                stack.add(symbol_str);
                may_unary = true;
            } else if (symbol == 'l') {
                i++;
                if (i + 1 < express.length() && express.charAt(i + 1) != '(' && express.charAt(i + 1) != ' ') {
                    throw new OverflowException();
                }
                last_was_digit = false;
                String symbol_str = "l0";
                while (!stack.isEmpty() && (signsPriority.getOrDefault(stack.peek(), 0) >= signsPriority.getOrDefault(symbol_str, 1) && signsPriority.getOrDefault(symbol_str, 0) != 3)) {
                    out.add(stack.pop());
                }
                stack.add(symbol_str);
                may_unary = true;
            } else if (symbol == 't') {
                i++;
                if (i + 1 < express.length() && express.charAt(i + 1) != '(' && express.charAt(i + 1) != ' ') {
                    throw new OverflowException();
                }
                last_was_digit = false;
                String symbol_str = "t0";
                while (!stack.isEmpty() && (signsPriority.getOrDefault(stack.peek(), 0) >= signsPriority.getOrDefault(symbol_str, 1) && signsPriority.getOrDefault(symbol_str, 0) != 3)) {
                    out.add(stack.pop());
                }
                stack.add(symbol_str);
                may_unary = true;
            } else if (Character.isWhitespace(symbol)) {
                last_was_digit = false;
            } else {
                throw new OverflowException();
            }
        }
        while (stack.size() != 0) {
            out.add(stack.pop());
        }
        Stack<UniteExpression> answer = new Stack<>();
        for (int i = 0; i < out.size(); i++) {
            if (!signsPriority.containsKey(out.get(i))) {
                if (out.get(i).equals("(") || out.get(i).equals(")")) {
                    throw new OverflowException();
                }
                String s = out.get(i);
                if (((s.length() >= 2 && s.charAt(1) != '-' && !isVariable(s.charAt(1))) || (s.charAt(0) != '-' && !isVariable(s.charAt(0))))) {
                    try {
                        answer.add(new Const(Integer.parseInt(out.get(i))));
                    } catch (Exception exception) {
                        throw new OverflowException();
                    }
                } else {
                    if (s.charAt(0) == '-') {
                        answer.add(new CheckedNegate(new Variable(s.substring(1))));
                    } else {
                        answer.add(new Variable(s));
                    }
                }
            } else {
                String operation = out.get(i);
                UniteExpression right = answer.pop();
                if (isUnary(operation)) {
                    if (operation.equals("$-")) answer.add(new CheckedNegate(right));
                    else if (operation.equals("t0")) answer.add(new ChildByte(right));
                    else answer.add(new AdultByte(right));
                } else {
                    UniteExpression left = answer.pop();
                    if (out.get(i).equals("+")) {
                        answer.add(new CheckedAdd(left, right));
                    } else if (out.get(i).equals("-")) {
                        answer.add(new CheckedSubtract(left, right));
                    } else if (out.get(i).equals("/")) {
                        answer.add(new CheckedDivide(left, right));
                    } else {
                        answer.add(new CheckedMultiply(left, right));
                    }
                }
            }
        }
        if (answer.size() != 1) {
            throw new OverflowException();
        }

        return answer.get(0);
    }
}