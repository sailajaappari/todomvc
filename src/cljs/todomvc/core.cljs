(ns todomvc.core
    (:require [reagent.core :as r]
              [ajax.core :as ajax]
              [secretary.core :as secretary :refer-macros [defroute]]))

(enable-console-print!)


(def todos (r/atom []))

(def retrieve-item (r/atom nil))

(def item (r/atom nil))

(def cnt (r/atom 0))

(def rtodos (r/atom nil))


(defn inc-cnt [id]
  (swap! id inc))

(defn add-todo [text]
  (hash-map :id (inc-cnt cnt) :task text :active true))

;;Add to vector
(defn add-to-vector [item]
  (swap! todos conj item))

(def adding (do
              (add-to-vector (add-todo "clojure"))
              (add-to-vector (add-todo "html"))
              (add-to-vector (add-todo "javascript"))))

;; Remove from vector
(defn remove-from-vector [todos1 index]
  (vec (concat (subvec @todos1 0 index) (subvec @todos1 (+ index 1)))))



(defn display [todos1]
  [:div
   [:table
    (for [i (range 0 (count @todos1))]
       ^{:key i}
       [:tr
        [:td (get-in @todos1 [i :task])]
        (if (get-in @todos1 [i :active])
         [:td "P"]
         [:td "D"])       
        (if (= (get-in @todos1 [i :active]) true)
         [:td 
          [:button {:on-click #(reset! todos (swap! todos1 assoc-in [i :active] false))} "Mark Done"]]
                 
         [:td
          [:button {:on-click #(reset! todos (swap! todos1 assoc-in [i :active] true))} "Mark Pending"]])
        [:td
         [:button {:on-click #(reset! rtodos (remove-from-vector rtodos i))} "X"]]])]])
 

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

;;clear complete todos
(defn clear-complete-todos [todos1]
  (vec (remove (fn [x] (= (:active x) false)) @todos1)))




;;(js/console.log @todos)

;;(js/console.log (clj->js (retrieve-from-vector 2)))


(defn home []
  (let [
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
         [:span "Items Left: " (count (active-todos todos))]
         [:button {:on-click #(reset! rtodos @todos)} "All"]
         [:button {:on-click #(reset! rtodos (active-todos todos))} "Active"]
         [:button {:on-click #(reset! rtodos (complete-todos todos))} "Complete"]
         [:button {:on-click #(reset! rtodos (reset! todos (clear-complete-todos todos)))} "Clear Complete"]]
        [:p "temparary todos: " (str @rtodos)]
        [:p "permenent todos: " (str @todos)]]))) 


(defn ^:export main []
  (r/render-component
   [home]
   (js/document.getElementById "app")))

(main)
