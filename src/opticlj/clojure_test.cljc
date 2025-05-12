(ns opticlj.clojure-test
  (:require
    [clojure.test]
    [opticlj.core :as optic :refer [defoptic]])
  #?(:cljs (:require-macros [opticlj.clojure-test])))

(defmacro defcram
  [test-name & body]
  (let [kw (keyword (str *ns*) (name test-name))]
    `(do (defoptic ~kw [~@body])
         (clojure.test/deftest ~test-name
           (clojure.test/is (optic/check (optic/run ~kw)))))))
