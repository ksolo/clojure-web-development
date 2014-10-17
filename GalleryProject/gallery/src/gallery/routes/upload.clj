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
            [gallery.models.db :as db]
            [gallery.util :refer :all])
  (:import [java.io File FileInputStream FileOutputStream]
           [java.awt.image AffineTransformOp BufferedImage]
            java.awt.RenderingHints
            java.awt.geom.AffineTransform
            javax.imageio.ImageIO))

(def thumb-size 150)
(def thumb-prefix "thumb_")

(defn scale [img ratio width height]
  (let [scale (AffineTransform/getScaleInstance
                (double ratio) (double ratio))
        transform-op (AffineTransformOp.
                        scale AffineTransformOp/TYPE_BILINEAR)]
    (.filter tranform-op img (BufferedImage. width height (.getType img)))))

(defn scale-image [file]
  (let [img        (ImageIO/read file)
        img-width  (.getWidth img)
        img-height (.getHeight img)
        ratio      (/ thumb-size img-height)]
    (scale img ratio (int (* img-width ratio)) thumb-size)))

(defn save-thumbnail [{:keys [filename]}]
  (let [path (str (gallery-path) File/separaator)]
    (ImageIO/write
      (scale-image (io/input-stream (str path filename)))
      "jpeg"
      (File. (str path thumb-prefix filename)))))

(defn upload-page [info]
  (layout/common
    [:h2 "Upload an image"]
    [:p info]
    (form-to {:enctype "mutlipart/form-data"} [:post "/upload"]
      (file-upload :file)
      (submit-button "upload"))))

(defn handle-upload [{:keys [filename] :as file}]
  (upload-page
    (if (empty? filename)
      "please select a file to upload"
      (try
        ;;save the file and create thumbnail
        (noir.io/upload-file (gallery-path) file :create-path? true)
        (save-thumbnail file)
        ;;display the thumbnail
        (image {:height "150px"}
          (str "/img/" thumb-prefix (url-encode filename)))

        (catch Exception ex
          (str "error uploading file " (.getMessage ex)))))))

(defn serve-file [file-name]
  (file-response (str (gallery-path) File/separator file-name)))

(defroutes upload-routes
  (GET "/upload" [info] (upload-page info))
  (POST "/upload" {params :params} (upload-page params))
  (GET "/img/:file-name" [file-name] (serve-file file-name)))
