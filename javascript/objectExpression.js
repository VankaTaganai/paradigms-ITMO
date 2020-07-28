"use strict";

const VARIABLES = ['x', 'y', 'z'];
function Const(value) {
    this.getValue = function () {
        return value;
    }
}

Const.prototype.argsCount = 1;
Const.prototype.evaluate = function () { return this.getValue() };
Const.prototype.toString = function () { return this.getValue().toString() };
Const.prototype.diff = function () { return Const.ZERO };
Const.prototype.prefix = Const.prototype.toString;
Const.prototype.postfix = Const.prototype.toString;

Const.ZERO = new Const(0);
Const.ONE = new Const(1);
Const.TWO = new Const(2);

function Variable(name) {
    this.getName = function () {
        return name;
    }
}

Variable.prototype.argsCount = 1;
Variable.prototype.evaluate = function () { return arguments[VARIABLES.indexOf(this.getName())] };
Variable.prototype.toString = function () { return this.getName() };
Variable.prototype.diff = function (name) { return this.getName() === name ? Const.ONE : Const.ZERO };
Variable.prototype.prefix = Variable.prototype.toString;
Variable.prototype.postfix = Variable.prototype.toString;

function Operation(func, symbol, ...operands) {
    this.getOperands = function () { return operands };
    this.getSymbol = function () { return symbol };
    this.getFunc = function () { return func };
}

Operation.prototype.evaluate = function (...variables) {
    const result = this.getOperands().map(operand => operand.evaluate(...variables));
    return this.getFunc()(...result);
};

Operation.prototype.toString = function () {
    return this.getOperands().join(' ') + ' ' + this.getSymbol();
};

Operation.prototype.diff = function (name) {
    return this.makeDiff(name, ...this.getOperands());
};

Operation.prototype.prefix = function () {
    return "(" + this.getSymbol() + " " + this.getOperands().map(function (arg) {
        return arg.prefix();
    }).join(" ") + ")";
};

Operation.prototype.postfix = function () {
    return "(" + this.getOperands().map(function (arg) {
        return arg.postfix();
    }).join(" ") + " " + this.getSymbol() + ")";
};

function createOperation(func, symbol, diff) {
    const result = function (...operands) {
        Operation.call(this, func, symbol, ...operands);
    };
    result.prototype = Object.create(Operation.prototype);
    result.prototype.makeDiff = diff;
    result.argsCount = func.length;
    return result;
}

const Add = createOperation(
    (a, b) => a + b,
    '+',
    (name, a, b) => new Add(a.diff(name), b.diff(name))
);

const Subtract = createOperation(
    (a, b) => a - b,
    '-',
    (name, a, b) => new Subtract(a.diff(name), b.diff(name))
);

const Multiply = createOperation(
    (a, b) => a * b,
    '*',
    (name, a, b) => new Add(
        new Multiply(a.diff(name), b),
        new Multiply(a, b.diff(name))
    )
);

const Divide = createOperation(
    (a, b) => a / b,
    '/',
    (name, a, b) => new Divide(
        new Subtract(
            new Multiply(a.diff(name), b),
            new Multiply(a, b.diff(name))
        ),
        new Multiply(b, b)
    )
);

const Negate = createOperation(
    a => -a,
    'negate',
    (name, a) => new Negate(a.diff(name))
);

const Gauss = createOperation(
    (a, b, c, x) => (a * Math.exp(-(x - b) * (x - b) / (2 * c * c))),
    'gauss',
    function (name, a, b, c, x) {
        const sub = new Subtract(x, b);
        const den = new Multiply(new Multiply(Const.TWO, c), c);
        const div = new Negate(new Divide(new Multiply(sub, sub), den));
        return new Add(new Multiply(a.diff(name), new Gauss(Const.ONE, b, c, x)), new Multiply(this, div.diff(name)));
    }
);

const Mean = createOperation(
    (...args) => (args.reduce((a, b) => a + b, 0) / args.length),
    'mean',
    (name, ...args) => new Mean(...args.map(operand => operand.diff(name)))
);

const Var = createOperation(
    function (...args) {
        const res = args.reduce((a, b) => a + b, 0) / args.length;
        return args.reduce((a, b) => a + (b - res) * (b - res), 0) / args.length;
    },
    'var',
    function (name, ...args) {
        const left = new Multiply(Const.TWO, new Mean(...args.map(operand => new Multiply(operand, operand.diff(name)))));
        const right = new Multiply(new Multiply(Const.TWO, new Mean(...args).diff(name)), new Mean(...args));
        return new Subtract(left, right);
    }
);

