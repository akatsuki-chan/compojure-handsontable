(ns handsontable.core
  (:gen-class)
  (:use [ring.util.response]
        [ring.middleware.session]
        [ring.middleware.cookies]
        [ring.middleware.params]
        [ring.middleware.nested-params]
        [ring.middleware.session.cookie]
        [ring.middleware.json]
        [compojure.core]
        [hiccup.page]
        [hiccup.element]
        )
  (:require [compojure.route :as route])
  (:import [net.sf.jett.transform ExcelTransformer]))

(def my-session-store (cookie-store {:key "fooooooooooooooo"}))
(defn index-handler []

  (html5 ""
         [:head
          [:title "Handsontable "]
          (include-css "jquery.handsontable.full.css")
          (include-css "//netdna.bootstrapcdn.com/twitter-bootstrap/2.3.2/css/bootstrap-combined.min.css")
          (include-js "//ajax.googleapis.com/ajax/libs/jquery/1.7/jquery.min.js")
          (include-js "jquery.handsontable.full.js")
          (include-js "app.js")
          ]
         [:body
          [:div#dataTable]
          [:input#save-button {:type "button" :value "save"}]
          (link-to "excel" "dl")
          ]))

(defn save-handler [{ session :session, { data "data" } :params }]
  (let [data (or data (:data session {}))
        save-data (if (map? data) (vals (apply sorted-map (apply concat data))) data)
        session (assoc session :data save-data)],
    (->
     (response { :error nil })
     (assoc :session session))))

(defn load-handler [{session :session}]
  (let [data (session :data)]
    (response data)))

(defn excel-handler [{session :session}]
  (def DS (System/getProperty "file.separator"))
  (defn make-seeds [xs]
    (doto (java.util.HashMap.)
      (.put "data" xs)))
  (with-open [r (java.io.FileInputStream.
                   (str (System/getProperty "user.dir") DS "resources" DS "handsontable.xls"))]
    (let [transformer (ExcelTransformer.)
          workbook (.transform transformer r (make-seeds (:data session)))
          out (java.io.ByteArrayOutputStream.)]
      (.write workbook out)
      (-> (response (java.io.ByteArrayInputStream. (.toByteArray out)))
          (header "Content-Disposition" (str "attachment; filename=\"hoge.xls\""))
          (content-type "application/vnd.ms-excel")))))

(defroutes ring-compojure-test-handler
  (GET "/" [] (index-handler))
  (GET "/load" [] (->
                   load-handler
                   wrap-json-response
                   (wrap-session {:store my-session-store})))
  (POST "/save" [] (->
                    save-handler
                    wrap-nested-params
                    wrap-params
                    wrap-json-response
                    (wrap-session {:store my-session-store}) ))
  (GET "/excel" [] (->
                    excel-handler
                    (wrap-session {:store my-session-store})))
  (route/resources "/")
  (route/not-found "<h1>Page not found</h1>"))
