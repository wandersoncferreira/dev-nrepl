(ns dev-nrepl.core
  (:require [cider.nrepl :refer [cider-middleware]]
            [nrepl.server :refer [start-server stop-server]]
            #_:clj-kondo/ignore [refactor-nrepl.middleware :refer [wrap-refactor]])
  (:gen-class))

(defn- resolve-or-fail
  [sym]
  (or (resolve sym)
      (throw (IllegalArgumentException. (format "Cannot resolve %s" sym)))))

(defn- setup-refactor-middleware
  []
  (->> (conj cider-middleware 'wrap-refactor)
       (map resolve-or-fail)
       (apply nrepl.server/default-handler)))

(defonce nrepl-server* (atom nil))

(def ^:private NREPL_PORT (or (System/getenv "NREPL_PORT") 17042))
(def ^:private ENABLE_CIDER? (or (System/getenv "ENABLE_CIDER") true))


;;; Public API

(defn start-nrepl!
  []
  (if @nrepl-server*
    (println "Could not complete action: a server is already running")
    (if ENABLE_CIDER?
      (reset! nrepl-server* (start-server :port NREPL_PORT
                                          :handler (setup-refactor-middleware)))
      (reset! nrepl-server* (start-server :port NREPL_PORT)))))


(defn stop-nrepl!
  []
  (if @nrepl-server*
    (do (stop-server @nrepl-server*)
        (reset! nrepl-server* nil))
    (println "Could not complete action: no server is currently running")))


(defn restart-nrepl!
  []
  (stop-nrepl!)
  (start-nrepl!))


(defn -main
  [& _args]
  (start-nrepl!))
