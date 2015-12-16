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

def ledYellow(on): 
    GPIO.output(LED_yell, on)
    
def ledRed(on): 
    GPIO.output(LED_red, on)

