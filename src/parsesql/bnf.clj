
(ns parsesql.bnf
  (:require [instaparse.core :as insta]))

(def ansi (slurp "92ansibnf.txt"))
#_(def cutansi (subs ansi (.indexOf ansi "<SQL")))
(defn cuttochase [input] (let [start (.indexOf input "<SQL")] (subs input start)))

(defn picklines 
  (
   [from to input] 
   (let [matcher (re-matcher #".*?\r\n" input)
         getmatch (fn [] (re-find matcher))
         conjer (fn [col val] (conj col (getmatch)))]
    (dotimes [n from] (getmatch))
    (clojure.string/join (reduce conjer [] (range (- to from)))))))

(def exclam (re-find #"\n[^\n]*\n[^\n]*![^\n]*\n[^\n]*\n" (picklines 0 200 (cuttochase ansi))))

(defn replacetokendef
  [tokenname rhs input] 
  (clojure.string/replace 
   input
   (re-pattern (str "(\r\n<"  tokenname "> ::= )([^\r]*)(\r\n)"))
   (str "$1" rhs "$3")
   ))
(defn replaceexldefs
  [input]
  (clojure.string/replace
   input
   (re-pattern (str "(\r\n<)([\\w\\040]*)(> ::= )(!![^\r]*)(\r\n)"))
   (str "$1$2$3<m_$2>$5")))
(defn createtokreptransform [tokenname rhs]
  (fn [input]
    (replacetokendef input tokenname rhs)))

(defn applymods [functions bnftext]
  (letfn [(transformstep [text func] (func text))]
    (reduce transformstep bnftext functions)))

(def firstansi (subs ansi (.indexOf ansi "<SQL") (+ 94 (.indexOf ansi "<SQL"))  ))
(def secondansi (subs ansi (.indexOf ansi "<SQL") (+ 200 (.indexOf ansi "<SQL"))  ))
(def EBNF 
"EBNF = statement+
statement = comment | rule
comment = whitespace? #'--li[^\\n]*[\\n]' 
rule = lhs whitespace? '::=' whitespace? rhs
lhs = identifier
identifier = #'<[\\w\\040]+>'
rhs = whitespace? (alternator | identifier | token | quantifier) (whitespace rhs)? whitespace?
nonaltrhs = identifier | token | quantifier
<whitespace> = <#'[\\s]+'>
alternator = nonaltrhs (whitespace? <'|'> whitespace? nonaltrhs)+
token = #'[^|\\]\\[\\}\\{\\040]' | #'[\\w+]'
quantifier = rhs whitespace '...' | <'['> whitespace rhs <']'>")
(def ebnfParser (insta/parser EBNF))

(defn printthenparse 
  ([from to input]
   (let [thisansi (picklines from to input)]
     (print thisansi)
     (clojure.pprint/pprint (ebnfParser thisansi))))
  ([input]
   (printthenparse 0 (count input) input))
s  )

(defn mmtest ([a] (print a)) ([a b] (print "two args!")))

(def testStrings
  {
   :joe "\r\n--lib<joe> ::= hello\r\n"
   :flat  "<SQL terminal character> ::= <SQL language character> | <SQL embedded language character>"
   })
(def usefulRanges [0 19 0 3 0 30 19 21 0 69 0 108])

(defn currentmods
  [start end]
  (vector cuttochase #(picklines start end %1) replaceexldefs))

(defn currentstuff 
  [start end] 
  (applymods (currentmods start end) ansi))


#_(printthenparse (currentstuff 0 108))
(printthenparse (currentstuff 107 108))
(print (map vector (range 43) (currentstuff 107 108)))
#_(print (currentstuff 0 106))
#_(printthenparse mtemp)
(print (currentstuff 0 20))
#_(print (picklines 0 69 (cuttochase ansi)))
