(ns fobos_clj.test.svm
  (:use fobos_clj.fobos)
  (:use fobos_clj.svm)
  (:use clojure.test))

(deftest test-margin
  (let [weight {0 0.1, 1 0.2, 2 0.5, 3 -0.1}
	fv [[1 1] [2 1]]]
    (is (= (margin weight fv 1)
	   0.7))
    (is (= (margin weight fv -1)
	   -0.7))))

(deftest test-muladd
   (let [weight {0 0.1, 1 0.2, 2 0.5, 3 -0.1}
         fv [[1 1] [2 1.0] [100 1]]]
     (is (= (muladd weight fv 1 1.0)
            {0 0.1, 1 1.2, 2 1.5, 3 -0.1, 100 1.0}))
     (is (= (muladd weight fv -1 1.0)
            {0 0.1, 1 -0.8, 2 -0.5, 3 -0.1, 100 -1.0}))))

(deftest test-update-weight
  (let [examples [[1 [[1 1] [2 1]]]
		  [-1 [[1 2] [2 0]]]]
	eta 0.1
	lambda 0.1
        model (make-SVM examples eta lambda)]
    (update-weight model)))

(deftest test-classify
  (let [weight {0 0.1, 1 0.2, 2 0.5, 3 -0.1}
	svm (assoc (make-SVM) :weight weight)]
    (is (= (classify svm [[1 1] [2 1]]) 1))))
