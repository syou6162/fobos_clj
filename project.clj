(defproject fobos_clj "0.0.2"
  :description "Implementation of Forward Backward Splitting"
  :dependencies [[org.clojure/clojure "1.3.0"]
		 [org.clojure/clojure-contrib "1.2.0"]
                 [info.yasuhisay/clj-utils "0.1.0-SNAPSHOT"]
                 [org.clojure/core.memoize "0.5.1"]]
  :dev-dependencies [[swank-clojure "1.3.2"]]
  :jvm-opts ["-Xmx20g" "-server" "-Dfile.encoding=UTF-8"]
  :main fobos_clj.core)