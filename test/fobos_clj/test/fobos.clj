(ns fobos_clj.test.fobos
  (:use [fobos_clj.fobos])
  (:use [clojure.test]))

(deftest test-clip-by-zero
  (is (= (clip-by-zero 1.0 0.5) 0.5))
  (is (= (clip-by-zero 1.0 1.5) 0.0))
  (is (= (clip-by-zero -1.0 0.5) -0.5))
  (is (= (clip-by-zero -1.0 1.5) 0.0)))

(deftest test-dotproduct
  (is (= 0.0
	 (dotproduct {} [[1 1] [3 2]])))
  (is (= 0.0
	 (dotproduct {0 0.1,
		      1 0.2,
		      2 0.5,
		      3 -0.1}
		     [])))
  (is (= 0.0
	 (dotproduct {0 0.1,
		      1 0.2,
		      2 0.5,
		      3 -0.1}
		     [[1 1] [3 2]])))
  (is (= 0.0
	 (dotproduct {0 0.1,
		      "1" 0.2,
		      2 0.5,
		      "3" -0.1}
		     [["1" 1.0] ["3" 2.0]])))
  (is (= 10.0
	 (dotproduct {0 0.1,
		      "1" 0.2,
		      2 0.5,
		      "3" -0.1
		      "100" 10}
		     [["1" 1.0] ["3" 2.0] ["100" 1.0]])))
  (is (= 0.1
	 (dotproduct {{:type 31, :str ""} 0.1,
		      {:type 32, :str ""} 0.3}
		     [[{:type 31, :str ""} 1.0]]))))

(deftest test-get-eta
  (is (= 1.0
	 (get-eta 0 100)))
  (is (= (/ 1.0 (+ 1.0 (/ 3 100)))
	 (get-eta 3 100))))

(deftest test-l1-regularize
  (let [weight {0 0.1, 1 0.2, 2 0.5, 3 -0.1}]
    (let [iter 0
	  example-size 1000
	  lambda 1.0]
      (is (empty?
	   (l1-regularize weight iter example-size lambda))))
    (let [iter 0
	  example-size 100
	  lambda 0.001]
      (is (not (empty?
		(l1-regularize weight iter example-size lambda)))))
    (let [iter 1000
	  example-size 100
	  lambda 1.0]
      (is (not (empty?
		(l1-regularize weight iter example-size lambda)))))))

(deftest test-muladd
  (let [weight {0 0.1, 1 0.2, 2 0.5, 3 -0.1}
	fv [[1 1] [2 1.0] [100 1]]]
    (is (= (muladd weight fv 1 1.0)
	   {0 0.1, 1 1.2, 2 1.5, 3 -0.1, 100 1.0}))
    (is (= (muladd weight fv -1 1.0)
	   {0 0.1, 1 -0.8, 2 -0.5, 3 -0.1, 100 -1.0}))))
