(ns fobos_clj.svm
  (:use [fobos_clj.fobos]))

(defn margin [weight fv y]
  (* (dotproduct weight fv) y))

(defn update-weight [examples weight iter eta lambda]
  (let [example-size (count examples)]
    (l1-regularize
     (reduce (fn [w [y fv]]
	       (if (< (margin w fv y) 1.0)
		 (muladd w fv y eta)
		 w))
	     weight examples)
     iter example-size lambda)))

(defn classify [weight fv y]
  (> (margin weight fv y) 0.0))
