(ns unbroken-promises.data-test
  (:require [clojure.test :as test :refer-macros [deftest is]]
            [unbroken-promises.data :as data]))

(deftest test-format-contexts
  (is (= "" (data/format-contexts [])) "it should return an empty string if not contexts are passed in")
  (is (= "first\n  second\n    third" (data/format-contexts ["third" "second" "first"])) "it should return a last-to-first formatted string of contexts"))

(deftest test-file-and-line-before-promise
  (is (= nil (data/find-promise-line-depth nil)) "it should return nil when the input is nil")
  (is (= nil (data/find-promise-line-depth (list))) "it should return nil when the input is empty")

  (is (= 3 (data/find-promise-line-depth ["at new cljs$core$ExceptionInfo (/path/to/out/cljs/core.cljs:11159:16)"
                                          "at cljs$core$ex_info (/path/to/out/cljs/core.cljs:11156:1)"
                                          "at anamespace.spec.validate_as_promise (/path/to/out/anamespace/spec.cljs:14:16)"
                                          "at new Promise (<anonymous>)"
                                          "at anamespace$spec$validate_as_promise (/path/to/out/anamespace/spec.cljs:4:1])"]))
      "it should find the right depth of the \"new Promise\" stack entry")

  (is (= nil (data/find-promise-line-depth ["at new cljs$core$ExceptionInfo (/path/to/out/cljs/core.cljs:11159:16)"
                                            "at cljs$core$ex_info (/path/to/out/cljs/core.cljs:11156:1)"]))
      "it should return nil when it cannot find the \"new Promise\" stack entry"))
