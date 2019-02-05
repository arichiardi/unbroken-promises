(ns unbroken-promises.macro
  (:require [cljs.test]
            [unbroken-promises.data]
            #?@(:cljs [[goog.object]]))
  #?(:cljs (:require-macros [unbroken-promises.macro])))

(defmacro is-resolved
  "Test asynchronously the Promise resolve branch.

  The macro binds the result of the Promise, passing it through the
  .then branch, so that it is available in the assertions."
  {:style/indent 1}
  [bindings & assertions]

  (assert (vector? bindings) "bindings should be a vector")
  (assert (even? (count bindings)) "bindings forms should be even")
  (assert (= 2 (count bindings)) "only one binding form is allowed")

  (let [bound-sym (first bindings)
        promised-body (second bindings)]
    `(let [promise# ~promised-body]
       (assert (instance? js/Promise promise#) "second binding item should yield a js/Promise")
       (cljs.test/async ~'done
         (. promise#
            (~'then
             (fn [~bound-sym]
               ~@assertions
               (~'done)
               ~bound-sym)
             (fn [err#]
               (-> err#
                   unbroken-promises.data/promise-error-map
                   cljs.test/do-report)
               (~'done)
               err#)))))))

(defmacro is-rejected
  "Test asynchronously the Promise reject branch.

  The macro binds the result of the Promise, passing it through the
  .catch branch, so that it is available in the assertions."
  {:style/indent 1}
  [bindings & assertions]

  (assert (vector? bindings) "bindings should be a vector")
  (assert (even? (count bindings)) "bindings forms should be even")
  (assert (= 2 (count bindings)) "only one binding form is allowed")

  (let [bound-sym (first bindings)
        promised-body (second bindings)]
    `(let [promise# ~promised-body]
       (assert (instance? js/Promise promise#) "second binding item should yield a js/Promise")
       (cljs.test/async ~'done
         (. promise#
            (~'then
             (fn [res#]
               (-> res#
                   unbroken-promises.data/promise-then-in-catch-test-error-map
                   cljs.test/do-report)
               (~'done)
               res#)
             (fn [~bound-sym]
               ~@assertions
               (~'done)
               ~bound-sym)))))))
