import RPi.GPIO as GPIO; 
import time
lightPin = 3; 
delay = 0.06

GPIO.setwarnings(False)
GPIO.setmode(GPIO.BOARD)
GPIO.setup(lightPin, GPIO.OUT)

def blink():
    for i in range(30):
        on();
        time.sleep(delay)
        off();
        time.sleep(delay)

def on():
    GPIO.output(lightPin, True)

def off():
    GPIO.output(lightPin, False)
					
