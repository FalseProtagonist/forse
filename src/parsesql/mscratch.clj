(import '(java.util.regex Matcher))


(ebnfParser (applymods [cuttochase, (partial picklines 0 80) #(replacetokendef %1 "space" "marctemp")] ansi ))

#_(printthenparse 0 60)

#_(pickansilines 0 2000)

(print (pickansilines 1200 1400))

(print (picklines 0 20 (cuttochase ansi)))

(clojure.pprint/pprint (ebnfParser (picklines 0 19 (cuttochase ansi))))

(print currentstuff)
(printthenparse 13 19 (cuttochase ansi))

(print (replaceexldefs (picklines 0 60 (cuttochase ansi))))
(printthenparse (:what teststrings))
(def teststrings {:original "<identifier body> ::= <identifier start> [ { <underscore> | <identifier part> } ... ]"
                  :nobrackets "<identifier body> ::= <identifier start> [ <underscore> | <identifier part> ... ]"
                  :what "<identifier> ::= \r\n <stuff>"
                  :what2 "<identifier> ::= <stuff>"})
(def testparser (insta/parser "whitespace = #'[\\040]'+"))
(testparser "    ")
(print (map vector (range 100) (:what2 teststrings)))

(print (:what teststrings))

