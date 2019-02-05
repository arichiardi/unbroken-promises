(ns unbroken-promises.data
  (:require [clojure.string :as str]
            [cljs.test :as test]))

(defn format-contexts [contexts]
  (->> contexts
       reverse
       (map-indexed
        (fn [i s]
          (str (str/join (repeat (* i 2) \space)) s)))
       (str/join \newline)))

(defn find-promise-line-depth
  [stack-lines]
  (some->> stack-lines
           (map-indexed vector)
           (filter #(re-find #"new Promise" (second %)))
           ffirst))

#?(:cljs
   (defn file-and-line-before-promise
     "Like cljs.test/file-and-line but gets the first item before the \"new Promise\" entry."
     [err]
     ;; from cljs.test, but targeted at js/Promise
     (let [stack-lines (when (string? (.-stack err))
                         (str/split-lines (.-stack err)))
           promise-depth (find-promise-line-depth stack-lines)]
       (if-let [stack-element (some-> stack-lines
                                      (get (dec promise-depth))
                                      str/trim)]
         (let [fname (test/js-filename stack-element)
               [line column] (test/js-line-and-column stack-element)
               [fname line column] (test/mapped-line-and-column fname line column)]
           {:file fname :line line :column column})
         {:file (.-fileName err)
          :line (.-lineNumber err)}))))

(defn promise-error-map
  [error]
  (merge
   {:type :error
    :message "Uncaught exception, not in assertion."
    :expected nil
    :actual error}
   #?(:cljs (file-and-line-before-promise error))))

(defn promise-then-in-catch-test-error-map
  [res]
  (merge
   {:type :error
    :message "Promise resolved in a .then branch instead of the expected .catch."
    :expected nil
    :actual res}))
