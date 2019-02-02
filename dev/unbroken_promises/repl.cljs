(ns unbroken-promises.repl
  (:require [cljs.spec.alpha :as s]
            [expound.alpha :as expound]))

(s/check-asserts true)

(set! s/*explain-out* (expound/custom-printer {:theme :figwheel-theme}))

(defn ^:export -main [& args]
  (println "Node REPL connected."))
