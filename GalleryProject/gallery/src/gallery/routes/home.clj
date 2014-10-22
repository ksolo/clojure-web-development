(ns gallery.routes.home
  (:require [compojure.core :refer :all]
            [noir.session :as session]
            [gallery.views.layout :as layout]
            [gallery.routes.gallery :refer [show-galleries]]))

(defn home []
  (layout/common (show-galleries)))

(defroutes home-routes
  (GET "/" [] (home)))
