(ns guestbook.models.db
  (:require [clojure.java.jdbc :as sql]
            [clj-time.coerce :as c])
  (:import java.sql.DriverManager))

(def db {:classname "org.sqlite.JDBC", :subprotocol "postgresql",
:subname "//localhost:5432/guestbook"
; :user postgres
; :password postgres
         })


(defn create-guestbook-table []
  (sql/with-connection
    db
    (sql/create-table
     :guestbook
     [:id "serial"]
     [:timestamp "TIMESTAMP DEFAULT CURRENT_TIMESTAMP"]
     [:name "TEXT"]
     [:message "TEXT"])
    (sql/do-commands "CREATE INDEX timestamp_index ON guestbook (timestamp)")))

(defn read-guests [] (sql/with-connection
    db
    (sql/with-query-results res
["SELECT * FROM guestbook ORDER BY timestamp DESC"] (doall res))))

(defn save-message [name message]
  (sql/with-connection
    db
    (sql/insert-values
     :guestbook
     [:name :message :timestamp]
     [name message (c/to-timestamp (c/from-date (new java.util.Date)))])))

