(ns rtt-clj.async)

(require '[clojure.core.async :as coreasync :refer :all])

(defn golifecycle []
  (let [c (chan)]
    (go (>! c true))
    (assert (= true (<!! c)))
    (close! c))
  )
