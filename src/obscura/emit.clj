(ns obscura.emit
  (:require [datomic.api :refer [tempid]]))

(defn assoc-id [ast]
  (assoc ast :db/id (tempid :db.part/user)))

(defn emit-common [ast]
  {:db/id (:db/id ast)
   :ast/op (:op ast)
   :ast/ns (-> ast :env :ns keyword)
   :ast/context (-> ast :env :context)
   :ast/top-level (or (:top-level ast) false)
   :source/file (-> ast :env :file)
   :source/line (or (-> ast :env :line) 0)
   :source/form (-> ast :form pr-str)
   :source/raw-forms (map pr-str (:raw-forms ast))})

(defmulti emit :op)

(defmethod emit :default [ast]
  (let [op-name (str "ast." (name (:op ast)))]
    (loop [emit1 (emit-common ast)
           emits ()
           child-ks (:children ast)]
      (if-let [k (first child-ks)]
        (let [v (k ast)]
          (if (vector? v)
            (let [children (map assoc-id v)]
              (recur (assoc emit1
                       (keyword op-name (apply str (butlast (name k))))
                       (map :db/id children))
                     (concat emits (mapcat emit children))
                     (rest child-ks)))
            (let [child (assoc-id v)]
              (recur (assoc emit1 (keyword op-name (name k)) (:db/id child))
                     (concat emits (emit child))
                     (rest child-ks)))))
        (cons emit1 emits)))))

(defmethod emit :binding [ast]
  (let [init (when (:init ast) (assoc-id (:init ast)))]
    (concat [(merge (emit-common ast)
                    {:ast.binding/name (-> ast :name keyword)}
                    (when init {:ast.binding/init (:db/id init)}))]
            (when init (emit init)))))

(defmethod emit :def [ast]
  (let [doc (:doc ast)
        init (when (:init ast) (assoc-id (:init ast)))
        meta (when (:meta ast) (assoc-id (:meta ast)))]
    (concat [(merge (emit-common ast)
                    {:ast.def/name (-> ast :name keyword)}
                    (when doc {:ast.def/doc doc})
                    (when init {:ast.def/init (:db/id init)})
                    (when meta {:ast.def/meta (:db/id meta)}))]
            (when init (emit init))
            (when meta (emit meta)))))

(defmethod emit :try [ast]
  (let [body (assoc-id (:body ast))
        catches (map assoc-id (:catches ast))
        finally (when (:finally ast) (assoc-id (:finally ast)))]
    (concat [(merge (emit-common ast)
                    {:ast.try/body (:db/id body)
                     :ast.try/catch (map :db/id catches)}
                    (when finally {:ast.try/finally (:db/id finally)}))]
            (emit body)
            (mapcat emit catches)
            (when finally (emit finally)))))

(defmethod emit :var [ast]
  [(assoc (emit-common ast) :ast.var/name (-> ast :form keyword))])
