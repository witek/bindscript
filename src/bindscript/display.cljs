(ns ^:no-doc bindscript.display
  (:require
   [reagent.core :as r]))

(def spacing "0.5em")

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
             :padding-right spacing}}
    [code (if exception col-code-error col-code-plain) var]]
   [:td
    {:style {:vertical-align :top
             :padding-right spacing}}
    [code col-code-dimmed expr]]
   [:td
    {:style {:vertical-align :top
             :padding-right spacing}}
    [code "#666666" "=>"]]
   [:td
    {:style {:vertical-align :top}}
    (if exception
      [code col-code-error exception]
      [code col-code-highlight value])]])


(defn script-div
  [script]
  [:div
   ;; {:style {:margin-bottom spacing}}
   [:div
    {:style {:font-family :monospace
             :background-color col-code-dimmed
             :color col-code-highlight
             :padding spacing}}
    (str (:identifier script))]
   [:div
    {:style {:scroll :auto
             :background-color col-code-bg
             :color col-code-plain
             :padding spacing}}
    [:table
     (into [:tbody]
           (map binding-tr (:bindings script)))]]])

(defn display-div
  [data]
  (into [:div]
        (map script-div (:results data))))

(defn toggle-size
  [size]
  (case size
    :half :min
    :half))


(defn save-size!
  [value]
  (set! (.-bindscript_size js/window) (name value)))


(defn load-size!
  []
  (if-let [value (.-bindscript_size js/window)]
    (keyword value)
    :half))


(defn heads-up-toggler
  [update-fn]
  [:div
   {:style {:padding spacing
            :color "#999999"
            :background-color col-code-bg
            :cursor :pointer}
    :on-click (fn [_]
                (save-size! (toggle-size (load-size!)))
                (update-fn))}
   "bindscripts"])

(defn heads-up
  [payload size update-fn]
  (let [minimized? (= :min size)]
    [:div
     {:style {:position :fixed
              :bottom 0
              :height (case size
                        :min :auto
                        :half "50%")
              :width (if minimized? nil "100%")
              :right (if minimized? "1em" nil)
              :background col-code-bg
              :font-size "90%"
              :line-height "90%"
              :overflow (if minimized? nil :auto)
              :z-index 9999 ;; figwheel's -1
              :transition "all 0.2s ease-in-out 0s"
              :box-sizing "border-box"
              :box-shadow "0px -4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19)"}}
     [:div]
     (if minimized?
       [heads-up-toggler update-fn]
       [:div
        {:style {}}
        [:div
         {:style {:position :fixed
                  :bottom "0em"
                  :right "1em"}}
         [heads-up-toggler update-fn]]
        payload])]))


(declare update!)

(defn heads-up-controller
  [evaluated-scripts size]
  [heads-up
   [display-div evaluated-scripts]
   size
   #(update! evaluated-scripts)])

(def container-id "bindscript-container")


(defn install!
  []
  (if-not (.querySelector js/document (str "#" container-id))
    (let [el (.createElement js/document "div")]
      (.setAttribute el "id" container-id)
      (-> (.-body js/document)
          (.appendChild el)))))


(defn update!
  [evaluated-scripts]
  (r/render [heads-up-controller evaluated-scripts (load-size!)]
            (js/document.getElementById container-id)))
