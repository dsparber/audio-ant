import socket
import sys
from thread import *
from outputManager import OutputManager
from inputManager import InputManager

HOST = ''		# Symbolic name meaning all available interfaces
PORT = 4207

serversocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

serversocket.bind((HOST, PORT))
serversocket.listen(5)

outManager = OutputManager()
inManager = InputManager(outManager)

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

			outManager.execute(function, options)
			
while True:
	#wait to accept a connection - blocking call
	(clientsocket, address) = serversocket.accept()
	start_new_thread(receiveThread ,(clientsocket,))
	start_new_thread(sendThread ,(clientsocket,))
