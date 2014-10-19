(ns gallery.util
  (:require [noir.session :as session])
  (:import java.io.File))

(def gallery "galleries")

(defn gallery-path []
  (str gallery File/separator (session/get :user)))
