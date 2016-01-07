
#!/usr/bin/env python
 
import socket
import sys
from _thread import *
# import ledController as led
 
HOST = ''		# Symbolic name meaning all available interfaces
PORT = 4207

serversocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

serversocket.bind((HOST, PORT))
serversocket.listen(5)


def sendThread(clientsocket):
	
	while True:
		text = input()
		text += "\r\n"
		print(text)
		clientsocket.send(text.encode('utf-8'))

def receiveThread(clientsocket):
	while True:
		data = clientsocket.recv(1024)
		
		if not data: 
			break

		data = str(data)[2:-1]
		
		function = data.split(';')[0]
		options = data.split(';')[1:]
		
		if function == "LED":
			
			ledName = options[0]
			ledOn = options[1] == "true"
		
			# led(ledName, ledOn)
 
while True:
	#wait to accept a connection - blocking call
	(clientsocket, address) = serversocket.accept()
	start_new_thread(receiveThread ,(clientsocket,))
	start_new_thread(sendThread ,(clientsocket,))