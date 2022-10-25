// :NOTE: non-strict mode
const variable = (left) => function (x, y, z) {
    if (left === "x") return x;
    else if (left === "y") return y;
    else return z;
}
// :NOTE: copypaste
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
const negate = a => function (x, y, z) {
    return -a(x, y, z);
}
const cnst = a => function (x, y, z) {
    return a;
}
const pi = (x, y, z) => Math.PI
const e = (x, y, z) => Math.E