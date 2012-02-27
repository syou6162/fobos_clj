(ns fobos_clj.test.svm
  (:use [fobos_clj.svm])
  (:use [clojure.test]))

(deftest test-margin
  (let [weight {0 0.1, 1 0.2, 2 0.5, 3 -0.1}
	fv [[1 1] [2 1]]]
    (is (= (margin weight fv 1)
	   0.7))
    (is (= (margin weight fv -1)
	   -0.7))))

(deftest test-update-weight
  (let [examples [[1 [[1 1] [2 1]]]
		  [-1 [[1 2] [2 0]]]]
	init-weight {0 0.1, 1 0.2, 2 0.5, 3 -0.1}
	iter 10
	eta 0.1
	lambda 0.1]
    (update-weight examples init-weight iter eta lambda)))

(deftest test-classify
  (let [weight {0 0.1, 1 0.2, 2 0.5, 3 -0.1}]
    (is (classify weight [[1 1] [2 1]] 1))))
