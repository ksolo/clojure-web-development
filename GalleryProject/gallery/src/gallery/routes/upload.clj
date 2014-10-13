(ns gallery.routes.upload
  (:require [compojure.core :refer [defroutes GET POST]]
            [hiccup.form :refer :all]
            [hiccup.element :refer [image]]
            [hiccup.util :refer [url-encode]]
            [gallery.views.layout :as layout]
            [noir.io :refer [upload-file]]
            [noir.session :as session]
            [noir.response :as response]
            [noir.util.route :refer [restricted]]
            [clojure.java.io :as io]
            [ring.util.response :refer [file-response]]
            [gallery.models.db :as db])
  (:import [java.io File FileInputStream FileOutputStream]
           [java.awt.image AffineTransformOp BufferedImage]
            java.awt.RenderingHints
            java.awt.geom.AffineTransform
            javax.imageio.ImageIO))
