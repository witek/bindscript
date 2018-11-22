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


(defn exception-code
  [exception prefix]
  [:div
   {:style {:color col-code-error}}
   [:div
    {:style {:font-family :serif}}
    prefix]
   (let [message (.-message exception)
         message (if message message (str exception))
         data (ex-data exception)
         data (if (empty? data) nil data)
         cause (or (ex-cause exception) (.-cause exception))]
     [:div
      (str message)
      (if data
        [:div (pr-str data)])
      (if cause [exception-code cause "cause"])])])


(defn binding-tr
  [{:as binding :keys [var expr spec value exception]}]
  [:tr
   {:style {:font-family :monospace}}

   [:td
    {:style {:vertical-align :top
             :padding-right spacing
             :white-space :nowrap
             :color col-code-plain}}
    var]

   [:td
    {:style {:vertical-align :top
             :padding-right spacing
             :color col-code-dimmed}}
    (if spec
      [:span
       [:span
        {:style {:color (if exception col-code-error headsup/col-default2)
                 :font-family :serif}}
        "spec "]
       spec]
      expr)]

   [:td
    {:style {:vertical-align :top
             :padding-right spacing
             :color headsup/col-default}}
    "=>"]

   [:td
    {:style {:vertical-align :top
             :color col-code-highlight}}
    (if exception
      [exception-code exception "error"]
      value)]])


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
