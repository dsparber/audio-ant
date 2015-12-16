
#!/usr/bin/env python
 
import socket
import ledController as led
 
HOST = "172.20.10.9"
PORT = 4207
 
sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
sock.connect((HOST, PORT))
 
while (1):
    data = sock.recv(1024)
    ledName = str(data).split(';')[0][2:]
    ledOn = str(data).split(';')[1][:-1] == "true"
		
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
    
