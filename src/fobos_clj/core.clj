(ns fobos_clj.core
  (:use fobos_clj.util)
  (:use fobos_clj.fobos)
  (:use fobos_clj.svm)
  (:use [clojure.contrib.duck-streams :only (reader read-lines)])
  (:use [clojure.contrib.command-line :only (with-command-line)]))

(defn- read-examples [filename]
  (vec (remove nil? (for [line (read-lines filename)]
		      (try (parse-line line)
			   (catch Exception e nil))))))

(defn run [opts]
  (let [train-examples (read-examples (opts :train-filename)) 
	test-examples (read-examples (opts :test-filename))
	gold (vec (map first test-examples))
	max-iter (opts :max-iter)
	eta (opts :eta)
	lambda (opts :lambda)
	init-weight (update-weight train-examples {} 0 eta lambda)]
    (loop [iter 1
	   weight init-weight]
      (if (= iter max-iter)
	weight
	(do
	  (println iter ":"
		   (count weight) ":"
		   (get-f-value gold (map #(if (> (dotproduct weight (second %)) 0.0) 1 -1) test-examples)))
	  (recur (inc iter)
		 (update-weight train-examples weight iter eta lambda)))))))

(defn -main [& args]
  (with-command-line args "lein run --train-filename train.txt --test-filename test.txt --max-iter 10 --eta 1.0 --lambda 1.0"
    [[train-filename "File name of train"]
     [test-filename "File name of test"]
     [max-iter "Maximum number of iteration"]
     [eta "Update step"]
     [lambda "Regularization parameter"]
     rest]
    (run {:train-filename train-filename
	  :test-filename test-filename
	  :max-iter (Integer/parseInt max-iter)
	  :eta (Double/parseDouble eta)
	  :lambda (Double/parseDouble lambda)})))
