(defn createOperation [func]
  (fn [& args]
    (fn [var] (apply func (mapv (fn [a] (a var)) args)))))

(defn constant [value] (constantly value))
(defn variable [arg] (fn [vars] (vars arg)))

(defn divide-imp [& args] (reduce #(/ %1 (double %2)) args))
(defn sumexp-imp [& args]
  (apply + (mapv #(Math/exp %) args)))
(defn softmax-imp [& args]
  (/ (Math/exp (first args)) (apply sumexp-imp args)))

(def add (createOperation +))
(def subtract (createOperation -))
(def multiply (createOperation *))
(def divide (createOperation divide-imp))
(def negate (createOperation #(- %)))
(def sumexp (createOperation sumexp-imp))
(def softmax (createOperation softmax-imp))

(def FUNCTIONAL_OPERATIONS
  {'+       add
   '-       subtract
   '*       multiply
   '/       divide
   'negate  negate
   'sumexp  sumexp
   'softmax softmax
   })

(defn proto-get [obj key]
  (cond
    (contains? obj key) (obj key)
    (contains? obj :prototype) (proto-get (obj :prototype) key)
    :else nil))

(defn proto-call [this key & args]
  (apply (proto-get this key) this args))

(defn field [key]
  (fn [this] (proto-get this key)))

(defn method [key]
  (fn [this & args] (apply proto-call this key args)))

(defn constructor [ctor prototype]
  (fn [& args] (apply ctor {:prototype prototype} args)))

(def _value (field :value))
(declare ZERO)

(defn const-to-string [this] (format "%.1f" (_value this)))

(def constant-prototype
  {:evaluate      (fn [this vars] (_value this))
   :toString      const-to-string
   :toStringInfix const-to-string
   :diff          (fn [this variable] ZERO)})

(defn constant-ctor [this value]
  (assoc this
    :value value))

(def Constant (constructor constant-ctor constant-prototype))

(def ZERO (Constant 0))

(def ONE (Constant 1))

(def _var (field :var))

(defn var-to-string [this] (str (_var this)))

(def variable-prototype
  {:evaluate      (fn [this vars] (vars (_var this)))
   :toString      var-to-string
   :toStringInfix var-to-string
   :diff          (fn [this variable] (if (= (_var this) variable) ONE ZERO))})

(defn variable-ctor [this var]
  (assoc this
    :var var))

(def Variable (constructor variable-ctor variable-prototype))

(def _func (field :func))
(def _args (field :args))
(def _arg (field :arg))
(def _symbol (field :symbol))
(def _diff (field :diff-op))

(def evaluate (method :evaluate))
(def toString (method :toString))
(def toStringInfix (method :toStringInfix))
(def diff (method :diff))

(def operation-prototype
  {:evaluate      (fn [this vars] (apply (_func this) (mapv (fn [x] (evaluate x vars)) (_args this))))
   :toString      (fn [this] (str "(" (_symbol this) " " (clojure.string/join " " (mapv (fn [x] (toString x)) (_args this))) ")"))
   :toStringInfix (fn [this] (str "(" (clojure.string/join (str " " (_symbol this) " ") (mapv (fn [x] (toStringInfix x)) (_args this))) ")"))
   :diff          (fn [this variable] ((_diff this) (mapv #(diff % variable) (_args this)) (_args this)))})

(def unary-prototype
  {:evaluate      (fn [this vars] ((_func this) (evaluate (_arg this) vars)))
   :toString      (fn [this] (str "(" (_symbol this) " " (clojure.string/join " " (toString (_arg this))) ")"))
   :toStringInfix (fn [this] (str (_symbol this) "(" (toStringInfix (_arg this)) ")"))
   :diff          (fn [this variable] ((_diff this) (diff (_arg this) variable) (_arg this)))})

(defn operation-ctor [this & args]
  (assoc this
    :args args))

(defn unary-ctor [this arg]
  (assoc this
    :arg arg))

(defn create [ctor proto func symbol diff]
  (constructor ctor (assoc proto :func func :symbol symbol :diff-op diff)))

(def create-operation (partial create operation-ctor operation-prototype))
(def create-unary (partial create unary-ctor unary-prototype))


(def Add (create-operation + '+ (fn [dargs args] (apply Add dargs))))

(def Subtract (create-operation - '- (fn [dargs args] (apply Subtract dargs))))

(declare Multiply)

(defn diff-mul [dargs args]
    (first (reduce
             (fn [[a' b] [a b']] [(Add (Multiply a' b') (Multiply b a)) (Multiply b b')])
             [ZERO ONE]
             (mapv vector dargs args))))

(def Multiply (create-operation * '* diff-mul))

(declare Divide)
(defn diff-div [[fdargs & rdargs] [fargs & rargs]]
  (let [divisor (apply Multiply rargs) ddivisor (diff-mul rdargs rargs)]
    (Divide (Subtract (Multiply fdargs divisor) (Multiply fargs ddivisor)) (Multiply divisor divisor))))

(def Divide (create-operation divide-imp '/ diff-div))

(def Negate (create-unary #(- %) 'negate (fn [darg arg] (Negate darg))))

(declare Exp)
(defn diff-exp [darg arg] (Multiply (Exp arg) darg))

(def Exp (create-unary (fn [a] (Math/exp a)) 'exp diff-exp))

(declare Sumexp)
(defn diff-sumexp [dargs args] (apply Add (mapv diff-exp dargs args)))

(def Sumexp (create-operation sumexp-imp 'sumexp diff-sumexp))

(defn diff-softmax [dargs args] (diff-div [(diff-exp (first dargs) (first args)) (diff-sumexp dargs args)]
                                          [(Exp (first args)) (apply Sumexp args)]))
(def Softmax (create-operation softmax-imp 'softmax diff-softmax))

(defn bitwise-operation [func]
  (fn [a b] (Double/longBitsToDouble (func (Double/doubleToLongBits a) (Double/doubleToLongBits b)))))

(def And (create-operation (bitwise-operation bit-and) '& nil))

(def Or (create-operation (bitwise-operation bit-or) '| nil))

(def Xor (create-operation (bitwise-operation bit-xor) "^" nil))

(def Impl (create-operation (bitwise-operation (fn [a b] (bit-or (bit-not a) b))) '=> nil))

(def Iff (create-operation (bitwise-operation (fn [a b] (bit-not (bit-xor a b)))) '<=> nil))

(def OBJECT_OPERATIONS
  {'+       Add
   '-       Subtract
   '*       Multiply
   '/       Divide
   'negate  Negate
   'sumexp  Sumexp
   'softmax Softmax
   '&       And
   '|       Or
   (symbol "^")       Xor
   '=>      Impl
   '<=>     Iff
   })

(defn parse [cnst vr operations expression]
  (cond
    (number? expression) (cnst expression)
    (list? expression) (apply (operations (first expression)) (mapv (partial parse cnst vr operations) (rest expression)))
    :else (vr (str expression))))

(defn make-parse [cnst vr operations expression]
  (parse cnst vr operations (read-string expression)))

(def parseObject (partial make-parse Constant Variable OBJECT_OPERATIONS))

(def parseFunction (partial make-parse constant variable FUNCTIONAL_OPERATIONS))

; HW 12

(defn -return [value tail] {:value value :tail tail})
(def -valid? boolean)
(def -value :value)
(def -tail :tail)

(defn _show [result]
  (if (-valid? result) (str "-> " (pr-str (-value result)) " | " (pr-str (apply str (-tail result)))) "!"))
(defn tabulate [parser inputs]
  (run! (fn [input] (printf "    %-10s %s\n" (pr-str input) (_show (parser input)))) inputs))

(defn _empty [value] (partial -return value))

(defn _char [p]
  (fn [[c & cs]]
    (if (and c (p c)) (-return c cs))))

(defn _map [f result]
  (if (-valid? result)
    (-return (f (-value result)) (-tail result))))

(defn _combine [f a b]
  (fn [str]
    (let [ar ((force a) str)]
      (if (-valid? ar)
        (_map (partial f (-value ar))
              ((force b) (-tail ar)))))))

(defn _either [a b]
  (fn [str]
    (let [ar ((force a) str)]
      (if (-valid? ar) ar ((force b) str)))))

(defn _parser [p]
  (fn [input] (-value ((_combine (fn [v _] v) p (_char #{\u0000})) (str input \u0000)))))


(defn +char [chars] (_char (set chars)))
(defn +char-not [chars] (_char (comp not (set chars))))
(defn +map [f parser] (comp (partial _map f) parser))
(def +parser _parser)
(def +ignore (partial +map (constantly 'ignore)))

(defn iconj [coll value]
  (if (= value 'ignore) coll (conj coll value)))
(defn +seq [& ps]
  (reduce (partial _combine iconj) (_empty []) ps))

(defn +seqf [f & ps] (+map (partial apply f) (apply +seq ps)))
(defn +seqn [n & ps] (apply +seqf (fn [& vs] (nth vs n)) ps))

(defn +or [p & ps]
  (reduce _either p ps))
(defn +opt [p]
  (+or p (_empty nil)))
(defn +star [p]
  (letfn [(rec [] (+or (+seqf cons p (delay (rec))) (_empty ())))] (rec)))
(defn +plus [p] (+seqf cons p (+star p)))
(defn +str [p] (+map (partial apply str) p))

(def *space (+char " \t\n\r"))
(def *ws (+ignore (+star *space)))

(def *digit (+char "0123456789"))
(def *number (+map read-string (+str (+seqf concat
                                            (+seqf cons (+opt (+char "-")) (+plus *digit))
                                            (+opt (+seqf cons (+char ".") (+plus *digit)))))))
(def *const (+map Constant *number))

(def *variable (+map (comp Variable str) (+char "xyz")))

(defn *make-operation [arg] (apply +seqf (comp symbol str) (mapv +char (mapv str (seq arg)))))

(def *negate (*make-operation "negate"))

(declare *iff)

(defn make-object [right input]
  (let [data (if right
               (reverse input)
               input)
        rec (fn [exp lst]
              (let [operation (OBJECT_OPERATIONS ((comp symbol str) (first lst)))]
                (if (empty? lst)
                  exp
                  (recur (if right
                           (operation (second lst) exp)
                           (operation exp (second lst))) (take-last (- (count lst) 2) lst)))))]
    (rec (first data) (rest data))))

(def *primary (+seqn 0 *ws (+or
                             *const
                             *variable
                             (+seqn 1 (+char "(") *ws (delay *iff) *ws (+char ")"))
                             (+map Negate (+seqn 1 *negate (delay *primary)))) *ws))

(defn make-parse [right next parsers]
  (let [ops (mapv *make-operation parsers)]
    (+map (partial make-object right) (+seqf cons next (+map (partial apply concat) (+star (+seq (apply +or ops) next)))))))

(defn left-assoc [next & parsers] (make-parse false next parsers))
(defn right-assoc [next & parsers] (make-parse true next parsers))

(def *iff (reduce (fn [next [assoc & operations]]
                    (apply assoc next operations))
                  *primary
                  [[left-assoc "*" "/"]
                   [left-assoc "+" "-"]
                   [left-assoc "&"]
                   [left-assoc "|"]
                   [left-assoc "^"]
                   [right-assoc "=>"]
                   [left-assoc "<=>"]]))

(def parseObjectInfix
  (+parser *iff))

(println (evaluate (parseObjectInfix "x<=>y<=>z") {"z" 1.0, "x" 0.0, "y" 0.0}))
(println (toStringInfix (parseObjectInfix "      -1523787372.0/1632059489.0 ")))
(println (mapv vector [1 2 3] [1 2 3]))