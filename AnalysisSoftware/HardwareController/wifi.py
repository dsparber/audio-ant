import subprocess

class WifiController:

	__init__(self):
		self.essid = subprocess.check_output("./essid.sh")[:-1]
		self.ap = subprocess.check_output("./ap.sh")[:-1]
		self.up = subprocess.check_output("./ifup.sh")[:-1]

	def getNetwork(self):
		self.essid = subprocess.check_output("./essid.sh")[:-1]
		return self.essid

	def isOn(self):
		self.up = subprocess.check_output("./ifup.sh")[:-1]
		return self.up

	def isHotspotOn(self):
		self.ap = subprocess.check_output("./ap.sh")[:-1]
		return self.ap

	def on():
		subprocess.call(["sudo ifup wlan0","sudo service hostapd restart"])

	def off():
		subprocess.call("sudo ifdown wlan0")

	def hotspotOn():
		subprocess.call("./enableHotspot.sh")

	def hotspotOff():
		subprocess.call("./disableHotspot.sh")
