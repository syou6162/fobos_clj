(ns fobos_clj.core
  (:use fobos_clj.util)
  (:use fobos_clj.fobos)
  (:use fobos_clj.svm)
  (:use fobos_clj.logistic)
  (:use [clojure.contrib.duck-streams :only (reader read-lines)])
  (:use [clojure.contrib.command-line :only (with-command-line)]))

(defn- read-examples [filename]
  (vec (remove nil? (for [line (read-lines filename)]
		      (try (parse-line line)
			   (catch Exception e nil))))))

(defn training-mode [opts]
  (let [train-examples (read-examples (opts :train-filename))
	gold (vec (map first train-examples))
	max-iter (opts :max-iter)
	eta (opts :eta)
	lambda (opts :lambda)
	model-name (opts :model)]
    (loop [iter 1
	   model (update-weight (make-model model-name train-examples eta lambda))]
      (if (= iter max-iter)
        (save-model (assoc model :examples []) (opts :model-file))
	(do
	  (println (str iter ", "
			(count (:weight model)) ", "
			(get-f-value gold (map #(classify model (second %)) train-examples))))
	  (recur (inc iter) (update-weight model)))))))

(defn test-mode [opts]
  (let [test-examples (read-examples (opts :test-filename))
	model (load-model (make-model (opts :model)) (opts :model-file))
	gold (vec (map first test-examples))]
    (get-f-value gold (map #(classify model (second %)) test-examples))))

(defn -main [& args]
  (with-command-line args "lein run --mode train --model fobos_clj.logistic.Logistic --train-filename train.txt --max-iter 100 --eta 1.0 --lambda 1.0 --model-file weight.model
lein run --mode test --test-filename test.txt --model-file weight.model"
    [[train-filename "File name of train"]
     [test-filename "File name of test"]
     [max-iter "Maximum number of iteration"]
     [eta "Update step"]
     [lambda "Regularization parameter"]
     [mode "Which mode to use. You can use [train|test|predict"]
     [model-file "Model filename"]
     [model "Model to use. Currently, we support fobos_clj.svm.SVM and fobos_clj.logistic.Logistic"]
     rest]
    (let [opts {:train-filename train-filename
		:test-filename test-filename
		:max-iter (Integer/parseInt max-iter)
		:eta (Double/parseDouble eta)
		:lambda (Double/parseDouble lambda)
		:model-file model-file
		:model model}]
      (cond (= mode "train") (training-mode opts)
	    (= mode "test") (test-mode opts)
	    :else (println "No such mode: select from train or test modes")))))
