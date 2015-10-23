(ns rtt-clj.core
  (:gen-class))

(defn timeMe [f & args]
  (let [start  (System/nanoTime)
        result (apply f args)
        stop   (System/nanoTime)]
    (vector result (- stop start))
    )
  )

(defn -main
  [& args]
  (let [nr 10000
        func #(pr-str [1 :a 2 :b 3 :c])
        [_, result] (timeMe #(loop [i 1]
                               func
                               (if (> i nr)
                                 0
                                 (recur (inc i)))))]
    (println (double (/ result nr)))))
