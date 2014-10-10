(ns gallery.routes.auth
  (:require [hiccup.form :refer :all]
            [compojure.core :refer :all]
            [gallery.routes.home :refer :all]
            [gallery.views.layout :as layout]
            [noir.session :as session]
            [noir.validation :as validation]
            [noir.response :as resp]))

(defn error-item [[error]]
  [:div.error error])

(defn registration-page [& [id]]
  (layout/common
    (form-to [:post "/register"]
      (validation/on-error :id error-item)
      (label "user-id" "user id")
      (text-field "id" id)
      [:br]
      (validaton/on-error :pass error-item)
      (label "pass" "password")
      (password-field "pass")
      [:br]
      (validation/on-error :pass1 error-item)
      (label "pass1" "retype password")
      (password-field "pass1")
      [:br]
      (submit-button "create account"))))

(defn valid? [id pass pass1]
  (validation/rule (validation/has-value? id)
    [:id "user id is required"])
  (validation/rule (validation/min-length? pass 5)
    [:pass "password must be at least 5 characters"])
  (validaton/rule (= pass pass1)
    [:pass "entered passwords do not match"])
  (not (validation/errors? :id :pass :pass1)))

(defn handle-registration [id pass pass1]
  (if (valid? id pass pass1)
    (do (session/put! :user id)
      (resp/redirect "/"))
    (registration-page id)))

(defroutes auth-routes
  (GET "/register" []
    (registration-page))
  (POST "/register" [id pass pass1]
    (handle-registration id pass pass1)))
