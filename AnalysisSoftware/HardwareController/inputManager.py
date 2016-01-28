from _thread import *

def send(text, socket):
	text = text + "\r\n"
	try:
		if socket.send(text.encode('utf-8')) == 0:
			print("Sending failed (" + text + ")")
	except (BrokenPipeError, IOError):
		print("Broken pipe")

def sendThread(buttonname, pin, socket):
	while True:
		
		# TODO: button pressed
		send(button, socket)

def start(socket):
	start_new_thread(sendThread ,("BUTTON_RECORDING", 36, socket,))