(ns fobos_clj.test.logistic
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
	init-weight {0 0.1, 1 0.2, 2 0.5, 3 -0.1}
	iter 10
	eta 0.1
	lambda 0.1
	logistic (fobos_clj.logistic.Logistic. examples init-weight eta lambda)]
    (.update-weight logistic iter)))

(deftest test-classify
  (let [weight {0 0.1, 1 0.2, 2 0.5, 3 -0.1}
	logistic (fobos_clj.logistic.Logistic. nil weight nil nil)]
    (is (= (.classify logistic [[1 1] [2 1]]) 1))))
