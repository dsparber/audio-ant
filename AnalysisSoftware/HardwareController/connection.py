import socket
import sys
from _thread import *
import ledController as led
import alertLightController as alert
import outputManager as out
import inputManager as input
 
HOST = ''		# Symbolic name meaning all available interfaces
PORT = 4207

serversocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

serversocket.bind((HOST, PORT))
serversocket.listen(5)

def sendThread(clientsocket):
	input.start(clientsocket)
				
def receiveThread(clientsocket):
	while True:
		data = clientsocket.recv(1024)
		
		if not data: 
			break

		data = str(data)[2:-1]
		
		function = data.split(';')[0]
		options = data.split(';')[1:]
		
		out.output(function, options)
 
while True:
	#wait to accept a connection - blocking call
	(clientsocket, address) = serversocket.accept()
	start_new_thread(receiveThread ,(clientsocket,))
	start_new_thread(sendThread ,(clientsocket,))