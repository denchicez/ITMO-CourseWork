package expression.exceptions;


import expression.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class ExpressionParser implements TripleParser {
    public boolean isVariable(char symbol) {
        if (symbol == 'x' || symbol == 'y' || symbol == 'z') return true;
        else return false;
    }

    public boolean isNegateVariable(String s) {
        if (s.length() >= 2 && s.charAt(1) != '-' && !isVariable(s.charAt(1))) return true;
        if (s.charAt(0) != '-' && !isVariable(s.charAt(0))) return true;
        return false;
    }

    public boolean isUnary(String operation) {
        if (operation.equals("$-") || operation.equals("l0") || operation.equals("t0")) return true;
        else return false;
    }

    public boolean priorityEquals(HashMap<String, Integer> signsPriority, String first, String second) {
        if (signsPriority.getOrDefault(first, -1) < signsPriority.getOrDefault(second, 0)) return false;
        if (signsPriority.getOrDefault(second, 0) == 3) return false; // unary must be first
        return true;
    }

    public boolean isNotSpaceAndBracket(char symbol) {
        if (!Character.isWhitespace(symbol) && symbol != ')' && symbol != '(') return true;
        else return false;
    }

    public TripleExpression parse(String express) {
        HashMap<String, Integer> signsPriority = new HashMap<>();
        signsPriority.put("min", 0);
        signsPriority.put("max", 0);
        signsPriority.put("-", 1);
        signsPriority.put("+", 1);
        signsPriority.put("*", 2);
        signsPriority.put("/", 2);
        signsPriority.put("$-", 3);
        signsPriority.put("l0", 3);
        signsPriority.put("t0", 3);
        HashMap<Character, Integer> signsWord = new HashMap<>();
        signsWord.put('m', 2); // min or max
        signsWord.put('t', 1); // t0
        signsWord.put('l', 1); // l0
        Stack<String> stack = new Stack<>();
        ArrayList<String> out = new ArrayList<>();
        boolean may_unary = true;
        boolean last_was_digit = false;
        for (int i = 0; i < express.length(); i++) {
            char symbol = express.charAt(i);
            String str_now = Character.toString(symbol);
            if (signsWord.containsKey(symbol)) { // must be max min
                if (i + signsWord.get(symbol) < express.length()) {
                    for (int j = 1; j <= signsWord.get(symbol); j++) {
                        i++;
                        str_now = str_now + express.charAt(i);
                    }
                    if (!signsPriority.containsKey(str_now)) throw new UnknownSymbol();
                    if (i + 1 >= express.length()) {
                        throw new NotCorrectOperation();
                    }
                    if ((symbol == 'l' || symbol == 't') && isNotSpaceAndBracket(express.charAt(i + 1)))
                        throw new NotCorrectOperation();
                    if (symbol == 'm') {
                        if (i - 3 < 0) throw new NotCorrectOperation();
                        if (isNotSpaceAndBracket(express.charAt(i - 3))) throw new NotCorrectOperation();
                    }
                } else {
                    throw new UnknownSymbol();
                }
            }
            if (Character.isDigit(symbol) || isVariable(symbol)) {
                String number_was = "";
                may_unary = false;
                if (last_was_digit) {
                    number_was = out.get(out.size() - 1);
                    out.remove(out.size() - 1);
                    str_now = number_was + str_now;
                }
                if (!stack.isEmpty() && stack.peek().equals("$-") && str_now.charAt(0) != '-') {
                    stack.pop();
                    str_now = "-" + str_now;
                }
                last_was_digit = true;
                out.add(str_now);
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
                    throw new IncorrectBrackets();
                }
                stack.pop();
            } else if (signsPriority.containsKey(str_now)) {
                last_was_digit = false;
                if (symbol == '-' && may_unary) {
                    str_now = "$-";
                }
                while (!stack.isEmpty() && priorityEquals(signsPriority, stack.peek(), str_now)) {
                    out.add(stack.pop());
                }
                stack.add(str_now);
                may_unary = true;
            } else if (Character.isWhitespace(symbol)) {
                last_was_digit = false;
            } else {
                throw new UnknownSymbol();
            }
        }
        while (stack.size() != 0) {
            out.add(stack.pop());
        }
        Stack<UniteExpression> answer = new Stack<>();
        for (int i = 0; i < out.size(); i++) {
            if (!signsPriority.containsKey(out.get(i))) {
                if (out.get(i).equals("(") || out.get(i).equals(")")) {
                    throw new IncorrectBrackets();
                }
                String s = out.get(i);
                if (isNegateVariable(s)) {
                    try {
                        answer.add(new Const(Integer.parseInt(out.get(i))));
                    } catch (OverflowException exception) {
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
                if (answer.isEmpty()) {
                    throw new NotEnoughOperation();
                }
                UniteExpression right = answer.pop();
                if (isUnary(operation)) {
                    if (operation.equals("$-")) answer.add(new CheckedNegate(right));
                    else if (operation.equals("t0")) answer.add(new ChildByte(right));
                    else answer.add(new AdultByte(right));
                } else {
                    if (answer.isEmpty()) {
                        throw new NotEnoughOperation();
                    }
                    UniteExpression left = answer.pop();
                    if (out.get(i).equals("+")) {
                        answer.add(new CheckedAdd(left, right));
                    } else if (out.get(i).equals("-")) {
                        answer.add(new CheckedSubtract(left, right));
                    } else if (out.get(i).equals("/")) {
                        answer.add(new CheckedDivide(left, right));
                    } else if (out.get(i).equals("*")) {
                        answer.add(new CheckedMultiply(left, right));
                    } else if (out.get(i).equals("min")) {
                        answer.add(new Min(left, right));
                    } else {
                        answer.add(new Max(left, right));
                    }
                }
            }
        }
        if (answer.size() != 1) {
            throw new NotEnoughOperation();
        }
        return answer.get(0);
    }
}
