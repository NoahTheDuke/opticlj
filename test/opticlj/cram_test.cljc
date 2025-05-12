(ns opticlj.cram-test
  (:require [opticlj.clojure-test :refer [defcram]]))

(defcram example-cram
  (= 6 (+ 3 3)))
