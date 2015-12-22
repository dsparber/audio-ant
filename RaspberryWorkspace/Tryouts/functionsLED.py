import RPi.GPIO as GPIO; 
LED_green = 36; 
LED_yell = 38; 
LED_red = 40; 

GPIO.setwarnings(False)
GPIO.setmode(GPIO.BOARD)
GPIO.setup(LED_red, GPIO.OUT)
GPIO.setup(LED_yell, GPIO.OUT)
GPIO.setup(LED_green, GPIO.OUT)

def LEDgreenOn(): 
    GPIO.output(LED_green, True)
    
def LEDgreenOff(): 
    GPIO.output(LED_green, False)

def LEDyellOn(): 
    GPIO.output(LED_yell, True)
    
def LEDyellOff(): 
    GPIO.output(LED_yell, False)
    
def LEDredOn(): 
    GPIO.output(LED_red, True)
    
def LEDredOff(): 
    GPIO.output(LED_red, False)
    
LEDgreenOn()
LEDyellOn()
LEDredOn()
