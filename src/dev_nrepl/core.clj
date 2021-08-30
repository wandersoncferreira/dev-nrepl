(ns dev-nrepl.core
  (:gen-class)
  (:require [cider.enrich-classpath]
            [cider.nrepl :refer [cider-middleware]]
            [nrepl.server :refer [start-server stop-server]]
            [refactor-nrepl.middleware]))

(defonce nrepl-server* (atom nil))

(def ^:private NREPL_PORT (or (System/getenv "NREPL_PORT") 17042))
(def ^:private ENABLE_CIDER? (or (System/getenv "ENABLE_CIDER") "true"))
(def ^:private ENABLE_SOURCE_ENRICHMENT? (or (System/getenv "ENABLE_SOURCE_ENRICHMENT") "true"))
(def ^:private cider? (= "true" ENABLE_CIDER?))
(def ^:private enriched? (= "true" ENABLE_SOURCE_ENRICHMENT?))

;; resolve middlewares

(defn- resolve-or-fail
  [sym]
  (or (resolve sym)
      (throw (IllegalArgumentException. (format "Cannot resolve %s" sym)))))

(defn- setup-middlewares
  []
  (let [mdw-refactor (conj cider-middleware 'refactor-nrepl.middleware/wrap-refactor)
        middlewares (if enriched?
                      (conj mdw-refactor 'cider.enrich-classpath/middleware)
                      mdw-refactor)]
    (->> middlewares
         (map resolve-or-fail)
         (apply nrepl.server/default-handler))))


;;; Public API

(defn start-nrepl!
  []
  (if @nrepl-server*
    (println "Could not complete action: a server is already running")
    (if cider?
      (do (reset! nrepl-server* (start-server :port NREPL_PORT
                                              :handler (setup-middlewares)))
          (println "NREPL (cider:enabled) started on PORT: " NREPL_PORT))
      (do (reset! nrepl-server* (start-server :port NREPL_PORT))
          (println "NREPL (cider:disabled) started on PORT: " NREPL_PORT)))))


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
