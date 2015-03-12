(ns clj-thrust.rpc
  (:require [cheshire.core :as c]
            [clojure.java.io :as io])
  (:import [clj_thrust RpcWriter RpcListener RpcEvent]
           [java.io StringReader]))

(defn rpc-output [process]
  (let [{:keys [pending-requests]} process]
    (RpcWriter.
      (proxy [RpcListener] []
        (received [^RpcEvent rpc-event]
          (let [event (c/parse-string (.getJson rpc-event) true)]
            (case (:_action event)
              "reply"
              (let [id (:_id event)
                    original-request (get @pending-requests id)]
                (deliver (:promise original-request)
                  (merge
                    (:context original-request)
                    {:target  (get-in event [:_result :_target])
                     :process process
                     :id      (:_id event)
                     :result  (:_result event)
                     :error   (:_error event)}))
                (swap! pending-requests dissoc id))

              "event"
              (let [target (:_target event)
                    event-type (:_type event)
                    k [target event-type]
                    handler (get @(:event-handlers process) k)
                    ]
                (when handler (handler (assoc (:_event event) :target target)))))))))))

(defn ->create [type & [args]]
  {:_action "create"
   :_type   (name type)
   :_args   args})

(defn ->call [method target & [args]]
  {:_target target
   :_action "call"
   :_method method
   :_args   args})

(defn execute-async
  "Executes an RPC request asynchronously, returning a promise which
  will have the response populated when it's ready."
  [process request & [context]]
  (let [id (swap! (:current-id process) inc)
        p {:promise (promise)
           :context context}
        request (assoc request :_id id)]
    (io/copy (StringReader.
               (str
                 (c/generate-string request {:key-fn #(clojure.string/replace (name %) "-" "_")})
                 \newline
                 RpcWriter/BOUNDARY
                 \newline))
      (.getOutputStream (:shell process)))
    (swap! (:pending-requests process) assoc id p)
    p))

(defn execute [process request & [context]]
  @(:promise (execute-async process request context)))

(defn call [method object & [args]]
  (execute (:process object) (->call method (:target object) args)))

(defn call-get-result [method object & [args]]
  (get-in (call method object args) [:response :_result]))
