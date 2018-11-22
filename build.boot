(def +version+ "1.0.4")

(set-env!
 :resource-paths #{"src"}
 :dependencies '[[witek/toolbox "1.0.1" :scope "test"]
                 [adzerk/bootlaces "0.1.13" :scope "test"]])

(require '[toolbox.boot.util])
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

