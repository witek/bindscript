(ns bindscript.core)

;; Global map of all defined bindscripts by its identifier.
(defonce !scripts (atom {}))


;; Temporary storage for results of a single evaluation of a bindscript.
;; Stores :var :expr :value :exception
;; Used by `eval-script`.
(defonce !script-results (atom []))


(defn eval-test-fn
  [f var-name form]
  (try
    (let [ret-val (f)]
      (swap! !script-results conj {:var var-name
                                   :expr form
                                   :value ret-val})
      ret-val)
    (catch #?(:cljs :default :clj Exception) ex
      (swap! !script-results conj {:var var-name
                                   :expr form
                                   :exception ex})
      nil)))


(defn wrap-in-fn
  [form var-name form]
  `(eval-test-fn (fn [] ~form) ~(str var-name) ~(str form)))


(defn wrap-forms-in-fns
  [result body]
  (if (empty? body)
    result
    (let [next-var (first body)
          next-form (second body)

          ;; spec? (= :spec next-var)
          ;; next-var (if spec? (second next-form) next-var)
          ;; next-form (if spec? `(validate/spec ~(first next-form) ~(second next-form)) next-form)

          rest-body (rest (rest body))
          result (conj result next-var)
          result (conj result (wrap-in-fn next-form next-var next-form))]
      (wrap-forms-in-fns result rest-body))))


(defn scripts-in-order
  "Provide all bindscripts in order."
  []
  (map #(get-in @!scripts [:scripts %])
       (reverse (get-in @!scripts [:order]))))


(defn reset-scripts!
  "Remove all defined bindscripts."
  []
  (reset! !scripts {}))


(defn reg-script!
  "Register a bindscript."
  [script]
  (let [name (:identifier script)
        scripts @!scripts
        order (:order scripts)
        order (conj order name)]
    (-> scripts
        (assoc-in [:order] order)
        (assoc-in [:scripts name] script)
        (->> (reset! !scripts)))))


(defn def-bindscript-macro-impl
  "Implementation for the `bindscript.api/def-bindscript` macro."
  [identifier body]
  (let [body (wrap-forms-in-fns [] body)]
    `(reg-script! {:identifier ~identifier
                   :eval-fn (fn []
                              (let [~@body]))})))


(defn- eval-script
  [script]
  (reset! !script-results [])
  ((:eval-fn script))
  (assoc script :bindings @!script-results))


(defn eval-all-scripts
  "Evaluates all defined bindscripts, returns the results."
  []
  {:results (map eval-script (scripts-in-order))})
