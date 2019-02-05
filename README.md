# ClojureScript UnBroken Promises

This is a library designed for easing up ClojureScript async testing of
Promises.

[![Clojars Project](https://img.shields.io/clojars/v/unbroken-promises.svg)](https://clojars.org/unbroken-promises)

```clojure
[unbroken-promises "0.1.9"]
```

```clojure
unbroken-promises {:mvn/version "0.1.9"}
```

### Readability and robustness

Two macros have been designed for helping with the perilous task of
[testing async code in ClojureScript](https://clojurescript.org/tools/testing#async-testing):
`is-resolved` and `is-rejected`.

Testing `js/Promise` resolution at the moment reads:

```clojure
(deftest the-fetch-test
  (cljs.test/async done
    (-> (fetch "http:/foo.com/bar.json")
        (.then (fn [result]
                 (is (some? result))
                 (is (= result "bar"))))
        (.then done))))
```

But thanks to `unbroken-promise.macro` it becomes:

```clojure
(ns my-namespace-under-test
  (:require [unbroken-promise.macro :as ubp :include-macros true]))

(deftest the-fetch-test
  (ubp/is-resolved [result (fetch "http:/foo.com/bar.json")]
    (is (some? result))
    (is (= result "bar"))))
```

Not only the former is arguably cleaner but it also correctly handles the case
when the promise unexpectedly rejects, reporting it as testing error.

Analogously, `unbroken-promise.macro/is-rejected` makes sure we correctly test
rejections:

```clojure
(deftest the-rejection-test
  (ubp/is-rejected [err (js/Promise.reject (js/Error. "Oh Noes!"))]
    (is (= "Oh Noes!" (.-message err)))))
```

Same convenience feature as above, in case of resolution we want a test error.

### Nitty gritty details

The last thing to be aware of is that this library is also responsible of
writing the messages that you see in the test report.

The `context` entries, in `clojure.test` parlance, are computed by
`unbroken-promises.data/file-and-line-before-promise`.  Some (very naive)
heuristic tries to return the right file line/column coordinates. Please report
any issue you encounter so that I can tweak things.

---

> NOTE: You cannot have more than one async test per deftest form or only the first one will run.

Because of the above ClojureScript restriction **only one macro per `deftest`
is allowed**.

You should never have more than one `is-resolved`/`is-rejected` in a `deftest`
because **only the first macro** will be evaluated **silently ignoring the
others**.

---

## Unlicensed Software

See [The Unlicense](http://unlicense.org/).
