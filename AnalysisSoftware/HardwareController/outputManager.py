def output(function, options):
	if function == "LED":
		led.ledByOptions(options)
	
	if function == "ALERT_LIGHT":
		alert.blink()
	
	if function == "DISPLAY":
		print("Display")