(ns notifier.core
  (:gen-class)
  (:import [java.awt Toolkit]))

;;; Inspired by 
;;; https://github.com/stathissideris/dotfiles/blob/master/.emacs.d/clojure/clojure-dev.clj

;;; Problems with the following code:
;;; it wants to allocate an icon, even if I only want to flash a message
;;; my "flash a message" routine will need to check & ensure that an icon is attached.

(defn icon->image [icon]
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

(defn remove-all-tray-icons []
  (let [tray (java.awt.SystemTray/getSystemTray)]
   (doseq [i (seq (.getTrayIcons tray))]
     (.remove tray i))))

(def tray-icon
  (try
    (let [icon (java.awt.TrayIcon.
                (icon->image (.getIcon (javax.swing.UIManager/getDefaults) "FileView.computerIcon"))
                #_(.getImage (java.awt.Toolkit/getDefaultToolkit) "tray.gif")
                "REPL notifications" nil)]
      (.add (java.awt.SystemTray/getSystemTray) icon)
      icon)
    (catch Exception e)))

(def tray-msg-type-map
  {:none java.awt.TrayIcon$MessageType/NONE
   :info java.awt.TrayIcon$MessageType/INFO
   :warning java.awt.TrayIcon$MessageType/WARNING
   :error java.awt.TrayIcon$MessageType/ERROR})

(defn notify
  "Send a notification to the system tray."
  [msg & [title type]]
  (let [type (or type :none)
        type (or (tray-msg-type-map type) (:none tray-msg-type-map))]
   (.displayMessage tray-icon title msg type))
  msg)

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (notify "title" "message" :info))
