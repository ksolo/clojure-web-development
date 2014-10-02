(ns guestbook.routes.auth
  (:require [compojure.core :refer [defroutes GET POST]]
            [guestbook.views.layout :as layout]
            [hiccup.form :refer
              [form-to label text-field password-field submit-button]]
            [noir.response :refer [redirect]]
            [noir.session :as session]
            [noir.validation
              :refer [rule errors? has-value? on-error]]))

(defn control [field name text]
  (list (label name text)
        (field name)
        [:br]))

(defn registration-page []
  (layout/common
    (form-to [:post "/register"]
             (control text-field :id "screen name")
             (control password-field :pass "password")
             (control password-field :pass1 "retype password")
             (submit-button "create-account"))))

(defn login-page [ & [error]]
  (layout/common
    (if error [:div.error "Login error: " error])
    (form-to [:post "/login"]
      (control text-field :id "screen name")
      (control password-field :pass "password")
      (submit-button "login"))))

(defn handle-login [id pass]
  (cond
    (empty? id)
    (login-page "screen name is required")
    (empty? pass)
    (login-page "passwrod is required")
    (and (= "foo" id) (= "bar" pass))
    (do
      (session/put! :user id)
      (redirect "/"))
    :else
    (login-page "authentication failed")))

(defroutes auth-routes
  (GET "/register" [_] (registration-page))
  (POST "/register" [id pass pass1]
    (if (= pass pass1)
      (redirect "/")
      (registration-page)))
  (GET "/login" [] (login-page))
  (POST "/login" [id pass]
    (handle-login id pass))
  (GET "/logout" []
    (layout/common
      (form-to [:post "/logout"]
        (submit-button "logout"))))
  (POST "/logout" []
    (session/clear!)
    (redirect "/")))
