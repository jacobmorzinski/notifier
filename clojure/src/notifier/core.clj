(ns notifier.core
  (:gen-class)
  (:import [java.awt Toolkit]))

;;; Inspired by 
;;; https://github.com/stathissideris/dotfiles/blob/master/.emacs.d/clojure/clojure-dev.clj

(defn- icon->image [icon]
  (let [w (.getIconWidth icon)
        h (.getIconHeight icon)
        img (-> (java.awt.GraphicsEnvironment/getLocalGraphicsEnvironment)
                (.getDefaultScreenDevice)
                (.getDefaultConfiguration)
                (.createCompatibleImage w h))
        gfx (.createGraphics img)
        icon (.paintIcon icon nil gfx 0 0)]
    (.dispose gfx)
    img))

(defn- remove-all-tray-icons []
  (let [tray (java.awt.SystemTray/getSystemTray)]
   (doseq [i (seq (.getTrayIcons tray))]
     (.remove tray i))))

(def ^:private tray-msg-type-map
  {:none java.awt.TrayIcon$MessageType/NONE
   :info java.awt.TrayIcon$MessageType/INFO
   :warning java.awt.TrayIcon$MessageType/WARNING
   :error java.awt.TrayIcon$MessageType/ERROR})

(defn- create-trayicon
  "Create a tray icon."
  []
  (let [icon (java.awt.TrayIcon.
              (icon->image (.getIcon (javax.swing.UIManager/getDefaults) "FileView.computerIcon"))
              "Clojure" nil)]
    (.add (java.awt.SystemTray/getSystemTray) icon)
    icon))

(defn- display-message
  "Send a message to the trayIcon."
  [^java.awt.TrayIcon trayicon message & [title type]]
  (.displayMessage trayicon title message type)
  trayicon)

(defn- remove-trayicon-in-future
  "Remove the trayicon, after a delay."
  [^java.awt.TrayIcon trayicon delayMilliseconds]
  (future
    (Thread/sleep delayMilliseconds)
    (.remove (java.awt.SystemTray/getSystemTray) trayicon)))

(defn notify
  "Show a notification in the system tray / notification area."
  [message & [title type]]
  (when (java.awt.SystemTray/isSupported)
    (let [type (or type :none)
          type (or (tray-msg-type-map type) (:none tray-msg-type-map))]
      (-> (create-trayicon)
          (display-message message title type)
          (remove-trayicon-in-future 5000)
          (deref)))))

(defn -main
  "I don't do a whole lot ... yet."
  [& [message title type]]
  (let [message (or message "Example Message")
        title (or title "Example Title")
        type (keyword type)]
    (notify title message type)))
