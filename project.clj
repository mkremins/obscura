(defproject obscura "0.1.0-SNAPSHOT"
  :description "Query Clojure source code with Datomic"
  :url "https://github.com/mkremins/obscura"
  :license {:name "MIT License"
            :url "http://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/tools.analyzer "0.2.1"]
                 [org.clojure/tools.analyzer.jvm "0.2.1"]
                 [org.clojure/tools.reader "0.8.4"]
                 [com.datomic/datomic-free "0.9.4815"]])
