(ns fobos_clj.logistic
  (:use [fobos_clj.fobos]))

(defn logistic-deriv [y inner-product xi]
  (* (- y)
     xi
     (Math/exp (* (- y) inner-product))
     (/ 1.0 (+ 1.0 (Math/exp (* (- y) inner-product))))))

(defn muldiff [weight fv y scale]
  (let [inner-product (dotproduct weight fv)]
    (reduce (fn [w [k xi]]
	      (assoc w k
		     (- (get w k 0.0)
			(* scale
			   (logistic-deriv y inner-product xi)))))
	    weight fv)))

(defrecord Logistic
  [examples weight eta lambda]
  Fobos
  (update-weight [_ iter]
		 (let [example-size (count examples)]
		   (assoc _ :weight
			  (l1-regularize
			   (reduce (fn [w [y fv]]
				     (muldiff w fv y eta))
				   weight examples)
			   iter example-size lambda))))
  (classify [_ fv]
	    (letfn [(prob [fv]
			  (let [inner-prod (dotproduct weight fv)]
			    (/ 1.0
			       (+ 1.0 (Math/exp (- inner-prod))))))]
	      (if (> (prob fv) 0.5) 1 -1))))
