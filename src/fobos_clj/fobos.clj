(ns fobos_clj.fobos)

(defn clip-by-zero [a b]
  (if (> a 0.0)
    (if (> a b) (- a b) 0.0)
    (if (< a (- b)) (+ a b) 0.0)))

(defn dotproduct "内積"
  [weight fv]
  (reduce (fn [sum [k v]]
	    (+ sum
	       (* v (get-in weight [k] 0.0))))
	  0.0 fv))

(defn get-eta [iter example-size]
  "各iterationで重みを減衰させていく"
  (/ 1.0 (+ 1.0 (/ iter example-size))))

(defn l1-regularize 
  "L1正則化をかけて、sparseにした重みベクトルを返す"
  [weight iter example-size lambda]
  (let [lambda-hat (* (get-eta iter example-size) lambda)]
    (reduce (fn [w [k v]]
	      (let [tmp-w (assoc w k (clip-by-zero v lambda-hat))]
		(if (< (Math/abs v) lambda-hat)
		  (dissoc tmp-w k)
		  tmp-w)))
	    weight weight)))

(defn muladd [weight fv y scale]
  (reduce (fn [w [k xi]]
	    (assoc w k (+ (get-in w [k] 0.0)
			  (* y xi scale))))
	  weight fv))
