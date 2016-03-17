import ledController as led
import alertLightController as alert
from wifi import WifiController
from display import Display

class OutputManager:

	def __init__(self):

		self.wifi = WifiController(self)
		self.display = Display()
		self.display.light(True)
		self.display.displayClock()

		led.ledWifi(self.wifi.isOn())
		led.ledHotspot(self.wifi.isHotspotOn())

	def execute(self,function, options):

		if function == "LED":
			led.ledByOptions(options)

		if function == "ALERT_LIGHT":
			alert.blink()

		if function == "DISPLAY":
			self.display.write(options, 0, 20)
