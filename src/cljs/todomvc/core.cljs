(ns todomvc.core
    (:require [reagent.core :as r]
              [ajax.core :as ajax]))

(enable-console-print!)


(def todos (r/atom []))

(def retrieve-item (r/atom nil))

(def atodos (r/atom nil))

(def ctodos (r/atom nil))

;;Add to vector
(defn add-to-vector [item]
  (swap! todos conj item))

(def adding (do
              (add-to-vector {:id 1 :task "clojure" :active true})
              (add-to-vector {:id 2 :task "html" :active false})
              (add-to-vector {:id 3 :task "javascript" :active false})))

;; Remove from vector
(defn remove-from-vector [index]
  (reset! todos (vec (concat (subvec @todos 0 index) (subvec @todos (+ index 1))))))

;;Retrieve from vector
(defn retrieve-from-vector [id1]
  (loop [i 0]
    (if (or (>= i (count @todos) retrieve-item))
       (into {} @retrieve-item)
       (if (= id1 (get-in @todos [i :id]))
         (reset! retrieve-item (subvec @todos i (+ i 1)))
         (recur (inc i))))))

(defn display [todos1]
  [:div
   [:table
    (for [i (range 0 (count @todos1))]
      ^{:key (get-in @todos1 [i :id])}
      [:tr
       [:td (get-in @todos1 [i :task])]])]])

;;Display all todos
(defn all-todos [todos]
  (display todos1))

;;Display acitve todos
(defn active-todos [todos]
  (do
    (reset! atodos (vec (filter (fn [x] (= (:active x) true)) @todos1)))
    (display atodos)))


;;Display complete todos
(defn complete-todos [todos1]
  (do
    (reset! ctodos (vec (filter (fn [x] (= (:active x) false)) @todos1)))
    (display ctodos)))




;;(js/console.log @todos)

;;(js/console.log (clj->js (retrieve-from-vector 2)))


(defn home []
  [:div
   [:div
    [:h1 "Todo List "]]
    [:p (complete-todos todos)]]) 


(defn ^:export main []
  (r/render-component
   [home]
   (js/document.getElementById "app")))

(main)
