import RPi.GPIO as GPIO
import time
import CONFIG
import PINS
from thread import *

class LedController:

    def __init__(self):
        GPIO.setup(PINS.ledWifi, GPIO.OUT)
        GPIO.output(PINS.ledWifi, GPIO.OUT)
        GPIO.setup(PINS.ledBluetooth, GPIO.OUT)
        GPIO.output(PINS.ledBluetooth, GPIO.OUT)
        GPIO.setup(PINS.ledHotspot, GPIO.OUT)
        GPIO.output(PINS.ledHotspot, GPIO.OUT)

        GPIO.setup(PINS.ledRunning, GPIO.OUT)
        GPIO.output(PINS.ledRunning, GPIO.OUT)
        GPIO.setup(PINS.ledRecording, GPIO.OUT)
        GPIO.output(PINS.ledRecording, GPIO.OUT)
        GPIO.setup(PINS.ledRecordingFailed, GPIO.OUT)
        GPIO.output(PINS.ledRecordingFailed, GPIO.OUT)
        GPIO.setup(PINS.ledRecordingSuccess, GPIO.OUT)
        GPIO.output(PINS.ledRecordingSuccess, GPIO.OUT)

        self.ledBlinking = {}

    def setLed(self, pin, on):
        self.ledBlinking[pin] = False
        GPIO.output(pin, on)

    def ledBlink(self, pin):
        self.ledBlinking[pin] = True
        start_new_thread(ledBlinkThread ,(pin,))

    def ledBlinkThread(self, pin):
        while ledBlinking[pin]:
            GPIO.output(pin, True)
            time.sleep(CONFIG.ledBlinkDelay)
            GPIO.output(pin, False)
            time.sleep(CONFIG.ledBlinkDelay)

    def ledWifi(self, on):
        self.setLed(PINS.ledWifi, on)
    def ledBluetooth(self, on):
        self.setLed(PINS.ledBluetooth, on)
    def ledHotspot(self, on):
        self.setLed(PINS.ledHotspot, on)

    def ledWifiBlink(self):
        self.ledBlink(PINS.ledWifi)
    def ledHotspotBlink(self):
        self.ledBlink(PINS.ledHotspot)

    def ledRunning(self, on):
        self.setLed(PINS.ledRunning, on)
    def ledRecording(self, on):
        self.setLed(PINS.ledRecording, on)
    def ledRecordingFailed(self, on):
        self.setLed(PINS.ledRecordingFailed, on)
    def ledRecordingSuccess(self, on):
        self.setLed(PINS.ledRecordingSuccess, on)

    def led(self, ledName, option):

    	option = int(option)

    	if option > 1:
    		for i in range(0,option):
    			self.led(ledName, 1)
    			time.sleep(CONFIG.ledBlinkDelay)
    			self.led(ledName, 0)
    			time.sleep(CONFIG.ledBlinkDelay)
    	else:
    		ledOn = option != 0

    		if(ledName == "LED_BLUETOOTH"):
    			self.ledBluetooth(ledOn)
    		elif(ledName == "LED_WIFI"):
    			self.ledWifi(ledOn)
    		elif(ledName == "LED_HOTSPOT"):
    			self.ledHotspot(ledOn)
    		elif (ledName == "LED_RUNNING"):
    			self.ledRunning(ledOn)
    		elif (ledName == "LED_RECORDING"):
    			self.ledRecording(ledOn)
    		elif (ledName == "LED_RECORDING_FAILED"):
    			self.ledRecordingFailed(ledOn)
    		elif (ledName == "LED_RECORDING_SUCCESS"):
    			self.ledRecordingSuccess(ledOn)
    		else:
    			print ("LED not defined")

    def ledByOptions(self, options):
        led(options[0], options[1])
