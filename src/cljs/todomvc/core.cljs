(ns todomvc.core
    (:require [reagent.core :as r]
              [ajax.core :as ajax]))

(enable-console-print!)


(def todos (r/atom {:1 "clojure" :2 "html" :3 "javascript" :4 "clojurescript"}))

(defn home []
  (let [item (r/atom nil)]
    (fn []
      [:div
       [:div
        [:span
         [:input {:type "text"
                  :placeholder "todo"
                  :on-change #(reset! item (-> % .-target .-value))}]
         [:button "Add"]]]
       [:div
        [:ul
          (for [i (range 1 5)]
            ^{:key i} [:li (:i @todos)])]]]))) 




(defn ^:export main []
  (r/render-component
   [home]
   (js/document.getElementById "app")))
