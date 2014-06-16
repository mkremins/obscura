(ns obscura.rules
  (:require [obscura.schema :as schema]))

(defn child-clause [k]
  ['(child ?parent ?child)
   ['?parent k '?child]])

(def child
  (->> schema/node-attributes
    (filter #(= (second %) :ref))
    (map first)
    (mapv child-clause)))

(def descendant
  '[[(descendant ?ancestor ?descendant)
     (child ?ancestor ?descendant)]
    [(descendant ?ancestor ?descendant)
     (child ?ancestor ?x)
     (descendant ?x ?descendant)]])
