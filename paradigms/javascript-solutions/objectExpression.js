"use strict";
const toString = (sign, ...args) => {
    let str = "";
    for (const argNew of args) {
        str = str + argNew.toString() + " ";
    }
    return str + sign;
}
const prefix = (sign, ...args) => {
    let str = "(" + sign;
    for (const argNew of args) {
        str = str + " " + argNew.prefix();
    }
    return str + ")";
}
const evaluate = (operate, x, y, z, arg, ...args) => {
    let ans = arg.evaluate(x, y, z);
    for (const argNew of args) {
        ans = operate(ans, argNew.evaluate(x, y, z));
    }
    return ans;
}
const templateBinary = function (op, diff, sign, ...args) {
    return {
        evaluate: function (x, y, z) {
            return evaluate(op, x, y, z, ...args)
        },
        toString: function () {
            return toString(sign, ...args);
        },
        prefix: function () {
            return prefix(sign, ...args);
        },
        diff: function (v) {
            return diff(v, args[0], args[1]);
        }
    }
}

const templateUnary = function (op, diff, sign, ...args) {
    return {
        evaluate: function (x, y, z) {
            return op(args[0].evaluate(x, y, z))
        },
        toString: function () {
            return toString(sign, ...args);
        },
        prefix: function () {
            return prefix(sign, ...args);
        },
        diff: function (v) {
            return diff(v, args[0]);
        }
    }
}

function Variable(value) {
    return {
        evaluate: function (x, y, z) {
            if (value === 'x') return x;
            else if (value === 'y') return y;
            else return z;
        },
        toString: function () {
            return value;
        },
        prefix: function () {
            return value;
        },
        diff: function (v) {
            if (v === value) return new Const(1);
            else return new Const(0);
        }
    }
}

function Const(value) {
    return {
        evaluate: function (x, y, z) {
            return value
        },
        toString: function () {
            return String(value);
        },
        prefix: function () {
            return String(value);
        },
        diff: function (v) {
            return new Const(0);
        }
    }
}

function Add(...args) {
    return templateBinary((a, b) => a + b, (v, a, b) => new Add(a.diff(v), b.diff(v)), "+", ...args);
}

function Subtract(...args) {
    return templateBinary((a, b) => a - b, (v, a, b) => new Subtract(a.diff(v), b.diff(v)), "-", ...args);
}

function Multiply(...args) {
    return templateBinary((a, b) => a * b,
        (v, a, b) => new Add(
            new Multiply(a.diff(v), b),
            new Multiply(a, b.diff(v))
        ), "*", ...args);
}

function Divide(...args) {
    return templateBinary((a, b) => a / b, (v, a, b) => new Divide(
        new Subtract(
            new Multiply(a.diff(v), b),
            new Multiply(a, b.diff(v))
        ),
        new Multiply(b, b)
    ), "/", ...args);
}

function Min3(...args) {
    return templateBinary((a, b) => Math.min(a, b), (a, b) => Math.max(a, b), "min3", ...args);
}

function Max5(...args) {
    return templateBinary((a, b) => Math.max(a, b), (a, b) => Math.max(a, b), "max5", ...args);
}

function Negate(value) {
    return templateUnary((a) => -a, (v, a) => new Negate(a.diff(v)), "negate", value);
}

function Sinh(value) {
    return templateUnary((a) => Math.sinh(a), (v, a) => new Multiply(new Cosh(a), a.diff(v)), "sinh", value);
}

function Cosh(value) {
    return templateUnary((a) => Math.cosh(a), (v, a) => new Multiply(new Sinh(a), a.diff(v)), "cosh", value);
}

