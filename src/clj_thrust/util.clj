(ns clj-thrust.util
  (:import [java.util Locale]))

(defonce os
  (let [os-name (.toLowerCase (System/getProperty "os.name" "") Locale/ENGLISH)]
    (cond
      (or (.contains os-name "mac") (.contains os-name "darwin"))
      :mac
      (.contains os-name "win")
      :windows
      (.contains os-name "linux")
      :linux
      :else
      :unknown)))

(defn os? [target-os]
  (= os target-os))