from ledController import LedController
from alertLightController import AlertLight
from wifi import WifiController
from sound import Sound
from display import Display
import RPi.GPIO as GPIO

class OutputManager:

	def __init__(self):

		GPIO.setwarnings(False)
		GPIO.cleanup()
		GPIO.setmode(GPIO.BCM)

		self.alertLight = AlertLight()
		self.sound = Sound()
		self.led = LedController()

		self.display = Display()
		self.display.light(True)
		self.display.displayClock()

		self.wifi = WifiController(self)
		self.led.ledWifi(self.wifi.isOn())
		self.led.ledHotspot(self.wifi.isHotspotOn())

	def execute(self,function, options):

		if function == "LED":
			self.led.ledByOptions(options)

		if function == "ALERT_LIGHT":
			self.alertLight.blink()

		if function == "SOUND":
			self.sound.play(str(options[0]))

		if function == "DISPLAY":
			self.display.addText(str(options[0]))
			self.display.displayText()

		if function == "BUTTON":
			if str(options[0]) == "BUTTON_CONFIRM":
				self.resetAlerts()

		if function == "WIFI":
			self.wifi.addNetwork(options[0], options[1])

	def resetAlerts(self):
		self.sound.stop()
		self.alertLight.stop()
		self.display.displayClock()
