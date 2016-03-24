import RPi.GPIO as GPIO;
from thread import *
import time
import PINS
import CONFIG

class AlertLight:

    def __init__(self):
        GPIO.setup(PINS.alertLight, GPIO.OUT)
        GPIO.output(PINS.alertLight, True)
        self.running = False

    def blink(self):
        if not self.running:
            self.running = True
            start_new_thread(self.blinkThread ,())

    def blinkThread(self):
        while self.running:
            self.on();
            time.sleep(CONFIG.alertLightDelay)
            self.off();
            time.sleep(CONFIG.alertLightDelay)

    def stop(self):
        self.running = False

    def on(self):
        GPIO.output(PINS.alertLight, False)

    def off(self):
        GPIO.output(PINS.alertLight, True)
