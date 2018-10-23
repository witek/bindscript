(ns bindscript.display
  (:require
   [bindscript.core :as bindscript :refer [def-bindscript]]))


(def-bindscript ::my-script-1
  a 7
  b (inc 2)
  m (assoc {}
           :sum
           (+ a b))
  v (assoc [] :k 1)
  k (keyword "bind" "script"))


(def col-code-bg        "#343029")
(def col-code-plain     "#AFC7B2")
(def col-code-highlight "#F3EFCA")
(def col-code-error     "#E25F19")
(def col-code-dimmed    "#4F7A8A")


(defn code
  [color s]
  [:span
   {:style {:font-family :monospace
            :color color
            :white-space :pre-wrap}}
   (str s)])


(defn binding-tr
  [{:as binding :keys [var expr value exception]}]
  [:tr
   [:td
    {:style {:vertical-align :top
             :padding-right "1em"}}
    [code (if exception col-code-error col-code-plain) var]]
   [:td
    {:style {:vertical-align :top
             :padding-right "1em"}}
    [code col-code-dimmed expr]]
   [:td
    {:style {:vertical-align :top
             :padding-right "1em"}}
    [code "#666666" "=>"]]
   [:td
    {:style {:vertical-align :top}}
    (if exception
      [code col-code-error exception]
      [code col-code-highlight value])]])


(defn script-div
  [script]
  [:div
   {:style {:margin-bottom "1em"}}
   [:div
    {:style {:font-family :monospace
             :background-color col-code-dimmed
             :color col-code-highlight
             :padding "1em"}}
    (str (:identifier script))]
   [:div
    {:style {:scroll :auto
             :background-color col-code-bg
             :color col-code-plain
             :padding "1em"}}
    [:table
     (into [:tbody]
           (map binding-tr (:bindings script)))]]])


(defn display-div
  [data]
  (into [:div]
        (map script-div (:results data))))


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
