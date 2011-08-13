(ns notepad.core
  (:require [goog.dom :as dom]
            [goog.ui.Zippy :as zippy]
            [goog.events :as events]
            [goog.events.EventType :as event-type]))

(defn show [el]
  (set! (.. el style display) "inline"))

(defn hide [el]
  (set! (.. el style display) "none"))

(defn create-note [title content]
  (let [header-element (dom/createDom "div"
                                      (.strobj {"style"
                                                "background-color:#EEE"})
                                      title)
        content-element (dom/createDom "div" nil content)
        editor-element (dom/createDom "textarea")
        save-button (dom/createDom "input" (.strobj {"type" "button"
                                                     "value" "Save"}))
        editor-container (dom/createDom "div"
                                        (.strobj {"style" "display:none"})
                                        editor-element
                                        save-button)

        content-container (dom/createDom "div" nil
                                         content-element editor-container)
        container (dom/createDom "div" nil header-element content-container)]
    (atom {:content content
           :header-element header-element
           :content-element content-element
           :save-button save-button
           :editor-element editor-element
           :editor-container editor-container
           :content-container content-container
           :container container})))

(defn edit-note [note]
  (fn [e]
    (dom/setTextContent (:editor-element @note) (:content @note))
    (hide (:content-element @note))
    (show (:editor-container @note))))

(defn save-note [note]
   (fn [e]
     (swap! note assoc :content (.value (:editor-element @note)))
     (dom/setTextContent (:content-element @note) (:content @note))
     (show (:content-element @note))
     (hide (:editor-container @note))))

(defn make-note-dom [parent {:keys [title content]}]
  (let [note (create-note title content)]
    (dom/appendChild parent (:container @note))
    (events/listen (:content-element @note) event-type/CLICK (edit-note note))
    (events/listen (:save-button @note) event-type/CLICK (save-note note))
    (goog.ui.Zippy. (:header-element @note) (:content-element @note))))

(defn make-notes [data note-container]
  (dom/appendChild note-container (dom/createDom "h3" nil "Notes"))
  (doseq [d data]
    (make-note-dom note-container d)))

(defn main []
  (make-notes [{:title "Note 1"
                 :content "Content of Note 1"}
                {:title "Note 2"
                 :content "Content of Note 2"}]
              (dom/getElement "notes")))

(main)
