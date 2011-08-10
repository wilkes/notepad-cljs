(ns notepad.core
  (:require [goog.dom :as dom]
            [goog.ui.Zippy :as zippy]))

(defn make-note-dom [parent {:keys [title content]}]
  (let [header-element (dom/createDom "div"
                                      (.strobj {"style"
                                                "background-color:#EEE"})
                                      title)
        content-element (dom/createDom "div" nil content)
        new-note (dom/createDom "div" nil header-element content-element)]
    (dom/appendChild parent new-note)
    (goog.ui.Zippy. header-element content-element)))

(defn make-notes [data note-container]
  (doseq [d data]
    (make-note-dom note-container d)))

(defn main []
  (make-notes [{:title "Note 1"
                 :content "Content of Note 1"}
                {:title "Note 2"
                 :content "Content of Note 2"}]
              (dom/$ "notes")))

(main)
