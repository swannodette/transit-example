(ns transit-example.core
  (:require [cognitect.transit :as t]
            [cljs.reader :as reader])
  (:import [goog.net XhrIo]))

(enable-console-print!)

(def r (t/reader :json))

(defn get-data [url cb]
  (XhrIo.send (str "/" url)
    (fn [e]
      (let [xhr (.-target e)]
        (cb (.getResponseText xhr))))))

(get-data "json"
  (fn [res]
    (println "JSON.parse")
    (println res)
    (time
      (dotimes [x 50000]
        (.parse js/JSON res)))))

(get-data "transit"
  (fn [res]
    (println "transit read")
    (println res)
    (time
      (dotimes [x 50000]
        (t/read r res)))))
