# bindscript

[![Clojars Project](https://img.shields.io/clojars/v/witek/bindscript.svg)](https://clojars.org/witek/bindscript)

bindscript is antoher tool for interactive coding ClojureScript.
It displays values from its bingins forms in a heads-up in the browser.

## Quick Usage

Add dependency to your project:

`witek/bindscript {:mvn/version "X.Y.Z"}`

Require:

`[bindscript.core :refer [def-bindscript]]`

Put bindscripts into any ClojureScript file. Similar to an usual `let` expression:

```clojure
(def-bindscript ::my-script-1
  a 7
  b (inc 2)
  m (assoc {}
           :sum
           (+ a b))
  v (assoc [] :k 1)
  k (keyword "bind" "script"))
```

Let figwheel load it into your browser and bindscript display the values:

![bindscript output example](https://servisto.de/projects/bindscript/output-example.png)

Get insights, modify your code, loop!

## License

Copyright © 2018 Witoslaw Koczewski

Distributed under the Eclipse Public License either version 1.0 or any later
version.
