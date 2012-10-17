(ns fobos_clj.test.logistic
  (:use [fobos_clj.fobos])
  (:use [fobos_clj.logistic])
  (:use [clojure.test]))

(deftest test-logistic-deriv
  (logistic-deriv 1 0.1 0.3)
  (logistic-deriv -1 0.1 0.3))

(deftest test-muldiff
  (muldiff {} [[0 1] [1 1.5]] 1 1.0)
  (muldiff {} [[0 1] [1 1.5]] -1 1.0)
  (muldiff {0 1.5} [[0 1] [1 1.5]] 1 1.0)
  (muldiff {0 1.5 1 1.0} [[0 1] [1 1.5]] 1 1.0))

(deftest test-update-weight
  (let [examples [[1 [[1 1] [2 1]]]
		  [-1 [[1 2] [2 0]]]]
	eta 0.1
	lambda 0.1
	logistic (make-Logistic examples eta lambda)]
    (update-weight logistic)))

(deftest test-classify
  (let [weight {0 0.1, 1 0.2, 2 0.5, 3 -0.1}
	logistic (assoc (make-Logistic) :weight weight)]
    (is (= (classify logistic [[1 1] [2 1]]) 1))))
