(defproject net.solicode/clj-thrust "0.1.0-SNAPSHOT"
  :description "Clojure bindings for Thrust."
  :url "https://github.com/solicode/clj-thrust"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :java-source-paths ["src-java"]
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [cheshire "5.5.0"]]
  :profiles {:dev {:dependencies [[criterium "0.4.3"]]
                   :jvm-opts ["-XX:-OmitStackTraceInFastThrow"]}
             :1.5 {:dependencies [[org.clojure/clojure "1.5.1"]]}
             :1.7 {:dependencies [[org.clojure/clojure "1.7.0-RC1"]]}})
