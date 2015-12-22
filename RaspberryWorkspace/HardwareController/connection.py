
#!/usr/bin/env python
 
import socket
import sys
from _thread import *
import ledController as led
 
HOST = ''		# Symbolic name meaning all available interfaces
PORT = 4207

serversocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

serversocket.bind((HOST, PORT))
serversocket.listen(5)

def ledManager(clientsocket):
	while True:
		data = clientsocket.recv(1024)
		
		if not data: 
			break
		
		ledName = str(data).split(';')[0][2:]
		ledOn = str(data).split(';')[1][:-1] == "true"

		print(data)
		
		if(ledName == "BLUETOOTH_STATUS"):
			led.ledYellow(ledOn)
		elif(ledName == "POWER_STATUS"):
			led.ledGreen(ledOn)
		elif(ledName == "WIFI_STATUS"):
			led.ledRed(ledOn)
		elif (ledName == "ALERT"):
			led.ledGreen(ledOn)
		else: 
			print ("LED not defined")
			
	clientsocket.close()
 
while True:
	#wait to accept a connection - blocking call
	(clientsocket, address) = serversocket.accept()
	start_new_thread(ledManager ,(clientsocket,))