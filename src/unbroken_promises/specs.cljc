(ns unbroken-promises.specs
  (:require [clojure.spec.alpha :as s]))

(s/def ::local-name (s/and simple-symbol? #(not= '& %)))

(s/def ::single-binding (s/tuple ::local-name any?))

(s/def ::promise-test-macro
  (s/cat :bindings ::single-binding
         :assertions (s/* any?)))

(s/fdef unbroken-promises.macro/then-test
  :args ::promise-test-macro)

(s/fdef unbroken-promises.macro/catch-test
  :args ::promise-test-macro)
