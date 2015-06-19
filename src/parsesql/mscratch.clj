(import '(java.util.regex Matcher))

(defn re-qr [replacement]
         (Matcher/quoteReplacement replacement))


(str "hello" "world")
(str "\r")
(def patt (re-pattern "(\r\n<percent> ::= )([^\r]*)(\r\n)"))
(def percentline (first  (re-find patt  cutansi)))
(re-find patt percentline)
(re-find patt (clojure.string/replace percentline patt "$1hello$3" ))

percentline

(print (pickansilines 1200 1400))

(re-find #"<percent> [^\r]*" (pickansilines 0 1000))

(pickansilines 0 200)

(defn applymods [functions bnftext]
  (letfn [(transformstep [text func] (func text))]
    (reduce transformstep bnftext functions)))
(apply str 2 3) 
(applymods [#(str %1 "hello" ) #(str %1 "world")] "yoyo")
