(defn binaryOperation [operation] (fn [left, right] (fn [input] (operation (left input) (right input)))))
(defn unaryOperation [operation] (fn [value] (fn [input] (operation (value input)))))

(defn constant [val] (fn [_] val))
(defn variable [val] (fn [input] (get input val)))
(def add (binaryOperation +))
(def subtract (binaryOperation -))
(def divide (binaryOperation (fn [left right] (/ left (double right)))))
(def multiply (binaryOperation *))
(def exp (unaryOperation (fn [value] (java.lang.Math/exp value))))
(def ln (unaryOperation (fn [value] (java.lang.Math/log value))))
(def negate (unaryOperation -))

(def operations {'+ add, '- subtract, 'negate negate, '* multiply, '/ divide, 'exp exp, 'ln ln})
(def variables {'x (variable "x"), 'y (variable "y"), 'z (variable "z")})

(defn parseFunction [expression]
  (cond
    (string? expression) (parseFunction (read-string expression))
    (contains? variables expression) (variables expression)
    (number? expression) (constant expression)
    :else (apply (operations (first expression)) (map parseFunction (rest expression)))
    )
  )



(defn parseFunction [expression]
  (cond
    (string? expression) (parseFunction (read-string expression))
    (contains? variables expression) (variables expression)
    (number? expression) (constant expression)
    :else (apply (operations (first expression)) (map parseFunction (rest expression)))
    )
  )
; THIS is NEXT HW
(declare Constant)
(declare Variable)
(declare Add)
(declare Subtract)
(declare Multiply)
(declare Divide)
(declare Negate)

(definterface Express
  (^Number evaluate [vars])
  (^String toString [])
  (diff [dx])
  )

(deftype AbstarctConstant [value]
  Express
  (evaluate [this vars] value)
  (toString [this] (format "%.1f" (double value)))
  (diff [this dx] (Constant 0))
  )
(defn Constant [value] (AbstarctConstant. value))


(deftype AbstarctVariable [value]
  Express
  (evaluate [this vars] (get vars (str value)))
  (toString [this] (str value))
  (diff [this dx] (cond
                    (identical? dx value) (Constant 1)
                    :else (Constant 0)
                    )
    )
  )

(defn Variable [value] (AbstarctVariable. value))

(deftype AbstractBinOp [left, right, op, sign, diff]
  Express
  (evaluate [this vars] (op (.evaluate left vars) (.evaluate right vars)))
  (toString [this] (format "(%s %s %s)" sign (.toString left) (.toString right)))
  (diff [this dx] (diff dx))
  )
(deftype AbstractUnaOp [left, op, sign, diff]
  Express
  (evaluate [this vars] (op (.evaluate left vars)))
  (toString [this] (format "(%s %s)" sign (.toString left)))
  (diff [this dx] (diff dx))
  )
(defn Add [left, right] (AbstractBinOp. left right + "+" (fn [dx] (Add (.diff left dx) (.diff right dx)))))
(defn Subtract [left, right] (AbstractBinOp. left right - "-" (fn [dx] (Subtract (.diff left dx) (.diff right dx)))))
(defn Multiply [left, right] (AbstractBinOp. left right * "*" (fn [dx] (Add (Multiply (.diff left dx) right) (Multiply left (.diff right dx))))))
(defn Divide [left, right] (AbstractBinOp. left right (fn [left right] (/ left (double right))) "/" (fn [dx] (Divide (Subtract (Multiply (.diff left dx) right) (Multiply left (.diff right dx))) (Multiply right right)))))
(defn Negate [left] (AbstractUnaOp. left - "negate" (fn [dx] (Negate (.diff left dx)))))
(defn Exp [left] (AbstractUnaOp. left (fn [value] (java.lang.Math/exp value)) "exp" ( fn [dx] (Multiply (Exp left) (.diff left dx ) ) )))
(defn Ln [left] (AbstractUnaOp. left (fn [value] (java.lang.Math/log value)) "ln" (fn [dx] (Divide (.diff left dx) left)) ))
(defn evaluate [express, vars] (.evaluate express vars))
(defn toString [express] (.toString express))
(defn diff [express, vars] (.diff express vars))
(def operationsObj {'+ Add, '- Subtract, '* Multiply, '/ Divide, 'negate Negate, 'exp Exp, 'ln Ln})
(def variablesObj {'x (Variable "x"), 'y (Variable "y"), 'z (Variable "z")})
(defn parseObject [expression]
  (cond
    (string? expression) (parseObject (read-string expression))
    (contains? variablesObj expression) (Variable expression)
    (number? expression) (Constant expression)
    :else (apply (operationsObj (first expression)) (map parseObject (rest expression)))
    )
  )