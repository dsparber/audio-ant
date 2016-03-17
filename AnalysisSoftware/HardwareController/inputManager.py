from thread import *
import RPi.GPIO as GPIO
import PINS
import time

class InputManager:

	def __init__(self):

		# Socket list
		self.sockets = []

		# GPIO setup
		GPIO.setwarnings(False)
		GPIO.setmode(GPIO.BCM)

		# Button Dict
		self.buttons = {}

		# Add buttons
		self.addButton("BUTTON_RECORDING", PINS.buttonRecording)
		self.addButton("BUTTON_WIFI", PINS.buttonWifi)
		self.addButton("BUTTON_HOTSPOT", PINS.buttonBluetooth)
		self.addButton("BUTTON_BLUETOOTH", PINS.buttonHotspot)

	def buttonAction(self, name):
		if name == "BUTTON_RECORDING":
			self.send("BUTTON;" + name)
		else:
			print name

	def addButton(self, name, pin):
		GPIO.setup(pin, GPIO.IN, pull_up_down=GPIO.PUD_UP)
		self.buttons[pin] = name
		GPIO.add_event_detect(pin, GPIO.FALLING, callback=self.callback, bouncetime=300)

	def callback(self, channel):
		self.buttonAction(self.buttons[channel])

	def addSocket(self, socket):
		self.sockets.append(socket)

	def send(self, text):
		text = text + "\r\n"
		for socket in self.sockets:
			try:
				if socket.send(text.encode('utf-8')) == 0:
					print("Sending failed (" + text + ")")
			except IOError:
				self.sockets.remove(socket)
