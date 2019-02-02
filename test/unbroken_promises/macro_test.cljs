(ns unbroken-promises.macro-test
  (:require [clojure.test :as test :refer-macros [deftest is testing]]
            [cljs.spec.alpha :as s]
            [unbroken-promises.macro :as macro :include-macros true]
            [unbroken-promises.specs :as specs]))

(deftest then-test-smoke-test
  (macro/is-resolved [res (js/Promise.resolve :foo)]
    (is (= res :foo) "should bind the result of the promise")
    (is (= res :foo) "should bind the result in all the assertion forms (not only the first)")))

(deftest catch-test-smoke-test
  (let [expected (js/Error. "Oh noes!")]
    (macro/is-rejected [err (js/Promise.reject expected)]
      (is (= err expected) "should bind the error of the rejected promise")
      (is (= err expected) "should bind the error in all the assertion forms (not only the first)"))))

(deftest macro-args-spec
  (testing "two bindings"
    (is (= [:bindings] (->> '([foo (js/Promise.resolve 1)
                               bar "i am extra input"]
                              (is (= 1 1) "i am an assertion"))
                            (s/explain-data ::specs/promise-test-macro)
                            ::s/problems
                            first
                            :path))
        "should fail the argument spec validation"))

  (testing "one binding"
    (is (nil? (->> '([foo (js/Promise.resolve 1)]
                     (is (= 1 1) "i am an assertion"))
                   (s/explain-data ::specs/promise-test-macro)))
        "should pass the argument spec validation"))

  (testing "zero bindings"
    (is (= [:bindings] (->> '([]
                              (is (= 1 1) "i am an assertion"))
                            (s/explain-data ::specs/promise-test-macro)
                            ::s/problems
                            first
                            :path))
        "should fail the argument spec validation")))
