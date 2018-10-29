(def +version+ "1.0.2")

(set-env!
 :resource-paths #{"src"}
 :dependencies '[[witek/browser-headsup "1.0.1"]
                 [adzerk/bootlaces "0.1.13" :scope "test"]])

(require '[adzerk.bootlaces :refer :all])

(bootlaces! +version+)

(task-options!

 pom {:project     'witek/bindscript
      :version     +version+
      :description "Dispay values of ClojureScript bindings for interactive coding"
      :developers  {"Witoslaw Koczewski" "wi@koczewski.de"}
      :url         "https://github.com/witek/bindscript"
      :scm         {:url "https://github.com/witek/bindscript.git"}
      :license     {"Eclipse Public License - v 2.0" "https://raw.githubusercontent.com/witek/bindscript/master/LICENSE"}})

