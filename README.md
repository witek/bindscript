# bindscript

[![Clojars](https://img.shields.io/clojars/v/witek/bindscript.svg)](https://clojars.org/witek/bindscript)
[![cljdoc](https://cljdoc.org/badge/witek/bindscript)](https://cljdoc.org/d/witek/bindscript/CURRENT)




bindscript is antoher tool for interactive coding ClojureScript.
It displays values from its bindings forms in a heads-up in the browser.

## Quick Usage

Add dependency to your project:

`witek/bindscript {:mvn/version "RELEASE"}`

Require:

`[bindscript.api :refer [def-bindscript]]`

Put bindscripts into any ClojureScript file. Similar to an usual `let` expression:

```clojure
(def-bindscript ::simple
  test-data   [1 2 3 5 8]
  incremented (map inc test-data))
```

Bindscript catches exceptions, if you make a mistake:

```clojure
(def-bindscript :some.project/with-error
  test-data "some string"
  result    (conj "some string" "more text"))
```

Use `:spec` keyword to conform the last binding to a clojure spec:

```clojure
(def-bindscript :some.project/with-spec
  k1    :some/keyword
  :spec qualified-keyword?
  :spec simple-keyword?)
```

Let figwheel load it into your browser and bindscript display the values:

![bindscript output example](https://servisto.de/projects/bindscript/output-example.png)

Get insights, modify your code, loop!

