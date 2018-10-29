(ns ^:no-doc bindscript.display
  (:require
   [reagent.core :as r]
   [browser-headsup.api :as headsup]))


(def spacing headsup/spacing)

(def col-code-bg        headsup/col-bg)
(def col-code-plain     headsup/col-default)
(def col-code-highlight headsup/col-highlight)
(def col-code-error     headsup/col-error)
(def col-code-dimmed    headsup/col-dimmed)


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
             :padding-right spacing}}
    [code (if exception col-code-error col-code-plain) var]]
   [:td
    {:style {:vertical-align :top
             :padding-right spacing}}
    [code col-code-dimmed expr]]
   [:td
    {:style {:vertical-align :top
             :padding-right spacing}}
    [code headsup/col-default2 "=>"]]
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
             :color headsup/col-highlight2
             :margin-bottom "0.2em"}}
    (str (:identifier script))]
   [:div
    {:style {:scroll :auto
             :color col-code-plain}}
    [:table
     (into [:tbody]
           (map binding-tr (:bindings script)))]]])

(defn display-div
  [data]
  (into [:div]
        (map script-div (:results data))))


(defonce !state (r/atom {}))


(defn headsup-tab-content
  []
  [display-div @!state])


(def container-id "bindscript-container")


(defn install!
  []
  (headsup/def-tab :bindscript "bindscript" [headsup-tab-content]))


(defn update!
  [evaluated-scripts]
  (reset! !state evaluated-scripts))
