(ns bindscript.api
  #?(:cljs (:require-macros bindscript.api))
  (:require
   [bindscript.core :as bs]
   #?(:cljs [bindscript.display :as bd-display])))


;; Provide a mechanism to disable execution of bindscripts.
;; In production this should yield false.
(def enabled?
  #?(:cljs goog.DEBUG
     :clj  false))


(defmacro def-bindscript
  "Define a bindscript for later execution and inspection. The script will be
  registered with `identifier`, which sould be a qualified keyword.
  `body` is an usual bindings-form, like in `let`."
  [identifier & body]
  (bs/def-bindscript-macro-impl identifier body))


;; The heads-up must be installed in the browser exactly once.

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
