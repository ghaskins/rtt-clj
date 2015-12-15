(ns rtt-clj.core
  (:require [clojure.tools.cli :refer [parse-opts]])
  (:gen-class))

(def cli-options
  ;; An option with a required argument
  [["-i" "--iterations COUNT" "Iteration count"
    :default 10000
    :parse-fn #(Integer/parseInt %)
    :validate [#(> % 0)]]
   ["-h" "--help"]])

(defn timeOnce [f & args]
  (let [start  (System/nanoTime)
        result (apply f args)
        stop   (System/nanoTime)]
    (vector result (- stop start))))

(defn timeMany [nr f & args]
  (let [[_, result] (timeOnce #(loop [i 1]
                                 (apply f args)
                                 (if (> i nr)
                                   nil
                                   (recur (inc i)))))]
    (double (/ result nr))))

(defn exit [status msg]
  (println msg)
  (System/exit status))

(defn -main [& args]
  (let [{:keys [options arguments errors summary]} (parse-opts args cli-options)]
    ;; Handle help and error conditions
    (cond
      (:help options) (exit 0 summary))
    (let [nr (:iterations options)
          tests [["null" #()]
                 ["pr-str" #(pr-str [1 :a 2 :b 3 :c])]]]
      (println "Running" nr "iterations across" (count tests) "test(s)")
      (dorun (map (fn [[name func]] (println name ":" (timeMany nr func))) tests)))))
