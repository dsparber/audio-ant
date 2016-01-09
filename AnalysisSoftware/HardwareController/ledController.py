import RPi.GPIO as GPIO; 
LED_green = 36; 
LED_yell = 38; 
LED_red = 40; 

GPIO.setwarnings(False)
GPIO.setmode(GPIO.BOARD)
GPIO.setup(LED_red, GPIO.OUT)
GPIO.setup(LED_yell, GPIO.OUT)
GPIO.setup(LED_green, GPIO.OUT)

def ledGreen(on): 
    GPIO.output(LED_green, on)
    print("Green")

def ledYellow(on): 
    GPIO.output(LED_yell, on)
    print("Yellow")
    
def ledRed(on): 
    GPIO.output(LED_red, on)
    print("Red")
		
def led(ledName, ledOn):
	if(ledName == "LED_BLUETOOTH_STATUS"):
		ledYellow(ledOn)
	elif(ledName == "LED_POWER_STATUS"):
		ledGreen(ledOn)
	elif(ledName == "LED_WIFI_STATUS"):
		ledRed(ledOn)
	elif (ledName == "LED_RECORDING"):
		ledRed(ledOn)
	elif (ledName == "LED_ALERT"):
		ledGreen(ledOn)
	else: 
		print ("LED not defined")				
        
def ledByOptions(options):
    led(options[0], options[1])