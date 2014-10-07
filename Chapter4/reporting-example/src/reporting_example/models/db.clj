(ns reporting-example.models.db
  (:require [clojure.java.jdbc :as sql]))

(def db {:subprotocol "postgresql"
         :subname "//localhost/reporting"})

(defn create-employee-table []
  (sql/create-table
    :employees
    [:name "varchar(50)"]
    [:occupation "varchar(50)"]
    [:place "varchar(50)"]
    [:country "varchar(50)"]))

(sql/with-connection
  db
  (create-employee-table)
  (sql/insert-rows
    :employees
    ["Albert Einstein", "Engineer", "Ulm", "Germany"]
    ["Alfred Hitchcok", "Movie Director", "London", "UK"]
    ["Wernher Von Braun", "Rocket Scientist", "Wyrzysk", "Poland"]
    ["Sigmund Freud", "Neurologist", "Pribor", "Czeck Republic"]
    ["Mahatma Ghandi", "Lawyer", "Gujarat", "India"]
    ["Sachin Tendulkar", "Cricket Player", "Mumbai", "India"]
    ["Michael Schumacher", "F1 Racer", "Cologne", "Germany"]))

(defn read-employes []
  (sql/with-connection
    db (sql/with-query-results rs ["select * from employees"] (doall rs))))