const OPERATIONS = {
    'negate' : Negate,
    '+' : Add,
    '-' : Subtract,
    '*' : Multiply,
    '/' : Divide,
    'gauss' : Gauss,
    'mean': Mean,
    'var': Var
};

const VARS = {
    'x' : new Variable('x'),
    'y' : new Variable('y'),
    'z' : new Variable('z')
};

const CreateError = function (name, message) {
    const error = function (...args) {
        this.name = name;
        this.message = message(args);
    };
    error.prototype = Object.create(Error.prototype);
    error.prototype.constructor = error;
    return error;
};

const NoOperationError = CreateError("NoOperationError", index => "Not found operation by index: " + index);

const NoClosingBracketError = CreateError("NoClosingBracketError", index => "Expected closing bracket by index: " + index);

const UnexpectedEndError = CreateError("UnexpectedEndError", index => "Expected end or operation of expression by index: " + index);

const ArgumentsError = CreateError("ArgumentsError", index => "Wrong number of arguments for operation by index: " + index);

const UnexpectedTokenError = CreateError("UnexpectedTokenError", index => "Unexpected token by index: " + index);


const isNumber = function (expression) {
    if (expression.length === 0) {
        return false;
    }
    let i = 0;
    if (expression[i] === "-") {
        if (expression.length === 1) {
            return false;
        }
        i++;
    }
    for (; i < expression.length; i++) {
        if (expression[i] < "0" || expression[i] > "9") {
            return false;
        }
    }
    return true;
};

const ExpressionParser = function(expression, parse) {
    this.expression = expression;
    this.parse = parse;
    this.index = 0;
    this.token = "";
};

ExpressionParser.prototype.skipWhitespaces = function() {
    while (this.index < this.expression.length && this.expression[this.index] === " ") {
        this.index++;
    }
};

ExpressionParser.prototype.nextToken = function() {
    if (this.expression[this.index] === "(" || this.expression[this.index] === ")") {
        this.token = this.expression[this.index];
        this.index++;
    } else {
        let prevIndex = this.index;
        while (this.index < this.expression.length && this.expression[this.index] !== " " &&
        this.expression[this.index] !== "(" && this.expression[this.index] !== ")")
        {
            this.index++;
        }
        this.token = this.expression.slice(prevIndex, this.index);
    }
    this.skipWhitespaces();
};

ExpressionParser.prototype.parseArguments = function(expression) {
    let args = [];
    while (!(this.token in OPERATIONS) && this.token !== ")" && this.index < expression.length) {
        args.push(this.parse());
        this.nextToken();
    }
    return args;
};

ExpressionParser.prototype.getOperation = function () {
    const result = this.token;
    this.nextToken();
    return result;
};

const makeParse = function (expression, mode) {
    const parser = new ExpressionParser(expression, function () {
        if (this.token === "(") {
            this.nextToken();
            let args, operation, operationIndex;
            if (mode === "prefix") {
                operationIndex = this.index;
                operation = this.getOperation();
                args = this.parseArguments(expression);
            } else {
                args = this.parseArguments(expression);
                operationIndex = this.index;
                operation = this.getOperation();
            }
            if (!(operation in OPERATIONS)) {
                throw new NoOperationError(operationIndex);
            } else {
                if (args.length !== OPERATIONS[operation].argsCount && OPERATIONS[operation].argsCount !== 0) {
                    throw new ArgumentsError(this.index);
                }
                let result = new OPERATIONS[operation](...args);
                if (this.token !== ")") {
                    throw new NoClosingBracketError(this.index);
                }
                return result;
            }
        } else if (this.token in VARS) {
            return VARS[this.token];
        } else if (isNumber(this.token)){
            return new Const(parseInt(this.token));
        } else {
            throw new UnexpectedTokenError(this.index);
        }
    });

    parser.skipWhitespaces();
    parser.nextToken();
    let result = parser.parse();
    if (parser.index < parser.expression.length) {
        throw new UnexpectedEndError(parser.index);
    }
    return result;
};

const parsePrefix = (expression) => makeParse(expression, "prefix");

const  parsePostfix = (expression) => makeParse(expression, "postfix");

const parse = expression => {
    let toParse = expression.split(' ').filter(token => token.length > 0);
    let stack = [];
    for (const token of toParse) {
        if (token in OPERATIONS) {
            const operation = OPERATIONS[token];
            stack.push(new operation(...stack.splice(-operation.argsCount)));
        } else if (token in VARS) {
            stack.push(VARS[token]);
        } else {
            stack.push(new Const(parseInt(token)));
        }
    }
    return stack.pop();
};
