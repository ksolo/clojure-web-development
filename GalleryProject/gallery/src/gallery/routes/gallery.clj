(ns gallery.routes.gallery
  (:require [compojure.core :refer :all]
            [hiccup.element :refer :all]
            [gallery.views.layout :as layout]
            [gallery.util :refer [thumb-prefix image-uri thumb-uri]]
            [gallery.models.db :as db]
            [noir.session :as session]))

(defn thumbnail-link [{:keys [userid name]}]
  [:div.thumbnail
    [:a {:href (image-uri userid name)}
      (image (thumb-uri userid name))]])

(defn gallery-link [{:keys [userid name]}]
  [:div.thumbnail
    [:a {:href (str "/gallery/" userid)}
      (image (thumb-uri userid name))
      userid "'s gallery"]])

(defn show-galleries []
  (map gallery-link (db/get-gallery-previews)))

(defn display-gallery [userid]
  (or
    (not-empty (map thumbnail-link (db/images-by-user userid)))
    [:p "The user " userid " does not have any galleries"]))

(defroutes gallery-routes
  (GET "/gallery/:userid" [userid] (layout/common (display-gallery userid))))
