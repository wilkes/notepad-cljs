(ns notepad.core
  (:require [goog.dom :as dom]
            [goog.ui.Zippy :as zippy]
            [goog.events :as events]
            [goog.events.EventType :as event-type]))

(defn make-note-dom [parent {:keys [title content]}]
  (let [content (atom content)
        header-element (dom/createDom "div"
                                      (.strobj {"style"
                                                "background-color:#EEE"})
                                      title)
        content-element (dom/createDom "div" nil @content)
        editor-element (dom/createDom "textarea")
        save-button (dom/createDom "input" (.strobj {"type" "button"
                                                     "value" "Save"}))
        editor-container (dom/createDom "div"
                                        (.strobj {"style" "display:none"})
                                        editor-element
                                        save-button)
        content-container (dom/createDom "div" nil
                                         content-element editor-container)
        new-note (dom/createDom "div" nil header-element content-container)]
    (dom/appendChild parent new-note)
    (events/listen content-element event-type/CLICK
                   (fn [e]
                     (set! (.. editor-element value) @content)
                     (set! (.. content-element style display) "none")
                     (set! (.. editor-container style display) "inline")))
    (events/listen save-button event-type/CLICK
                   (fn [e]
                     (reset! content (.value editor-element))
                     (set! (.. content-element innerHTML) @content)
                     (set! (.. content-element style display) "inline")
                     (set! (.. editor-container style display) "none")))
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
