(ns obscura.schema
  (:require [datomic.api :refer [tempid]]))

(defn attribute
  ([ident type]
    (attribute ident type :one))
  ([ident type cardinality]
    (attribute ident type cardinality nil))
  ([ident type cardinality doc]
    (merge {:db/id (tempid :db.part/db)
            :db/ident ident
            :db/valueType (keyword "db.type" (name type))
            :db/cardinality (keyword "db.cardinality" (name cardinality))
            :db.install/_attribute :db.part/db}
           (when doc {:db/doc doc}))))

(def common-attributes
  [[:ast/op :keyword]
   [:ast/ns :keyword]
   [:ast/context :keyword] ; maybe this should be an enumerated value?
   [:ast/top-level :boolean]
   [:source/file :string]
   [:source/line :long]
   [:source/form :string]
   [:source/raw-forms :string :many]])

(def node-attributes
  [[:ast.binding/name :keyword]
   [:ast.binding/init :ref]
   [:ast.catch/local :ref]
   [:ast.catch/body :ref]
   [:ast.def/name :keyword]
   [:ast.def/doc :string]
   [:ast.def/init :ref]
   [:ast.def/meta :ref]
   [:ast.do/statement :ref :many]
   [:ast.do/ret :ref]
   [:ast.fn/local :ref]
   [:ast.fn/method :ref :many]
   [:ast.fn-method/param :ref :many]
   [:ast.fn-method/body :ref]
   [:ast.host-call/target :ref]
   [:ast.host-call/arg :ref :many]
   [:ast.host-field/target :ref]
   [:ast.host-interop/target :ref]
   [:ast.if/test :ref]
   [:ast.if/then :ref]
   [:ast.if/else :ref]
   [:ast.invoke/fn :ref]
   [:ast.invoke/arg :ref :many]
   [:ast.let/binding :ref :many]
   [:ast.let/body :ref]
   [:ast.letfn/binding :ref :many]
   [:ast.letfn/body :ref]
   [:ast.loop/binding :ref :many]
   [:ast.loop/body :ref]
   [:ast.map/key :ref :many]
   [:ast.map/val :ref :many]
   [:ast.new/arg :ref :many]
   [:ast.quote/expr :ref]
   [:ast.recur/expr :ref :many]
   [:ast.set/item :ref :many]
   [:ast.set!/target :ref]
   [:ast.set!/val :ref]
   [:ast.throw/exception :ref]
   [:ast.try/body :ref]
   [:ast.try/catch :ref :many]
   [:ast.try/finally :ref]
   [:ast.var/name :keyword]
   [:ast.vector/item :ref :many]
   [:ast.with-meta/expr :ref]
   [:ast.with-meta/meta :ref]])

(def schema
  (map (partial apply attribute)
       (concat common-attributes node-attributes)))
