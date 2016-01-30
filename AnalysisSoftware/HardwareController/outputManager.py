import ledController as led
import alertLightController as alert

def output(function, options):
	if function == "LED":
		led.ledByOptions(options)
	
	if function == "ALERT_LIGHT":
		alert.blink()
	
	if function == "DISPLAY":
		print("Display")