package demo

import java.awt.GraphicsEnvironment
import java.awt.SystemTray
import java.awt.TrayIcon
import java.awt.image.BufferedImage
import java.lang.Thread
import javax.swing.Icon
import javax.swing.UIManager

fun main(args: Array<String>) {
    println("Creating system tray icon...")
    if (SystemTray.isSupported()) {
        val icon = createTrayIcon()
        icon.displayMessage("Title", "Message", TrayIcon.MessageType.INFO)
//        sleep(3000)
//        val sysTray = SystemTray.getSystemTray()
//        sysTray.remove(icon)
        val runnable = TrayIconRunner(icon, 3000)
        val t = Thread(runnable)
        t.isDaemon = true
        t.start()
    }
}

class TrayIconRunner : Runnable {

    val icon: TrayIcon
    val delayMillis: Long

    constructor(icon: TrayIcon, delayMillis: Long = 3000) {
        this.icon = icon
        this.delayMillis = delayMillis
    }

    override fun run() {
        Thread.sleep(delayMillis);
        val sysTray = SystemTray.getSystemTray()
        sysTray.remove(icon)
    }
}


fun iconToImage(icon: Icon): BufferedImage {
    val w = icon.iconWidth
    val h = icon.iconHeight
    val ge = GraphicsEnvironment.getLocalGraphicsEnvironment()
    val gd = ge.defaultScreenDevice
    val gc = gd.defaultConfiguration
    val img = gc.createCompatibleImage(w, h)
    val gfx = img.createGraphics()
    icon.paintIcon(null, gfx, 0, 0)
    gfx.dispose()
    return img
}

fun createTrayIcon(): TrayIcon {
    val uiDefaults = UIManager.getDefaults()
    val image = iconToImage(uiDefaults.getIcon("FileView.computerIcon"))
    val icon = TrayIcon(image, "Java", null)
    val sysTray = SystemTray.getSystemTray()
    sysTray.add(icon)
    return icon
}
