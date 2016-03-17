import socket
import sys
from thread import *
import outputManager as out
from inputManager import InputManager

from display import Display

HOST = ''		# Symbolic name meaning all available interfaces
print "Port: "
PORT = input()

serversocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

serversocket.bind((HOST, PORT))
serversocket.listen(5)

inManager = InputManager()

def sendThread(clientsocket):
	inManager.addSocket(clientsocket)

def receiveThread(clientsocket):
	while True:
		data = clientsocket.recv(1024)

		if not data:
			break

		data = str(data)

		for line in data.split("\r\n"):

			function = line.split(';')[0]
			options = line.split(';')[1:]

			out.output(function, options)

def displayClock():
        d = Display()
        d.light(True)
        d.displayClock()

while True:
	#wait to accept a connection - blocking call
	(clientsocket, address) = serversocket.accept()
	start_new_thread(receiveThread ,(clientsocket,))
	start_new_thread(sendThread ,(clientsocket,))
	start_new_thread(displayClock ,())
