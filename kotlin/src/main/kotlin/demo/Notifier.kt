package demo

import java.awt.image.BufferedImage
import java.awt.GraphicsDevice
import java.awt.GraphicsEnvironment
import java.awt.SystemTray
import java.awt.TrayIcon
import javax.swing.Icon
import javax.swing.UIManager

fun main(args: Array<String>) {
    println("Creating system tray icon...")
    if (SystemTray.isSupported()) {
        var icon = createTrayIcon()
        icon.displayMessage("Title", "Message", TrayIcon.MessageType.INFO)
        Thread.sleep(3000)
        var sysTray = SystemTray.getSystemTray()
        sysTray.remove(icon)
    }
}

fun iconToImage(icon: Icon): BufferedImage {
    var w = icon.getIconWidth()
    var h = icon.getIconHeight()
    var ge = GraphicsEnvironment.getLocalGraphicsEnvironment()
    var gd = ge.getDefaultScreenDevice()
    var gc = gd.getDefaultConfiguration()
    var img = gc.createCompatibleImage(w, h)
    var gfx = img.createGraphics()
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
