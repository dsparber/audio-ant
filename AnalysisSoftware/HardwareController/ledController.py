import RPi.GPIO as GPIO
import time
import CONFIG
import PINS
from thread import *

GPIO.setwarnings(False)
GPIO.setmode(GPIO.BCM)

GPIO.setup(PINS.ledWifi, GPIO.OUT)
GPIO.setup(PINS.ledBluetooth, GPIO.OUT)
GPIO.setup(PINS.ledHotspot, GPIO.OUT)

GPIO.setup(PINS.ledRunning, GPIO.OUT)
GPIO.setup(PINS.ledRecording, GPIO.OUT)
GPIO.setup(PINS.ledRecordingFailed, GPIO.OUT)
GPIO.setup(PINS.ledRecordingSuccess, GPIO.OUT)

ledBlinking = {}

def setLed(pin, on):
    ledBlinking[pin] = False
    GPIO.output(pin, on)

def ledBlink(pin):
    ledBlinking[pin] = True
    start_new_thread(ledBlinkThread ,(pin,))

def ledBlinkThread(pin):
    while ledBlinking[pin]:
        GPIO.output(pin, True)
        time.sleep(CONFIG.ledBlinkDelay)
        GPIO.output(pin, False)
        time.sleep(CONFIG.ledBlinkDelay)

def ledWifi(on):
    setLed(PINS.ledWifi, on)
def ledBluetooth(on):
    setLed(PINS.ledBluetooth, on)
def ledHotspot(on):
    setLed(PINS.ledHotspot, on)

def ledWifiBlink():
    ledBlink(PINS.ledWifi)
def ledHotspotBlink():
    ledBlink(PINS.ledHotspot)

def ledRunning(on):
    setLed(PINS.ledRunning, on)
def ledRecording(on):
    setLed(PINS.ledRecording, on)
def ledRecordingFailed(on):
    setLed(PINS.ledRecordingFailed, on)
def ledRecordingSuccess(on):
    setLed(PINS.ledRecordingSuccess, on)

def led(ledName, option):

	option = int(option)

	if option > 1:
		for i in range(0,option):
			led(ledName, 1)
			time.sleep(CONFIG.ledBlinkDelay)
			led(ledName, 0)
			time.sleep(CONFIG.ledBlinkDelay)
	else:
		ledOn = option != 0

		if(ledName == "LED_BLUETOOTH"):
			ledBluetooth(ledOn)
		elif(ledName == "LED_WIFI"):
			ledWifi(ledOn)
		elif(ledName == "LED_HOTSPOT"):
			ledHotspot(ledOn)
		elif (ledName == "LED_RUNNING"):
			ledRunning(ledOn)
		elif (ledName == "LED_RECORDING"):
			ledRecording(ledOn)
		elif (ledName == "LED_RECORDING_FAILED"):
			ledRecordingFailed(ledOn)
		elif (ledName == "LED_RECORDING_SUCCESS"):
			ledRecordingSuccess(ledOn)
		else:
			print ("LED not defined")

def ledByOptions(options):
    led(options[0], options[1])
