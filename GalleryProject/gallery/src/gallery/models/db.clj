(ns picture-gallery.models.db
  (:require [clojure.java.jdbc :as sql]))

(def db
  {:subprotocol "postgresql"
   :subname "//localhost/gallery"})

(defmacro with-db [f & body]
  `(sql/with-connection ~db (~f ~@body)))

(def create-user [user]
  (with-db sql/insert-record :users user))

(def get-user [id]
  (with-db sql/with-query-results
    res ["select * from users where id = ?" id] (first res)))
