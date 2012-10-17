(ns fobos_clj.util
  (:use [clojure.contrib.core :only (new-by-name)])
  (:use [clojure.contrib.string :only (split)]))

(defn parse-line [line]
  (let [[y fv] (-> (re-seq #"([-+]?1)\s(.*)" line)
		   (first)
		   (rest))]
    [(if (= y "+1") 1 -1)
     (->> fv
	  (split #"\s")
	  (map #(let [[xi cnt] (split #":" %)]
		  [xi (Double/parseDouble cnt)]))
	  (vec))]))

(defn make-model
  ([model-name]
     (let [examples []
           eta 1.0
           lambda 1.0]
       (make-model model-name examples eta lambda)))
  ([model-name examples eta lambda]
     (let [weight (hash-map)
           t 1
           last-update (hash-map)]
       (new-by-name model-name examples weight eta lambda t last-update))))

(defn get-f-value [gold prediction]
  (let [freq (frequencies (map vector gold prediction))
        tp (get freq [1 1] 0)
        tn (get freq [-1 -1] 0)
        fp (get freq [-1 1] 0)
        fn (get freq [1 -1] 0)
        recall (/ tp (+ tp fn))
        precision (/ tp (+ tp fp))]
    (/ (* 2.0 recall precision)
       (+ recall precision))))
