(import '(java.util.regex Matcher))


(ebnfParser (applymods [cuttochase, (partial picklines 0 80) #(replacetokendef %1 "space" "marctemp")] ansi ))

#_(printthenparse 0 60)

#_(pickansilines 0 2000)

(print (pickansilines 1200 1400))

(print (picklines 0 20 (cuttochase ansi)))

(clojure.pprint/pprint (ebnfParser (picklines 0 19 (cuttochase ansi))))

(printthenparse 13 19 (cuttochase ansi))

(print (replaceexldefs (picklines 0 60 (cuttochase ansi))))

