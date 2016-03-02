import RPi.GPIO as GPIO 
import time 
import CONFIG
import PINS

GPIO.setwarnings(False)
GPIO.setmode(GPIO.BOARD)

GPIO.setup(PINS.ledWifi, GPIO.OUT)
GPIO.setup(PINS.ledBluetooth, GPIO.OUT)
GPIO.setup(PINS.ledHotspot, GPIO.OUT)

GPIO.setup(PINS.ledRunning, GPIO.OUT)
GPIO.setup(PINS.ledRecording, GPIO.OUT)
GPIO.setup(PINS.ledRecordingFailed, GPIO.OUT)
GPIO.setup(PINS.ledRecordingSuccess, GPIO.OUT)

def ledWifi(on): 
    GPIO.output(PINS.ledWifi, on)
def ledBluetooth(on): 
    GPIO.output(PINS.ledBluetooth, on)
def ledHotspot(on): 
    GPIO.output(PINS.ledHotspot, on)

def ledRunning(on):
    GPIO.output(PINS.ledRunning, on)	
def ledRecording(on):
    GPIO.output(PINS.ledRecording, on)    
def ledRecordingFailed(on):
    GPIO.output(PINS.ledRecordingFailed, on)	
def ledRecordingSuccess(on):
    GPIO.output(PINS.ledRecordingSuccess, on)
		
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
