(ns sideshowcoder.func-cache.core
  (require [clojure.core.async
            :as async
            :refer [>! <! >!! <!! go-loop chan close!]]))

(defn func-cache
  []
  (let [ops (chan)
        cache (transient (hash-map))]
    (go-loop []
      (let [{res :res op :op} (<! ops)]
        (cond
          (= op :close) (do
                          (close! ops)
                          (>! res true))
          (ifn? op) (do
                      (let [result (apply op [cache])]
                        (>! res (if result result false)))
                      (recur))
          :else (recur))))
    ops))

(defn run-op
  [func-cache op]
  (let [result-chan (chan 1)]
    (>!! func-cache {:res result-chan :op op})
    (let [result (<!! result-chan)]
      (close! result-chan)
      result)))

(defn cache-empty?
  [func-cache]
  (run-op func-cache #(zero? (count %))))

(defn cache-destroy
  [func-cache]
  (run-op func-cache :close))

(defn cache-insert
  [func-cache key value]
  (run-op func-cache #(assoc! % key value)))

(defn cache-get
  [func-cache key]
  (run-op func-cache #(get % key)))

(defn cache-delete
  [func-cache key]
  (run-op func-cache #(dissoc! % key)))
