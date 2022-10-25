package expression.parser;


import expression.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class ExpressionParser implements TripleParser {
    public  ExpressionParser() {

    }
    public UniteExpression parse(String express) {
        HashMap<String, Integer> signsPriority = new HashMap<>();
        Stack<String> stack = new Stack<>();
        ArrayList<String> out = new ArrayList<>();
        boolean may_unary = true;
        boolean last_was_digit = false;
        signsPriority.put("-", 1);
        signsPriority.put("+", 1);
        signsPriority.put("*", 2);
        signsPriority.put("/", 2);
        signsPriority.put("$-", 3);
        signsPriority.put("l0", 3);
        signsPriority.put("t0", 3);
        for (int i = 0; i < express.length(); i++) {
            char symbol = express.charAt(i);
            if (Character.isDigit(symbol) || symbol == 'x' || symbol == 'y' || symbol == 'z') {
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
                while (!stack.peek().equals("(")) {
                    out.add(stack.pop());
                }
                stack.pop();
            } else if (signsPriority.containsKey(Character.toString(symbol))) {
                last_was_digit = false;
                String symbol_str = Character.toString(symbol);
                if (symbol == '-' && may_unary) {
                    symbol_str = "$-";
                }
                while (!stack.isEmpty() && signsPriority.getOrDefault(stack.peek(), 0) >= signsPriority.getOrDefault(symbol_str, 0) && signsPriority.getOrDefault(symbol_str, 0) != 3) {
                    out.add(stack.pop());
                }
                stack.add(symbol_str);
                may_unary = true;
            } else if (symbol == 'l') {
                i++;
                last_was_digit = false;
                String symbol_str = "l0";
                while (!stack.isEmpty() && (signsPriority.getOrDefault(stack.peek(), 0) >= signsPriority.getOrDefault(symbol_str, 1) && signsPriority.getOrDefault(symbol_str, 0) != 3)) {
                    out.add(stack.pop());
                }
                stack.add(symbol_str);
                may_unary = true;
            } else if (symbol == 't') {
                i++;
                last_was_digit = false;
                String symbol_str = "t0";
                while (!stack.isEmpty() && (signsPriority.getOrDefault(stack.peek(), 0) >= signsPriority.getOrDefault(symbol_str, 1) && signsPriority.getOrDefault(symbol_str, 0) != 3)) {
                    out.add(stack.pop());
                }
                stack.add(symbol_str);
                may_unary = true;
            } else {
                continue;
            }
        }
        while (stack.size() != 0) {
            out.add(stack.pop());
        }
        Stack<UniteExpression> answer = new Stack<>();
        for (int i = 0; i < out.size(); i++) {
            if (!signsPriority.containsKey(out.get(i))) {
                try {
                    answer.add(new Const(Integer.parseInt(out.get(i))));
                } catch (Exception exception) {
                    String s = out.get(i);
                    if (s.charAt(0) == '-') {
                        answer.add(new UnaryMinus(new Variable(s.substring(1))));
                    } else {
                        answer.add(new Variable(s));
                    }
                }
            } else {
                if (out.get(i).equals("+")) {
                    UniteExpression right = answer.pop();
                    UniteExpression left = answer.pop();
                    answer.add(new Add(left, right));
                } else if (out.get(i).equals("-")) {
                    UniteExpression right = answer.pop();
                    UniteExpression left = answer.pop();
                    answer.add(new Subtract(left, right));
                } else if (out.get(i).equals("/")) {
                    UniteExpression right = answer.pop();
                    UniteExpression left = answer.pop();
                    answer.add(new Divide(left, right));
                } else if (out.get(i).equals("*")) {
                    UniteExpression right = answer.pop();
                    UniteExpression left = answer.pop();
                    answer.add(new Multiply(left, right));
                } else if (out.get(i).equals("l0")) {
                    UniteExpression part = answer.pop();
                    answer.add(new AdultByte(part));
                } else if (out.get(i).equals("t0")) {
                    UniteExpression part = answer.pop();
                    answer.add(new ChildByte(part));
                } else {
                    UniteExpression part = answer.pop();
                    answer.add(new UnaryMinus(part));
                }
            }
        }


        return answer.get(0);
    }

}
