(defn count? [comp args] (apply comp (mapv count args)))
(defn is-vector? [a] (and (vector? a) (every? number? a)))
(defn is-matrix? [a] (and (vector? a) (every? is-vector? a) (count? == a)))

(defn make-basic-operation [operation check]
  (fn [& args]
    {:pre [(every? check args) (count? == args)]
     :post [(check %)]}
    (apply mapv operation args)))

(defn make-vector-operation [operation]
  (make-basic-operation operation is-vector?))

(def v+ (make-vector-operation +))
(def v- (make-vector-operation -))
(def v* (make-vector-operation *))

(defn scalar [& args]
  {:pre [(every? is-vector? args) (count? == args)]
   :post [(number? %)]}
  (apply + (apply v* args)))

(defn vect-two [[x1 y1 z1] [x2 y2 z2]]
  {:pre [(every? number? [x1 y1 z1 x2 y2 z2])]
   :post [(vector? %) (= 3 (count %))]}
  (vector
    (- (* y1 z2) (* y2 z1))
    (- (- (* x1 z2) (* x2 z1)))
    (- (* x1 y2) (* x2 y1))))

(defn vect [& args]
  {:pre [(every? is-vector? args) (count? (partial == 3) args)]
   :post [(vector? %) (= 3 (count %))]}
  (reduce vect-two args))

(defn v*s [v & ss]
  {:pre [(is-vector? v) (every? number? ss)]
   :post [(is-vector? v)]}
  (let [prod (apply * ss)] (mapv #(* %1 prod) v)))

; :NOTE: Число столбцов?
(defn make-matrix-operation [operation]
  (make-basic-operation operation is-matrix?))

(def m+ (make-matrix-operation v+))
(def m- (make-matrix-operation v-))
(def m* (make-matrix-operation v*))

(defn m*s [m & ss]
  {:pre [(is-matrix? m) (every? number? ss)]
   :post [(is-matrix? %)]}
  (let [prod (apply * ss)] (mapv #(v*s %1 prod) m)))

(defn m*v [m & vs]
  {:pre [(is-matrix? m) (every? is-vector? vs)]
   :post [(is-vector? %)]}
  (mapv (apply partial scalar vs) m))

(defn transpose [m]
  {:pre [(is-matrix? m)]
   :post [(is-matrix? %)]}
  (apply mapv vector m))

(defn m*m [& args]
  {:pre [(every? is-matrix? args)]
   :post [(is-matrix? %)]}
  (reduce (fn [a b] (transpose (mapv (partial m*v a) (transpose b)))) args))

(defn get-shape [x]
  {:pre [(or (vector? x) (number? x))]}
  (if (number? x)
    ()
    (let [shapes (mapv get-shape x)]
      (if (and (apply = shapes) (every? #(not (nil? %)) shapes))
        (conj (first shapes) (count x))
        nil))))

(defn tensor? [t]
  (not (nil? (get-shape t))))

(defn check-shape? [shapeA shapeB]
  (let [len (min (count shapeA) (count shapeB))]
    (= (take-last len shapeA) (take-last len shapeB))))

(defn broadcast [shapeA b]
  {:pre [(tensor? b) (check-shape? shapeA (get-shape b))]
   :post [(or (check-shape? (get-shape %) shapeA) (check-shape? (get-shape %) (get-shape b)))]}
  (let [shapeB (get-shape b)
        diff (vec (take (Math/abs (- (count shapeA) (count shapeB))) shapeA))
        do-broadcast (fn [res shape]
                 (if (empty? shape)
                   res
                   (recur (vec (repeat (peek shape) res)) (pop shape))))]
    (do-broadcast b diff))
  )

(defn apply-function-to-broadcast [f]
  (fn [& args]
    (if (number? (first args))
      (apply f args)
      (apply mapv (apply-function-to-broadcast f) args))))

(defn get-max-shape [args]
  {:pre [(every? tensor? args)]
   :post [(every? (partial >= (count %)) (mapv count (mapv get-shape args)))]}
; :NOTE: Производиттельность
  (apply max-key count (mapv get-shape args)))

(defn make-broadcast-operation [f]
  (fn [& args]
    {:pre [(every? tensor? args) (every? (partial check-shape? (get-max-shape args)) (mapv get-shape args))]
     :post [(tensor? %)]}
    (let [max-shape (get-max-shape args)]
      (apply (apply-function-to-broadcast f) (mapv (partial broadcast max-shape) args)))))

(def b+ (make-broadcast-operation +))
(def b- (make-broadcast-operation -))
(def b* (make-broadcast-operation *))
(def bd (make-broadcast-operation /))

(println (get-shape (vector (vector 1.1 2.1 3.1) (vector 4.1 5.1 6.1))))
(println (tensor? [[[1]] [[2 3]]]))
(b+ 1)
(println (take-last 2 [1 2]))
(println (take-last 2 [3 1 2]))
(println (check-shape? [1 2] [3 1 2]))
