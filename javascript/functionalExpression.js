"use strict";

const VARIABLES = ['x', 'y', 'z'];

const makeOperation = f => (...args) => (...vars) => f(...args.map(arg => arg(...vars)));

const cnst = a => () => a;

const variable = a => {
    const j = VARIABLES.indexOf(a);
    return (...x) => x[j];
};

const add = makeOperation((a, b) => a + b);

const multiply = makeOperation((a, b) => a * b);

const subtract = makeOperation((a, b) => a - b);

const divide = makeOperation((a, b) => a / b);

const negate = makeOperation(a => -a);

const abs = makeOperation(Math.abs);

const iff = makeOperation((a, b, c) => a >= 0 ? b : c);

const one = cnst(1);

const two = cnst(2);

const OPERATIONS = {
    'negate' : [negate, 1],
    '+' : [add, 2],
    '-' : [subtract, 2],
    '*' : [multiply, 2],
    '/' : [divide, 2],
    'iff' : [iff, 3],
    'abs' : [abs, 1]
};

const CONSTANTS = {
    'one' : one,
    'two' : two
};

const VARS = {
    'x' : variable('x'),
    'y' : variable('y'),
    'z' : variable('z')
};

const parse = expression => {
    let toParse = expression.split(' ').filter(word => word.length > 0);
    let st = [];
    for (const token of toParse) {
        if (token in OPERATIONS) {
            const operation = OPERATIONS[token];
            st.push(operation[0](...st.splice(-operation[1])));
        } else if (token in VARS) {
            st.push(VARS[token]);
        } else if (token in CONSTANTS) {
            st.push(CONSTANTS[token]);
        } else {
            st.push(cnst(parseInt(token)));
        }
    }
    return st.pop();
};

let test = parse("x x 2 - * x * 1 +");
for (let i = 0; i <= 10; i++) {
    println(test(i));
}
