import RPi.GPIO as GPIO; 
import time
lightPin = 36; 

GPIO.setwarnings(False)
GPIO.setmode(GPIO.BOARD)
GPIO.setup(lightPin, GPIO.OUT)

def blink():
    for i in range(30):
        on();
        time.sleep(0.05)
        off();
        time.sleep(0.05)

def on():
    GPIO.output(lightPin, True)

def off():
    GPIO.output(lightPin, False)
					