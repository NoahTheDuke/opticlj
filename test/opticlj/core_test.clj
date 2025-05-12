(ns opticlj.core-test
  (:require
   [clojure.test :refer [deftest is]]
   [opticlj.core :as optic :refer [defoptic ok? review!]]
   [opticlj.file :as file]
   [opticlj.writer :as writer]))

(defoptic ::form-output-stream
  (map (fn [[form result]]
         (writer/form-output-stream
           "cool_file.opticlj" {:line 10 :column 20}
           form result))
       '[[(+ 1 1) 2]
         [(map inc (range 10)) (1 2 3 4 5 6 7 8 9 10)]]))

(defoptic ::err-filename
  [(file/err-path "foo.opticlj") (file/err-path "foo-bar-baz..opticlj")])

(defn fib [n]
  (take n (iterate (fn [[a b]] [b (+ a b)]) [1 1])))

(defoptic ::defoptic
  (let [system (atom {:optics {} :dir "test/__optic__"})]
    (defoptic ::fibonacci (fib 10) :system system)
    (get-in @system [:optics ::fibonacci])))

(deftest optics
  (is (ok? (review!))))
