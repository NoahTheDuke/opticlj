;; opticlj/core_test.clj 9:3
(in-ns 'opticlj.core-test)

(map (fn [[form result]]
       (writer/form-output-stream "cool_file.clj"
                                  {:column 20,
                                   :line 10}
                                  (quote opticlj.writer/form-output-stream)
                                  form
                                  result))
     (quote [[(+ 1 1) 2] [(map inc (range 10)) (1 2 3 4 5 6 7 8 9 10)]]))

(";; cool_file.clj 10:20\n(in-ns 'opticlj.writer)\n\n(+ 1 1)\n\n2\n"
 ";; cool_file.clj 10:20\n(in-ns 'opticlj.writer)\n\n(map inc (range 10))\n\n(1 2 3 4 5 6 7 8 9 10)\n")
