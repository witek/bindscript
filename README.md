# bindscript

bindscript is antoher tool for interactive coding ClojureScript. It displays values from a bingins forms in the browser.

## Quick Usage

Add dependency to your project:

`witek/bindscript {:mvn/version "1.0.1"}`

Require:

`[bindscript.core :refer [def-bindscript]]`

Put your test or inspecting code like a `let` expression into any ClojureScript file:

```clojure
(def-bindscript ::my-script-1
  a   7
  b   (inc 2) 
  sum (+ a b))
```

Let figwheel load it into your browser and bindscript display the values:

TODO: screenshot

Get insights, modify your code, loop!

## License

Copyright © 2018 Witoslaw Koczewski

Distributed under the Eclipse Public License either version 1.0 or any later
version.
