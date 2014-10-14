(ns gallery.handler
  (:require [compojure.route :as route]
            [compojure.core :refer [defroutes]]
            [noir.util.middleware :as noir-middleware]
            [gallery.routes.home :refer [home-routes]]
            [gallery.routes.auth :refer [auth-routes]]
            [gallery.routes.upload :refer [upload-routes]]))

(defn init []
  (println "gallery is starting"))

(defn destroy []
  (println "gallery is shutting down"))

(defroutes app-routes
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (noir-middleware/app-handler
    [auth-routes
     home-routes
     upload-routes
     app-routes]))
