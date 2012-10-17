(ns fobos_clj.logistic
  (:use [fobos_clj.fobos]
        [fobos_clj.util]))

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

(defrecord Logistic [examples weight eta lambda t last-update])

(extend Logistic
  Fobos
  (merge default-io-fns
         {:regularize-weigth l1-regularize
          :update-weight (fn [{examples :examples :as _}]
                           (reduce
                            (fn [new-logistic [^long y fv]]
                              (let [new-w (muldiff (:weight new-logistic) fv y (:eta new-logistic))]
                                (regularize-weigth (assoc new-logistic :weight new-w) fv)))
                            _ examples))
          :classify (fn ^long [{weight :weight :as _} fv] ;; return 1 or -1
                      (letfn [(prob [fv]
                                (let [inner-prod (dotproduct weight fv)]
                                  (/ 1.0
                                     (+ 1.0 (Math/exp (- inner-prod))))))]
                        (if (> (prob fv) 0.5) 1 -1)))}))

(defn make-Logistic
  ([] (make-model "fobos_clj.logistic.Logistic"))
  ([examples eta lambda] (make-model "fobos_clj.logistic.Logistic" examples eta lambda)))
