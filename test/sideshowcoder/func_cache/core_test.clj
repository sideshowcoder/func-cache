(ns sideshowcoder.func-cache.core-test
  (:require [clojure.test :refer :all]
            [sideshowcoder.func-cache.core :refer :all]
            [clojure.core.async :refer [<!!]]))

(deftest init-empty
  (is (= true (cache-empty? (func-cache)))))

(deftest round-trip
  (let [c (func-cache)
        key :key
        value "value"]
    (cache-insert c key value)
    (is (= value (cache-get c key)))))

(deftest destroy-cache
  (let [c (func-cache)]
    (is (= true (cache-destroy c)))
    (is (= nil (<!! c)))))
