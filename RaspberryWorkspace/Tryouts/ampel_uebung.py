import time
import RPi.GPIO as GPIO
LED_green = 36; 
LED_yell = 38; 
LED_red = 40; 

GPIO.setwarnings(False)
GPIO.setmode(GPIO.BOARD)
GPIO.setup(LED_red, GPIO.OUT)
GPIO.setup(LED_yell, GPIO.OUT)
GPIO.setup(LED_green, GPIO.OUT)

while 1: 
    GPIO.output(LED_red,True)
    time.sleep(5)
    GPIO.output(LED_yell,True)
    time.sleep(1)
    GPIO.output(LED_red, False)
    GPIO.output(LED_yell, False)
    GPIO.output(LED_green,True)
    time.sleep(5)
    GPIO.output(LED_green, False)
    GPIO.output(LED_red, True)