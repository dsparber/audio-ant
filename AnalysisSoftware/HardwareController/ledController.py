import RPi.GPIO as GPIO 
import time 
delay = 0.15

LED_green = 36 
LED_yell = 38 
LED_red = 40

LED_warning = 7
LED_recording = 8
LED_success = 10

GPIO.setwarnings(False)
GPIO.setmode(GPIO.BOARD)

GPIO.setup(LED_red, GPIO.OUT)
GPIO.setup(LED_yell, GPIO.OUT)
GPIO.setup(LED_green, GPIO.OUT)

GPIO.setup(LED_warning, GPIO.OUT)
GPIO.setup(LED_recording, GPIO.OUT)
GPIO.setup(LED_success, GPIO.OUT)

def ledGreen(on): 
    GPIO.output(LED_green, on)

def ledYellow(on): 
    GPIO.output(LED_yell, on)
    
def ledRed(on): 
    GPIO.output(LED_red, on)

def ledWarning(on):
    GPIO.output(LED_warning, on)
		
def ledRecording(on):
    GPIO.output(LED_recording, on)
		
def ledSuccess(on):
    GPIO.output(LED_success, on)
		
def led(ledName, option):
	
	option = int(option)
	
	if option > 1:
		for i in range(0,option):
			led(ledName, 1)
			time.sleep(delay)
			led(ledName, 0)
			time.sleep(delay)
	else:
		ledOn = option != 0

		if(ledName == "LED_BLUETOOTH_STATUS"):
			ledYellow(ledOn)
		elif(ledName == "LED_POWER_STATUS"):
			ledGreen(ledOn)
		elif(ledName == "LED_WIFI_STATUS"):
			ledRed(ledOn)
		elif (ledName == "LED_RECORDING"):
			ledRecording(ledOn)
		elif (ledName == "LED_WARNING"):
			ledWarning(ledOn)
		elif (ledName == "LED_SUCCESS"):
			ledSuccess(ledOn)
		else: 
			print ("LED not defined")				
        
def ledByOptions(options):
    led(options[0], options[1])
