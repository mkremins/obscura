(ns obscura.emit.jvm
  (:require [obscura.emit :refer [emit emit-common]]))

(defmethod emit :case [ast]
  [(emit-common ast)])

(defmethod emit :case-test [ast]
  [(emit-common ast)])

(defmethod emit :case-then [ast]
  [(emit-common ast)])

(defmethod emit :deftype [ast]
  [(emit-common ast)])

(defmethod emit :instance? [ast]
  [(emit-common ast)])

(defmethod emit :instance-call [ast]
  [(emit-common ast)])

(defmethod emit :keyword-invoke [ast]
  [(emit-common ast)])

(defmethod emit :monitor-enter [ast]
  [(emit-common ast)])

(defmethod emit :monitor-exit [ast]
  [(emit-common ast)])

(defmethod emit :reify [ast]
  [(emit-common ast)])

(defmethod emit :static-call [ast]
  [(emit-common ast)])
