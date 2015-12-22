import RPi.GPIO as GPIO

GPIO.setmode(GPIO.BOARD)
GPIO.setup(40, GPIO.OUT)
GPIO.output(40,True)
GPIO.setup(38, GPIO.OUT)
GPIO.output(38,True)
GPIO.setup(36, GPIO.OUT)
GPIO.output(36,True)