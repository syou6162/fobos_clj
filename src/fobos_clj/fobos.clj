(ns fobos_clj.fobos
  (:use [clj-utils.io :only (serialize deserialize)]))

(use '[clojure.core.memoize])

(defprotocol Fobos
  (regularize-weigth [_ fv])
  (update-weight [_])
  (classify [_ fv])
  (save-model [_ filename])
  (load-model [_ filename]))

(def default-io-fns
  {:save-model (fn [_ filename] (serialize _ filename))
   :load-model (fn [_ filename] (deserialize filename))})

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
  (/ 1.0 (Math/sqrt iter)))

(def get-eta (memo get-eta'))

(defn get-Lambda-t
  ([t result]
     (if (zero? t)
       result
       (double (+ (get-Lambda-t (dec t)) (get-eta t)))))
  ([t]
     (double (get-Lambda-t t 0.0))))

(def get-Lambda-t (memo get-Lambda-t))

(defn l1-regularize ^Fobos [{examples :examples weight :weight eta :eta
                             lambda :lambda t :t last-update :last-update :as _}
                            fv]
  "return Fobos. 'weight' and 'last-update' are updated by regularization."
  (let [fv-count (count fv)
        Lambda-t (get-Lambda-t t)]
    (loop [fv-idx 0
           weight (transient weight)
           last-update (transient last-update)]
      (if (= fv-idx fv-count)
        (-> _
            (assoc :weight (persistent! weight))
            (assoc :t (inc t))
            (assoc :last-update (persistent! last-update)))
        (let [[k v] (nth fv fv-idx)
              old-w-k (get weight k 0.0)
              t0 (get last-update k 0)
              Lambda-t0 (get-Lambda-t t0)
              lambda-hat (* (- Lambda-t Lambda-t0) lambda)
              new-w-k (clip-by-zero old-w-k lambda-hat)]
          (recur (inc fv-idx)
                 (if (zero? new-w-k)
                   (dissoc! weight k)
                   (assoc! weight k new-w-k))
                 (if (zero? new-w-k)
                   last-update
                   (assoc! last-update k t))))))))
