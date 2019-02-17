(ns bindscript.figwheel-adapter
  (:require
   [bindscript.api :refer [def-bindscript]]))

(.log js/console "DEMO SPA")


(def-bindscript :some.project/simple
  test-data   [1 2 3 5 8]
  incremented (map inc test-data))


(def-bindscript :some.project/with-error
  test-data "some string"
  result    (conj "some string" "more text"))


(def-bindscript :some.project/with-spec
  k1    :some/keyword
  :spec qualified-keyword?
  :spec simple-keyword?)
