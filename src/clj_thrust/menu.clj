(ns clj-thrust.menu
  (:require [clj-thrust.rpc :as r]))

(defn create-menu [process]
  (r/execute process
    (r/->create :menu)
    {:menu-item-id (atom 0)}))

(defn add-check-item [menu command-id label]
  (r/call-get-result :add_check_item menu
    {:command-id command-id
     :label label}))

(defn add-radio-item [menu command-id label]
  (r/call-get-result :add_radio_item menu
    {:command-id command-id
     :label label}))

(defn add-separator [menu]
  (r/call-get-result :add_separator menu))

(defn set-checked [menu command-id value]
  (r/call-get-result :set_checked menu
    {:command-id command-id
     :value value}))

(defn set-enabled [menu command-id value]
  (r/call-get-result :set_enabled menu
    {:command-id command-id
     :value value}))

(defn set-visible [menu command-id value]
  (r/call-get-result :set_visible menu
    {:command-id command-id
     :value value}))

(defn set-accelerator [menu command-id accelerator]
  (r/call-get-result :set_accelerator menu
    {:command-id command-id
     :accelerator accelerator}))

(defn add-submenu [menu menu-id label]
  (let [menu-item-id (:menu-item-id menu)]
    (swap! menu-item-id inc)
    (r/call-get-result :add_submenu menu
      {:menu-id menu-id
       :label label
       :command-id @menu-item-id})
    @menu-item-id))

(defn clear [menu]
  (r/call-get-result :clear menu))

(defn popup [menu window-id]
  (r/call-get-result :popup menu {:window-id window-id}))

(defn set-application-menu [menu]
  (r/call-get-result :set_application_menu menu))

(defn add-item [menu label & {:keys [accelerator]}]
  (let [menu-item-id (:menu-item-id menu)]
    (swap! menu-item-id inc)
    (r/call :add_item menu
      {:command-id @menu-item-id
       :label label})
    (when accelerator
      (set-accelerator menu @menu-item-id accelerator))
    @menu-item-id ; TODO: Possibly return a map here, like {:id id, etc}
    ))

(defn listen-execute [menu handler]
  (let [target (:target menu)
        k [target "execute"]]
    (swap! (get-in menu [:process :event-handlers]) assoc k handler)))