
(ns parsesql.bnf
  (:require [instaparse.core :as insta]))

(def ansi (slurp "92ansibnf.txt"))
(def cutansi (subs ansi (.indexOf ansi "<SQL")))
(defn pickansilines 
  (
   [from to] 
   (let [matcher (re-matcher #".*?\r\n" cutansi)
         getmatch (fn [] (re-find matcher))
         conjer (fn [col val] (conj col (getmatch)))]
    (dotimes [n from] (getmatch))
    (clojure.string/join (reduce conjer [] (range (- to from)))))))


(defn replacetokendef
  [input tokenname rhs] 
  (clojure.string/replace 
   input
   (re-pattern (str "(\r\n<"  tokenname "> ::= )([^\r]*)(\r\n)"))
   (str "$1" rhs "$3")
   ))
(defn createtokreptransform [tokenname rhs]
  (fn [input]
    (replacetokendef (input tokenname rhs))))
(replacetokendef (pickansilines 0 200) "percent" "hello")
((createtokreptransform "percent" "hello") (pickansilines 0 200))

(def firstansi (subs ansi (.indexOf ansi "<SQL") (+ 94 (.indexOf ansi "<SQL"))  ))
(def secondansi (subs ansi (.indexOf ansi "<SQL") (+ 200 (.indexOf ansi "<SQL"))  ))
(def EBNF 
"EBNF = statement+
statement = comment | rule
comment = #'--li[^\\n]*[\\n]' 
rule = lhs whitespace? '::=' whitespace? rhs
lhs = identifier
identifier = #'<[\\w ]+>'
rhs = whitespace? (alternator | identifier | token | quantifier) (whitespace rhs)? whitespace?
nonaltrhs = identifier | token | quantifier
<whitespace> = <#'[\\s]+'>
alternator = nonaltrhs (whitespace? <'|'> whitespace? nonaltrhs)+
token = #'[\\w]+'
quantifier = rhs whitespace '...' | <'['> whitespace rhs <']'>")
(def ebnfParser (insta/parser EBNF))

(defn printthenparse [from to]
  (let [thisansi (pickansilines from to)]
    (print thisansi)
    (ebnfParser thisansi)))

(def ebnfSamples 
(import '(java.util.regex Matcher))
  {
  :joe "--lib<joe> ::= hello"
  :flat  "<SQL terminal character> ::= <SQL language character> | <SQL embedded language character>"
})
(def usefulRanges [0 19 0 3 0 30])

(printthenparse 0 60)

(pickansilines 0 2000)


(re-matcher #".*?\r\n" cutansi)
