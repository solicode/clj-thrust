(ns clj-thrust.core
  (:require [clj-thrust.rpc :as r]
            [clj-thrust.util :refer [os]]
            [clojure.java.io :as io]
            [clojure.string :as s])
  (:import [java.io FileNotFoundException]
           [clj_thrust NullOutputStream]))

(def thrust-executable-name
  (case os
    :windows "thrust_shell.exe"
    :linux "thrust_shell"
    :mac "ThrustShell.app/Contents/MacOS/ThrustShell"))

; TODO: Check for Thrust shell in the other locations that Thrust bindings authors have chosen?
; https://github.com/breach/thrust/issues/264
(defn locate-thrust-shell [thrust-shell-path]
  (let [locations [(io/file thrust-shell-path)
                   (io/file (System/getProperty "user.home") ".thrust" thrust-executable-name)]
        found (first (filter #(when % (.exists %)) locations))]
    (if found
      (str found)
      (throw (FileNotFoundException.
               (str "Could not locate Thrust shell at locations:\n"
                    (s/join "\n" (filter some? locations))
                    \newline
                    "If you do not have a Thrust runtime, you can download one here: https://github.com/breach/thrust/releases"))))))

(defn create-process [& [thrust-shell-path]]
  (let [thrust-shell-path (locate-thrust-shell thrust-shell-path)
        thrust-shell (-> (ProcessBuilder. [thrust-shell-path]) (.start))
        pending-requests (atom {})
        current-id (atom 0)
        event-handlers (atom {})
        process {:shell            thrust-shell
                 :pending-requests pending-requests
                 :current-id       current-id
                 :event-handlers   event-handlers}]
    (future
      (io/copy (.getInputStream thrust-shell) (r/rpc-output process)))

    (future
      ; TODO: Add verbose option?
      ;(io/copy (.getErrorStream thrust-shell) *out*)

      ; If you're wondering why this is necessary, this comes straight from the documentation for
      ; Java's Process class:
      ;
      ; "Because some native platforms only provide limited buffer size for standard input and
      ;  output streams, failure to promptly write the input stream or read the output stream of
      ;  the subprocess may cause the subprocess to block, or even deadlock."
      ;
      ; And I was indeed seeing these deadlocks before this line was added.
      (io/copy (.getErrorStream thrust-shell) (NullOutputStream.)))
    process))

(defn destroy-process [process]
  (.destroy (:shell process)))