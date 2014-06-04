(defproject handsontable "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :main handsontable.core
  :resource-paths ["resources"]
  :plugins [[lein-ring "0.8.2"]]
  :ring {:handler handsontable.core/ring-compojure-test-handler}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/java.data "0.1.1"]
                 [compojure "1.1.6"]
                 [ring/ring-core "1.2.1"]
                 [ring/ring-json "0.3.1"]
                 [hiccup "1.0.2"]
                 [net.sf.jett/jett-core "0.6.0"]])
