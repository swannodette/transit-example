(defproject transit-example "0.1.0-SNAPSHOT"
  :description "Example demonstrating transit-clj and transit-cljs"
  :url "http://github.com/swannodette/transit-example"

  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2276"]
                 [com.cognitect/transit-clj "0.8.229"]
                 [com.cognitect/transit-cljs "0.8.137"]
                 [bidi "1.10.4"]
                 [ring/ring "1.2.1" :exclusions [org.clojure/java.classpath]]]

  :plugins [[lein-cljsbuild "1.0.4-SNAPSHOT"]
            [cider/cider-nrepl "0.7.0-SNAPSHOT"]]

  :source-paths ["src/clj"]

  :cljsbuild { 
    :builds [{:id "transit-example"
              :source-paths ["src/cljs"]
              :compiler {
                :output-to "resources/public/js/transit_example.js"
                :output-dir "resources/public/js/out"
                :optimizations :none
                :source-map true
                :static-fns true}}
             {:id "transit-example-adv"
              :source-paths ["src/cljs"]
              :compiler {
                :output-to "resources/public/js/transit_example.js"
                :output-dir "resources/public/js/out-adv"
                :optimizations :advanced
                :pretty-print false}}]})
