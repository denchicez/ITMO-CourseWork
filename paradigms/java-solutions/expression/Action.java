package expression;
//

import java.util.Objects;

public abstract class Action implements UniteExpression {
    public final UniteExpression right;
    private final UniteExpression left;
    private final String act;
    protected int valueLeft;
    protected int valueRight;

    public Action(UniteExpression left, UniteExpression right, String act) {
        this.left = left;
        this.right = right;
        this.act = act;
    }

    protected int getAnswer(final int a, final int b) {
        return a + b;
    }

    public int evaluate(int x, int y, int z) {
        return getAnswer(left.evaluate(x, y, z), right.evaluate(x, y, z));
    }

    public int evaluate(int x) {
        return getAnswer(left.evaluate(x), right.evaluate(x));
    }

    public String toString() {
        String answer = "(" + left.toString() + " " + act + " " + right.toString() + ")";
        return answer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Action action = (Action) o;
        return Objects.equals(act, action.act) && Objects.equals(right, action.right)
                && Objects.equals(left, action.left);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right, act);
    }
}
