(defn simpleOperation [operation] (fn [a, b] (mapv (fn [x] (operation (nth a x) (nth b x))) (range (count a)))))
(def v+ (simpleOperation +))
(def v- (simpleOperation -))
(def v* (simpleOperation *))
(def vd (simpleOperation /))
(defn v*s [a, b] (mapv (fn [x] (* (nth a x) b)) (range (count a))))
(defn scalar [a, b] (apply + (v* a b)))
(defn vect [a, b] (vector (- (* (nth a 1) (nth b 2)) (* (nth a 2) (nth b 1)))
                          (- (- (* (nth a 0) (nth b 2)) (* (nth a 2) (nth b 0))))
                          (- (* (nth a 0) (nth b 1)) (* (nth a 1) (nth b 0)))))
(def m+ (simpleOperation v+))
(def m- (simpleOperation v-))
(def m* (simpleOperation v*))
(def md (simpleOperation vd))
(defn transpose [a] (apply mapv vector a))
(defn m*v [a, b] (mapv (fn [x] (apply + (v* (nth a x) b))) (range (count a))))
(defn m*m [a, b] (transpose (mapv (fn [x] (m*v a (nth (transpose b) x))) (range (count (transpose b))))))
(defn m*s [a, b] (mapv (fn [x] (v*s (nth a x) b)) (range (count a))))
(def c+ (simpleOperation m+))
(def c- (simpleOperation m-))
(def c* (simpleOperation m*))
(def cd (simpleOperation md))