(ns obscura.core
  (:refer-clojure :exclude [read])
  (:require [clojure.pprint :refer [pprint]]
            [clojure.tools.analyzer.env :as env]
            [clojure.tools.analyzer.jvm :as ana.jvm]
            [clojure.tools.reader :refer [read]]
            [clojure.tools.reader.reader-types :refer [string-push-back-reader]]
            [datomic.api :as d]
            [obscura.emit :refer [assoc-id emit]]
            [obscura.emit.jvm]
            [obscura.schema :refer [schema]]))

(defn analyze-file [file]
  (env/with-env (ana.jvm/global-env)
    (let [rdr (string-push-back-reader (slurp file))
          eof (Object.)]
      (loop [asts []]
        (let [form (read rdr false eof)]
          (if (= form eof)
            asts
            (recur (conj asts (ana.jvm/analyze+eval form)))))))))

(defn setup-db [files]
  (let [uri "datomic:mem://ast"]
    (when (d/delete-database uri)
      (println uri "deleted."))
    (when (d/create-database uri)
      (println uri "created."))
    (let [conn (d/connect uri)]
      @(d/transact conn schema)
      (println "Schema transaction complete.")
      (doseq [file files]
        (let [emitted (->> file analyze-file (map assoc-id) (mapcat emit))]
          (pprint emitted)
          @(d/transact conn emitted)))
      (println "AST transaction complete.")
      (d/db conn))))
