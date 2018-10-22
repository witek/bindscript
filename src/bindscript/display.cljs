(ns bindscript.display
  (:require
   [bindscript.core :as bindscript]))

(defn binding-tr
  [binding]
  [:tr
   [:td (str (:var binding))]
   [:td (str (:value binding))]
   [:td (str (:exception binding))]
   [:td (str (:expr binding))]])


(defn result-div
  [result]
  [:div
   [:h6
    (str (:identifier result))]
   [:table
    (into [:tbody]
          (map binding-tr (:bindings result)))]])


(defn display-div
  [data]
  (into [:div]
        (map result-div (:results data))))


(def test-data
  {:results
   [{:identifier :hello/world
     :bindings [{:var "a"
                 :expr 1
                 :value 1}
                {:var "ai"
                 :expr '(inc a)
                 :value 2}]}
    {:identifier :hello/world2
     :bindings [{:var "a"
                 :expr 1
                 :value 1}
                {:var "ai"
                 :expr '(inc a)
                 :value 2}]}]})


(defn test-ui
  []
  [:div
   [display-div (bindscript/eval-all-scripts)]])
