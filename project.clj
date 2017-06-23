(defproject func-cache "0.1.0-SNAPSHOT"
  :description "func-cache using first class functions as a synchronisation mechanism"
  :url "https://github.com/sideshowcoder/func-cache"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/core.async "0.3.443"]
                 [criterium "0.4.4"]]
  :global-vars {*warn-on-reflection* true})
