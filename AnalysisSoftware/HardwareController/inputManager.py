from _thread import *
import RPi.GPIO as GPIO  
GPIO.setmode(GPIO.BOARD)
GPIO.setup(5, GPIO.IN, pull_up_down=GPIO.PUD_UP) 

def send(text, socket):
	text = text + "\r\n"
	if socket.send(text.encode('utf-8')) == 0:
		print("Sending failed (" + text + ")")

def sendThread(buttonname, pin, socket):
	while True:	
		GPIO.wait_for_edge(pin, GPIO.FALLING) 
		GPIO.wait_for_edge(pin, GPIO.RISING)
		try:
			send("BUTTON;" + buttonname, socket)
		except (BrokenPipeError, IOError):
			break

def start(socket):
	start_new_thread(sendThread ,("BUTTON_RECORDING", 5, socket,))