package expression.generic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class ExpressionParser<T> implements TripleParser<T> {
    public AbstractOperaions<T> op;


    public ExpressionParser(AbstractOperaions<T> op) {
        this.op = op;
    }

    public boolean priorityEquals(HashMap<String, Integer> signsPriority, String first, String second) {
        if (signsPriority.getOrDefault(first, -1) < signsPriority.getOrDefault(second, 0)) return false;
        return signsPriority.getOrDefault(second, 0) != 3; // unary must be first
    }

    public UniteExpression<T> parse(String express) {
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
        signsPriority.put("count", 3);
        for (int i = 0; i < express.length(); i++) {
            char symbol = express.charAt(i);
            if (Character.isDigit(symbol) || symbol == 'x' || symbol == 'y' || symbol == 'z') {
                String number_was;
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
                while (!stack.isEmpty() && priorityEquals(signsPriority, stack.peek(), symbol_str)) {
                    out.add(stack.pop());
                }
                stack.add(symbol_str);
                may_unary = true;
            } else if (symbol == 'c') {
                i++;
                last_was_digit = false;
                String symbol_str = "count";
                while (!stack.isEmpty() && priorityEquals(signsPriority, stack.peek(), symbol_str)) {
                    out.add(stack.pop());
                }
                stack.add(symbol_str);
                may_unary = true;
            } else if (symbol == 'm') {
                i++;
                last_was_digit = false;
                String symbol_str;
                if (express.charAt(i) == 'i' && express.charAt(++i) == 'n') symbol_str = "min";
                else symbol_str = "max";
                i += 1;
                while (!stack.isEmpty() && priorityEquals(signsPriority, stack.peek(), symbol_str)) {
                    out.add(stack.pop());
                }
                stack.add(symbol_str);
                may_unary = true;
            }
        }
        while (stack.size() != 0) {
            out.add(stack.pop());
        }
        Stack<UniteExpression<T>> answer = new Stack<>();
        for (String value : out) {
            if (!signsPriority.containsKey(value)) {
                try {
                    answer.add(new Const<>(op.parseNumber(value)));
                } catch (Exception exception) {
                    if (value.charAt(0) == '-') {
                        answer.add(new UnaryMinus<>(new Variable<>(value.substring(1)), op));
                    } else {
                        answer.add(new Variable<>(value));
                    }
                }
            } else {
                switch (value) {
                    case "+" -> {
                        UniteExpression<T> right = answer.pop();
                        UniteExpression<T> left = answer.pop();
                        answer.add(new Add<>(left, right, op));
                    }
                    case "-" -> {
                        UniteExpression<T> right = answer.pop();
                        UniteExpression<T> left = answer.pop();
                        answer.add(new Subtract<>(left, right, op));
                    }
                    case "/" -> {
                        UniteExpression<T> right = answer.pop();
                        UniteExpression<T> left = answer.pop();
                        answer.add(new Divide<T>(left, right, op));
                    }
                    case "*" -> {
                        UniteExpression<T> right = answer.pop();
                        UniteExpression<T> left = answer.pop();
                        answer.add(new Multiply<>(left, right, op));
                    }
                    case "count" -> {
                        UniteExpression<T> part = answer.pop();
                        answer.add(new Count<>(part, op));
                    }
                    case "min" -> {
                        UniteExpression<T> right = answer.pop();
                        UniteExpression<T> left = answer.pop();
                        answer.add(new Min<>(left, right, op));
                    }
                    case "max" -> {
                        UniteExpression<T> right = answer.pop();
                        UniteExpression<T> left = answer.pop();
                        answer.add(new Max<>(left, right, op));
                    }
                    default -> {
                        UniteExpression<T> part = answer.pop();
                        answer.add(new UnaryMinus<>(part, op));
                    }
                }
            }
        }
        return answer.get(0);
    }

}
