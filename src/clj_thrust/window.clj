(ns clj-thrust.window
  (:require [clj-thrust.rpc :as r]))

(defn create-window [process & {:keys [root-url size title icon-path has-frame session-id]
                                :or   {root-url "about:blank"
                                       size     {:width 400 :height 300}
                                       title    "Untitled"}}]
  (r/execute process
    (r/->create :window
      {:root-url   root-url
       :size       size
       :title      title
       :icon-path  icon-path
       :has-frame  has-frame
       :session-id session-id})))

(defn listen-closed [window handler]
  (let [target (:target window)
        k [target "closed"]]
    (swap! (get-in window [:process :event-handlers]) assoc k handler)))

(defn listen-blur [window handler]
  (let [target (:target window)
        k [target "blur"]]
    (swap! (get-in window [:process :event-handlers]) assoc k handler)))

(defn listen-focus [window handler]
  (let [target (:target window)
        k [target "focus"]]
    (swap! (get-in window [:process :event-handlers]) assoc k handler)))

(defn listen-unresponsive [window handler]
  (let [target (:target window)
        k [target "unresponsive"]]
    (swap! (get-in window [:process :event-handlers]) assoc k handler)))

(defn listen-responsive [window handler]
  (let [target (:target window)
        k [target "responsive"]]
    (swap! (get-in window [:process :event-handlers]) assoc k handler)))

(defn listen-worker-crashed [window handler]
  (let [target (:target window)
        k [target "worker_crashed"]]
    (swap! (get-in window [:process :event-handlers]) assoc k handler)))

(defn listen-remote [window handler]
  (let [target (:target window)
        k [target "remote"]]
    (swap! (get-in window [:process :event-handlers]) assoc k handler)))

(defn show [window]
  (r/call-get-result :show window))

(defn close [window]
  (r/call-get-result :close window))

(defn focus [window focus]
  (r/call-get-result :focus window {:focus focus}))

(defn maximize [window]
  (r/call-get-result :maximize window))

(defn minimize [window]
  (r/call-get-result :minimize window))

(defn restore [window]
  (r/call-get-result :restore window))

(defn set-title [window title]
  (r/call-get-result :set_title window {:title title}))

(defn set-fullscreen [window fullscreen]
  (r/call-get-result :set_fullscreen window {:fullscreen fullscreen}))

(defn set-kiosk [window kiosk]
  (r/call-get-result :set_kiosk window {:kiosk kiosk}))

(defn open-devtools [window]
  (r/call-get-result :open_devtools window))

(defn close-devtools [window]
  (r/call-get-result :close_devtools window))

(defn move [window x y]
  (r/call-get-result :move window {:x x :y y}))

(defn resize [window width height]
  (r/call-get-result :resize window {:width width :height height}))

(defn remote [window message]
  (r/call-get-result :remote window {:message message}))

(defn closed? [window]
  (r/call-get-result :is_closed window))

(defn size [window]
  (r/call-get-result :size window))

(defn position [window]
  (r/call-get-result :position window))

(defn maximized? [window]
  (r/call-get-result :is_maximized window))

(defn minimized? [window]
  (r/call-get-result :is_minimized window))

(defn fullscreen? [window]
  (r/call-get-result :is_fullscreen window))

(defn kiosked? [window]
  (r/call-get-result :is_kiosked window))

(defn devtools-opened? [window]
  (r/call-get-result :is_devtools_opened window))