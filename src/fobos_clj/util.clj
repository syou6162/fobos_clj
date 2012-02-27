(ns fobos_clj.util
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
