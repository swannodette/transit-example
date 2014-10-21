(ns transit-example.server
  (:import (java.io ByteArrayOutputStream))
  (:require [ring.util.response :refer [file-response resource-response]]
            [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.resource :refer [wrap-resource]]
            [bidi.bidi :refer [make-handler] :as bidi]
            [cognitect.transit :as transit])
  (:use ring.middleware.edn))

(defn write [x]
  (let [baos (ByteArrayOutputStream.)
        w    (transit/writer baos :json)
        _    (transit/write w x)
        ret  (.toString baos)]
    (.reset baos)
    ret))

(defn writev [x]
  (let [baos (ByteArrayOutputStream.)
        wv   (transit/writer baos :json-verbose)
        _    (transit/write wv x)
        ret  (.toString baos)]
    (.reset baos)
    ret))

(def routes
  ["" {"/" :index
       "/transit" :transit
       "/json" :json
       "/edn" :edn}])

(defn index [req]
  (assoc (resource-response "html/index.html" {:root "public"})
    :header {"Content-Type" "text/html"}))

(def sample-data [{::foobar "bar"
                   ::bazbaz "woz"
                   ::fooba1 "bar"
                   ::bazba2 "woz"}
                  {::foobar "key"
                   ::bazbaz 1
                   ::fooba1 "bar"
                   ::bazba2 "woz"}
                  {::foobar "gar"
                   ::bazbaz [1 2 3]
                   ::fooba1 "bar"
                   ::bazba2 "woz"}
                  {::foobar "cat"
                   ::bazbaz ::classified
                   ::fooba1 "bar"
                   ::bazba2 "woz"}
                  {::foobar ::zebra
                   ::bazbaz [::telephone (java.util.Date.)]
                   ::fooba1 "bar"
                   ::bazba2 "woz"}
                  {::foobar ::telephone
                   ::bazbaz ::classified
                   ::fooba1 "bar"
                   ::bazba2 "woz"}
                  {::foobar "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum."
                   ::bazbaz [1 9 10 {::foobar ::bazwozwoz}]
                   ::fooba1 "bar"
                   ::bazba2 "woz"}
                  {::foobar ::classified
                   ::bazbaz ::telephone
                   ::fooba1 "bar"
                   ::bazba2 "woz"}
                  {::foobar "bagof"
                   ::bazbaz :minecraft
                   ::fooba1 "bar"
                   ::bazba2 "woz"}
                  {::foobar 1N
                   ::bazbaz 1N
                   ::fooba1 "bar"
                   ::bazba2 "woz"}])

(defn json [req]
  {:header {"Content-Type" "application/json"}
   :body (writev sample-data)})

(defn transit [req]
  {:header {"Content-Type" "application/transit+json"}
   :body   (write sample-data)})

(defn edn [req]
  {:header {"Content-Type" "application/edn"}
   :body (pr-str sample-data)})

(defn app-routes [req]
  (let [match (bidi/match-route
                routes
                (:uri req)
                :request-method (:request-method req))]
    (case (:handler match)
      :index (index req)
      :transit (transit req)
      :json (json req)
      :edn (edn req)
      req)))

(def handler
  (-> (wrap-resource app-routes "public")
      wrap-edn-params))

(defonce server (run-jetty #'handler {:port 8081 :join? false}))


