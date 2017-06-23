(ns sideshowcoder.func-cache.performance-test
  (:require [sideshowcoder.func-cache.core :refer :all]
            [criterium.core :refer [bench with-progress-reporting quick-bench]]))


(defn func-cache-empty-insert-read
  [cache threads iterations]
  (let [futures (map (fn [_]
                       (future
                         (dotimes [n iterations]
                           (cache-empty? cache)
                           (cache-insert cache n n)
                           (cache-get cache n))))
                     (range threads))]
    (last (map #(deref %) futures))))

(defn ref-swap-empty-insert-read
  [map-ref threads iterations]
  )

;; 10 threads, makeing 1000 operations each (aka 10000 ops total) takes...

;; func-cache
(with-progress-reporting (quick-bench (func-cache-empty-insert-read (func-cache) 10 1000)))
;; hash-map ref using swap!
(with-progress-reporting (quick-bench (ref-swap-empty-insert-read (ref (hash-map)) 10 1000)))
