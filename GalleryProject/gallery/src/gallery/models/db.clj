(ns picture-gallery.models.db
  (:require [clojure.java.jdbc :as sql]))

(def db
  {:subprotocol "postgresql"
   :subname "//localhost/gallery"})

(def create-user [user]
  (sql/with-connection
    db
    (sql/insert-record :users user)))
