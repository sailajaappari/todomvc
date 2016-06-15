(ns user
  (:require [figwheel-sidecar.repl-api :as fw]
            [todomvc.core :as core]))

(defn go []
  (fw/start-figwheel!)
  (core/start!))

(def cljs-repl fw/cljs-repl)
