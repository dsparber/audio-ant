from thread import *
import RPi.GPIO as GPIO  
import PINS

GPIO.setwarnings(False)
GPIO.setmode(GPIO.BCM)

def send(text, socket):
	text = text + "\r\n"
	if socket.send(text.encode('utf-8')) == 0:
		print("Sending failed (" + text + ")")

def sendThread(buttonname, pin, socket):
	GPIO.setup(pin, GPIO.IN, pull_up_down=GPIO.PUD_UP) 
	while True:	
		try:
			GPIO.wait_for_edge(pin, GPIO.FALLING) 
			GPIO.wait_for_edge(pin, GPIO.RISING)
			
			send("BUTTON;" + buttonname, socket)
		except (BrokenPipeError, IOError):
			break

def start(socket):
	start_new_thread(sendThread ,("BUTTON_RECORDING", PINS.buttonRecording, socket,))
