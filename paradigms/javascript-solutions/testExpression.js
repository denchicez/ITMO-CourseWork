const variable = (left) => function (x, y, z) {
    if (left === "x") return x;
    else if (left === "y") return z;
    else return z;
}
const add = (left, right) => function (x, y, z) {
    return left(x, y, z) + right(x, y, z);
}
const subtract = (left, right) => function (x, y, z) {
    return left(x, y, z) - right(x, y, z);
}
const multiply = (left, right) => function (x, y, z) {
    return left(x, y, z) * right(x, y, z);
}
const divide = (left, right) => function (x, y, z) {
    return left(x, y, z) / right(x, y, z);
}
const cnst = a => function (x, y, z) {
    return a;
}
const expr = add(
    subtract(
        multiply(
            cnst(2),
            multiply(
                variable('x'),
                variable('x')
            )
        ),
        multiply(cnst(2), variable('x'))
    ),
    cnst(1)
);
for (let i = 0; i <= 10; i++) {
    println(expr(i, 0, 0));
}