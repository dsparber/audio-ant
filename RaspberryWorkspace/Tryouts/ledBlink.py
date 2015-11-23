import time
import RPi.GPIO as GPIO
LED = 40

GPIO.setmode(GPIO.BCM)
GPIO.setup(LED, GPIO.OUT)
GPIO.output(LED,True)
time.sleep(0.5)
GPIO.output(LED, False)
time.sleep(0.5)