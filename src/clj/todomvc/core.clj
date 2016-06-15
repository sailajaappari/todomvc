(ns todomvc.core
  (:require [todomvc.server :as server])
  (:gen-class))


(defn start! []
  (server/start!))

(defn -main []
  (start!))