const parse = (express) => {
    const expressArr = express.split(' ')
    let n = expressArr.length
    let stack = []
    for (let i = 0; i < n; i++) {
        if (expressArr[i] === '') {
            continue;
        }
        if (expressArr[i] === 'negate') {
            let value = stack.pop();
            stack.push(new Negate(value));
        } else if (!isNaN(expressArr[i])) {
            stack.push(new Const(Number(expressArr[i])));
        } else if (expressArr[i] === "x" || expressArr[i] === 'y' || expressArr[i] === 'z' || expressArr[i] === "min3"
            || expressArr[i] === "max5") {
            if (expressArr[i] === 'min3') {
                let arrMin = []
                for (let i = 0; i < 3; i++) {
                    arrMin.push(stack.pop());
                }
                arrMin.reverse();
                stack.push(new Min3(...arrMin));
            } else if (expressArr[i] === 'max5') {
                let arrMax = []
                for (let i = 0; i < 5; i++) {
                    arrMax.push(stack.pop());
                }
                arrMax.reverse();
                stack.push(new Max5(...arrMax));
            } else stack.push(new Variable(expressArr[i]));
        } else if (expressArr[i] === "+" || expressArr[i] === '-' || expressArr[i] === '*' || expressArr[i] === '/') {
            let a = stack.pop();
            let b = stack.pop();
            if (expressArr[i] === '+') {
                stack.push(new Add(b, a));
            } else if (expressArr[i] === '-') {
                stack.push(new Subtract(b, a));
            } else if (expressArr[i] === '/') {
                stack.push(new Divide(b, a));
            } else {
                stack.push(new Multiply(b, a));
            }
        }
    }
    return stack.pop();
}
const errorMessages = function (error) {
    throw new Error(error);
}
const errorToMuch = function (error) {
    throw errorMessages("Bad arguments! " + error);
}
const unknownWord = function () {
    throw errorMessages("What's it?? I don't know this command");
}
const bracketUpdater = function (exp, counter) {
    if (exp === "(") counter++;
    if (exp === ")") counter--;
    return counter
}
const parseArray = function (expressArr) {
    if (expressArr === undefined) {
        errorMessages("get bad expression");
    }
    expressArr = expressArr.replaceAll('(', ' ( ')
    expressArr = expressArr.replaceAll(')', ' ) ')
    return expressArr.split(" ").filter(a => a !== '');
}
const parsePrefix = (express) => {
    let operate = new Map();
    operate.set("sinh", [(a) => new Sinh(parsePrefix(a)), 2]);
    operate.set("cosh", [(a) => new Cosh(parsePrefix(a)), 2]);
    operate.set("negate", [(a) => new Negate(parsePrefix(a)), 2]);
    operate.set("*", [(a, b) => new Multiply(parsePrefix(a), parsePrefix(b)), 3]);
    operate.set("/", [(a, b) => new Divide(parsePrefix(a), parsePrefix(b)), 3]);
    operate.set("-", [(a, b) => new Subtract(parsePrefix(a), parsePrefix(b)), 3]);
    operate.set("+", [(a, b) => new Add(parsePrefix(a), parsePrefix(b)), 3]);
    const OPERATIONS = ['+', '-', '/', '*', 'negate', 'sinh', 'cosh']
    if (typeof (express) === "object") return express;
    const expressArr = parseArray(express);
    let n = expressArr.length;
    let stack = []
    for (let i = 0; i < n; i++) {
        let exp = expressArr[i];
        if (exp === "(") {
            let checker = 0;
            for (let j = 1; i + j < n; j++) {
                if (OPERATIONS.indexOf(expressArr[i + j]) !== -1) {
                    checker = 1;
                    break
                }
                if (expressArr[i + j] !== ' ') {
                    checker = 0;
                    break;
                }
            }
            if (checker === 0) errorToMuch("not too much arguments for express");
            let openBr = 1
            let ans = "";
            i += 1
            exp = expressArr[i];
            openBr = bracketUpdater(exp, openBr);
            while (openBr !== 0) {
                ans = ans + " " + exp;
                i += 1
                if (i >= n) errorToMuch("not too much arguments for express");
                exp = expressArr[i];
                openBr = bracketUpdater(exp, openBr);
            }
            stack.push(parsePrefix(ans));
        } else stack.push(exp);
    }
    if (operate.has(stack[0])) {
        if (stack.length > operate.get(stack[0])[1]) errorToMuch("too much for " + stack[0]);
        let arrMax = []
        for (let k = 1; k < operate.get(stack[0])[1]; k++) {
            arrMax.push(stack[k]);
        }
        return operate.get(stack[0])[0](...arrMax);
    } else {
        if (stack.length > 1) errorToMuch("too much for const or variable");
        if (stack[0] === 'x' || stack[0] === 'y' || stack[0] === 'z') return new Variable(stack[0]);
        else if (!isNaN(stack[0])) return new Const(Number(stack[0]));
        if (typeof (stack[0]) !== "object") errorToMuch("not too much arguments for express");
        else return stack[0];
    }
}

