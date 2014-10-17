(ns gallery.util
  (:require [noir/session :as session]))

(def gallery "galleries")

(defn gallery-path []
  (str galleries File/separator (session/get :user)))
