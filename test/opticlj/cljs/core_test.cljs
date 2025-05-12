(ns opticlj.cljs.core-test
  (:require [clojure.test :refer [deftest is]]
            [opticlj.core :refer [defoptic ok? review!]]))

(defoptic ::two-plus-two (+ 2 2))

(deftest optics
  (is (ok? (review!))))
