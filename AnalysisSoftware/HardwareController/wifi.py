import subprocess
import ledController as led
import time


class WifiController:

	def __init__(self, outputManager):

		self.outputManager = outputManager

		self.essid = self.getNetwork()
		self.ap = self.isHotspotOn()
		self.up = self.isOn()

		self.setLeds()

	def setLeds(self):
		led.ledHotspot(self.isHotspotOn())
		led.ledWifi(self.isOn())

	def getNetwork(self):
		self.essid = subprocess.check_output("scripts/essid.sh")[:-1]
		return self.essid

	def addNetwork(self, ssid, psk):
		f = open("/etc/wpa_supplicant/wpa_supplicant.conf")
		backuptext = f.read()
		f.close()
		f = open("/etc/wpa_supplicant/wpa_supplicant.conf", "w")
		f.write("network={\n")
		f.write("\tssid=\"")
		f.write(ssid)
		f.write("\"\n\tpsk=\"")
		f.write(psk)
		f.write("\"\n}\n\n")
		f.write(backuptext)
		f.close()

	def getIP(self):
		self.ip = subprocess.check_output("scripts/ip.sh")[:-1]
		return self.ip

	def isOn(self):
		self.up = subprocess.check_output("scripts/ifup.sh")[:-1] == "True"
		return self.up

	def isHotspotOn(self):
		self.ap = subprocess.check_output("scripts/ap.sh")[:-1] == "active" and self.getNetwork() == "AudioAnt" and self.getIP() == "192.168.42.1"
		return self.ap

	def toggle(self):
		if self.isOn():
			self.off()
		else:
			self.on()
		self.setLeds()

	def toggleHotspot(self):
		if self.isOn():
			if self.isHotspotOn():
				self.hotspotOff()
			else:
				self.hotspotOn()
			self.setLeds()

	def on(self):
		led.ledWifiBlink()
		subprocess.check_output("scripts/wifiUp.sh")
		time.sleep(10)
		self.setLeds()

	def off(self):
		led.ledWifiBlink()
		subprocess.check_output("scripts/wifiDown.sh")
		self.setLeds()

	def hotspotOn(self):
		led.ledHotspotBlink()
		subprocess.check_output("scripts/enableHotspot.sh")
		self.setLeds()

	def hotspotOff(self):
		led.ledHotspotBlink()
		subprocess.check_output("scripts/disableHotspot.sh")
		self.setLeds()
