
Add-Type -AssemblyName System.Windows.Forms

$notifyIcon = New-Object System.Windows.Forms.NotifyIcon 

$notifyIcon.Icon = "C:\Users\Jacob\Documents\GitHub\DesktopNotifier\DesktopNotifier\Resources\CommandLineIcon.ico"
$notifyIcon.BalloonTipIcon = "Info"
$notifyIcon.BalloonTipText = "Message goes here."
$notifyIcon.BalloonTipTitle = "Title"

$notifyIcon.Visible = $True
$notifyIcon.ShowBalloonTip(10000)

