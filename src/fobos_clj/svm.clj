(ns fobos_clj.svm
  (:use [fobos_clj.fobos]
        [fobos_clj.util]))

(defn margin ^double [weight fv ^long y]
  (* (dotproduct weight fv) (double y)))

(defn muladd [weight fv ^long y ^double scale]
  (reduce (fn [w [k xi]]
            (assoc w k (+ (get-in w [k] 0.0)
                          (* y xi scale))))
          weight fv))

(defrecord SVM [examples weight eta lambda t last-update])

(extend SVM
  Fobos
  (merge default-io-fns
         {:regularize-weigth l1-regularize
          :update-weight (fn [_]
                           (reduce
                            (fn [new-svm [^long y fv]]
                              (let [new-w (if (< (margin (:weight new-svm) fv y) 1.0)
                                            (muladd (:weight new-svm) fv y (:eta new-svm))
                                            (:weight new-svm))]
                                (regularize-weigth (assoc new-svm :weight new-w) fv)))
                            _ (:examples _)))
          :classify (fn ^long [_ fv] ;; return 1 or -1
                      (if (> (dotproduct (:weight _) fv) 0.0) 1 -1))}))

(defn make-SVM
  ([] (make-model "fobos_clj.svm.SVM"))
  ([examples eta lambda] (make-model "fobos_clj.svm.SVM" examples eta lambda)))
