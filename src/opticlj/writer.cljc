(ns opticlj.writer
  (:require [clojure.string :as str]
            [opticlj.file :as file]
            [zprint.core :refer [zprint-str]]))

;; Manage diff display

(defrecord Diff [string]
  Object
  (toString [_] "{:string <truncated>}"))

;; Output stream writer

(defn format-str
  "Wrap zprint to make printing consistent."
  [form]
  (zprint-str form {:style :community
                    :map {:force-nl? true
                          :sort-in-code? true}
                    :set {:sort-in-code? true}
                    :record {:to-string? true}}))

(defn fmt-result [result]
  (if (string? result)
    (str/split (format-str result) #"\\n")
    [(format-str result)]))

(defn form-output-stream [file meta kw form result]
  (str/join "\n" (concat [(str ";; " file " " (:line meta) ":" (:column meta))
                          (str "(in-ns '" (namespace kw) ")") ""
                          (format-str form) ""]
                         (fmt-result result)
                         [""])))

;; Optic data

(defn err-optic [path err-path diff]
  {:file     path
   :err-file err-path
   :diff     (->Diff diff)
   :passing? false})

(defn optic [path]
  {:file     path
   :err-file nil
   :diff     nil
   :passing? true})

;; Test checker

(defn write [{:keys [path file meta kw form result]}]
  (let [output   (form-output-stream file meta kw form result)
        err-path (file/err-path path)]
    (merge {:form form :result result :kw kw}
     (if-let [diff (and (file/exists path) (file/diff path err-path output))]
       (do (file/write err-path output)
           (err-optic path err-path diff))
       (do (file/write path output)
           (file/delete err-path)
           (optic path))))))
