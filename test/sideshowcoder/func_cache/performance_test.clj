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
  (let [futures (map (fn [_]
                       (future
                         (dotimes [n iterations]
                           (dosync
                            (alter map-ref assoc n n))
                           (get @map-ref n))))
                     (range threads))]
    (last (map #(deref %) futures))))

;; 10 threads, makeing 1000 operations each (aka 10000 ops total) takes...

;; func-cache
(with-progress-reporting (quick-bench (func-cache-empty-insert-read (func-cache) 20 1000)))

;; Warming up for JIT optimisations 5000000000 ...
;; Estimating execution count ...
;; Sampling ...
;; Final GC...
;; Checking GC...
;; Finding outliers ...
;; Bootstrapping ...
;; Checking outlier significance
;; Evaluation count : 6 in 6 samples of 1 calls.
;;              Execution time mean : 615.304078 ms
;;     Execution time std-deviation : 9.046675 ms
;;    Execution time lower quantile : 603.134465 ms ( 2.5%)
;;    Execution time upper quantile : 625.622173 ms (97.5%)
;;                    Overhead used : 16.312208 ns

;; hash-map ref using alter
(with-progress-reporting (quick-bench (ref-swap-empty-insert-read (ref (hash-map)) 20 1000)))

;; Warming up for JIT optimisations 5000000000 ...
;; Estimating execution count ...
;; Sampling ...
;; Final GC...
;; Checking GC...
;; Finding outliers ...
;; Bootstrapping ...
;; Checking outlier significance
;; Evaluation count : 6 in 6 samples of 1 calls.
;;              Execution time mean : 100.765711 ms
;;     Execution time std-deviation : 3.891516 ms
;;    Execution time lower quantile : 95.998604 ms ( 2.5%)
;;    Execution time upper quantile : 104.946814 ms (97.5%)
;;                    Overhead used : 16.312208 ns
