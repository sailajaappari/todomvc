(ns todomvc.core
    (:require [reagent.core :as r]
              [ajax.core :as ajax]
              [secretary.core :as secretary :refer-macros [defroute]]))

(enable-console-print!)


(def todos (r/atom []))

(def retrieve-item (r/atom nil))

(def item (r/atom nil))

(def cnt (r/atom 0))

(defn add-todo [text]
  (let [id (swap! cnt inc)]
    (sorted-map :id id :task text :active true)))

;;Add to vector
(defn add-to-vector [item]
  (swap! todos conj item))

(def adding (do
              (add-to-vector (add-todo "clojure"))
              (add-to-vector (add-todo "html"))
              (add-to-vector {:id cnt :task "javascript" :active false})))

(defn display [todos1]
  [:div
   [:table
    (for [i (range 0 (count @todos1))]
      ^{:key (get-in @todos1 [i :id])}
      [:tr
       [:td (get-in @todos1 [i :task])]])]])

;; Remove from vector
(defn remove-from-vector [index]
  (do
    (reset! todos (vec (concat (subvec @todos 0 index) (subvec @todos (+ index 1)))))
    (display todos)))

;;Retrieve from vector
(defn retrieve-from-vector [id1]
  (loop [i 0]
    (if (or (>= i (count @todos) retrieve-item))
       (into {} @retrieve-item)
       (if (= id1 (get-in @todos [i :id]))
         (reset! retrieve-item (subvec @todos i (+ i 1)))
         (recur (inc i))))))



;;Display all todos
(defn all-todos [todos1]
  (display todos1))

;;Display acitve todos
(defn active-todos [todos1]
   (vec (filter (fn [x] (= (:active x) true)) @todos1)))
     


;;Display complete todos
(defn complete-todos [todos1]
  (vec (filter (fn [x] (= (:active x) false)) @todos1)))




;;(js/console.log @todos)

;;(js/console.log (clj->js (retrieve-from-vector 2)))


(defn home []
  (let [rtodos (r/atom [])
        task (r/atom nil)]
    (fn []
       [:div
        [:div
         [:h1 "Todo List "]]
        [:div
         [:span
          [:input {:type "text"
                     :on-change #(reset! task (-> % .-target .-value))}]
          [:button {:on-click #(swap! todos conj (add-todo @task))} "Add"]]]

            ;; [:p (all-todos todos)]
        [:div
         [:p (display rtodos)]]
        [:span 
         [:button {:on-click #(reset! rtodos @todos)} "All"]
         [:button {:on-click #(reset! rtodos (active-todos todos))} "Active"]
         [:button {:on-click #(reset! rtodos (complete-todos todos))} "Complete"]]
        [:p (str @rtodos)]]))) 


(defn ^:export main []
  (r/render-component
   [home]
   (js/document.getElementById "app")))

(main)
