(ns todomvc.dev
  (:require [todomvc.core :as core]))

(enable-console-print!)

(defn on-jsload []
  (core/main))
