import RPi.GPIO as GPIO; 
import time
import PINS
import CONFIG

GPIO.setwarnings(False)
GPIO.setmode(GPIO.BCM)
GPIO.setup(PINS.alertLight, GPIO.OUT)

def blink():
    for i in range(30):
        on();
        time.sleep(CONFIG.alertLightDelay)
        off();
        time.sleep(CONFIG.alertLightDelay)

def on():
    GPIO.output(PINS.alertLight, True)

def off():
    GPIO.output(PINS.alertLight, False)
