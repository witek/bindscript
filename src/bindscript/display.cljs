(ns ^:no-doc bindscript.display
  (:require
   [clojure.pprint :as pprint]
   [cljs.reader :refer [read-string]]
   [reagent.core :as r]
   [browser-headsup.api :as headsup]))


(def spacing "10px")
(def tr-spacing "5px")

(def col-code-bg        headsup/col-bg)
(def col-code-plain     headsup/col-default)
(def col-code-highlight headsup/col-highlight)
(def col-code-error     headsup/col-error)
(def col-code-dimmed    headsup/col-dimmed)


(defn Data [data]
  [:div
   {:style {:white-space :pre-wrap}}
   (with-out-str (pprint/pprint data))])


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
         cause (or (ex-cause exception) (.-cause ^js exception))]
     [:div
      (str message)
      (if data
        (Data data))
      (if cause [exception-code cause "cause"])])])


(def collapse-len 42)

(defn collapsed-str
  [s]
  (let [s (str s)]
    (if (< collapse-len (count s))
      (str (.substring s 0 collapse-len) "...")
      s)))

(defn binding-tr
  [{:as binding :keys [var expr spec value exception]}]
  [:tr
   {:style {:font-family :monospace}}

   [:td
    {:style {:vertical-align :top
             :padding-right spacing
             :padding-top tr-spacing
             :white-space :nowrap
             :color col-code-plain}}
    var]

   [:td
    {:style {:vertical-align :top
             :white-space :nowrap
             :padding-right spacing
             :padding-top tr-spacing
             :color col-code-dimmed}}
    (if spec
      [:span
       [:span
        {:style {:color (if exception col-code-error headsup/col-default2)
                 :font-family :serif}}
        "spec "]
       [collapsed-str spec]]
      [collapsed-str expr])]

   [:td
    {:style {:vertical-align :top
             :padding-top tr-spacing
             :color col-code-highlight}}
    (if exception
      [exception-code exception "error"]
      [Data
       (try
         (read-string value)
         (catch :default ex
           (str value)))])]])


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
