(ns fobos_clj.svm
  (:use [fobos_clj.fobos]))

(defn margin [weight fv y]
  (* (dotproduct weight fv) y))

(defn muladd [weight fv y scale]
  (reduce (fn [w [k xi]]
	    (assoc w k (+ (get-in w [k] 0.0)
			  (* y xi scale))))
	  weight fv))

(defrecord SVM
  [examples weight eta lambda]
  Fobos
  (update-weight [_ iter]
		 (let [example-size (count examples)]
		   (assoc _ :weight
			  (l1-regularize
			   (reduce (fn [w [y fv]]
				     (if (< (margin w fv y) 1.0)
				       (muladd w fv y eta)
				       w))
				   weight examples)
			   iter example-size lambda))))
  (classify [_ fv]
	    (if (> (dotproduct weight fv) 0.0) 1 -1)))
