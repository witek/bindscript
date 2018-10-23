(ns ^:figwheel-hooks bindscript.api
  #?(:cljs (:require-macros bindscript.api))
  (:require
   [bindscript.core :as bs]
   #?(:cljs [bindscript.display :as bd-display])))


(def enabled?
  #?(:cljs (-> js/window .-goog .-DEBUG)
     :clj  false))


(defmacro def-bindscript
  [identifier & body]
  (bs/def-bindscript identifier body))


(defn ^:before-load reset-bindscripts!
  []
  (bs/reset-scripts!))


(defonce installed
  (do
    (when enabled?
      #?(:cljs (.log js/console "Installing bindscript"))
      #?(:cljs (bd-display/install!))
      (add-watch bs/!scripts
                 ::auto-display
                 (fn [_context _key _ref _old-value _new-value]
                   #?(:cljs (bd-display/update! (bs/eval-all-scripts))))))
                   ;; TODO :clj eval and send results to browser
    true))
