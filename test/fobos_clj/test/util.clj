(ns fobos_clj.test.util
  (:use [fobos_clj.util])
  (:use [clojure.test]))

(deftest test-parse-line
  (is (= (parse-line "+1 1:2 2:3")
	 [1 [["1" 2.0] ["2" 3.0]]]))
  (is (= (parse-line "-1 1:2.05 2:0.1")
	 [-1 [["1" 2.05] ["2" 0.1]]]))
  (is (= (parse-line "+1 40:0.039590 75:0.039590 89:0.039590 92:0.039590")
	 [1 [["40" 0.039590] ["75" 0.039590]
	     ["89" 0.039590] ["92" 0.039590]]]))
  (is (= (parse-line "-1 40:0.039590 75:0.039590 89:0.039590 92:0.039590")
	 [-1 [["40" 0.039590] ["75" 0.039590]
	      ["89" 0.039590] ["92" 0.039590]]])))

(deftest test-get-f-value
  (let [gold    [-1 -1 1 -1 1 -1 1 1]
        predict [1 -1 1 -1 1 -1 1 -1]
        tp 3
        fp 1
        fn 1
        tn 3
        precision (/ tp (+ tp fp))
        recall (/ tp (+ tp fn))]
    (is (= (/ (* 2.0 recall precision)
              (+ recall precision))
           (get-f-value gold predict)))))
