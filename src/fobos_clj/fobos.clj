(use '[clojure.core.memoize])

(defprotocol Fobos
  (update-weight [_ iter])
  (classify [_ fv]))

(defn clip-by-zero ^double [^double a ^double b]
  "(clip-by-zero a b) = sign(a) max(|a| - b, 0)"
  (if (> a 0.0)
    (if (> a b) (- a b) 0.0)
    (if (< a (- b)) (+ a b) 0.0)))

(defn dotproduct ^double [weight fv]
  (reduce (fn [sum [k v]]
	    (+ sum
	       (* v (get weight k 0.0))))
          0.0 fv))

(defn get-eta' ^double [^long iter]
  "decrease the learning rate in each iteration"
  (/ 1.0 (+ 1.0 iter)))

(def get-eta (memo get-eta'))

(defn get-Lambda-t
  ([t result]
     (if (zero? t)
       result
       (double (+ (get-Lambda-t (dec t)) (get-eta t)))))
  ([t]
     (double (get-Lambda-t t 0.0))))

(def get-Lambda-t (memo get-Lambda-t))
