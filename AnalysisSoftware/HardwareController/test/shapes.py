import time
 
import Adafruit_Nokia_LCD as LCD
import Adafruit_GPIO.SPI as SPI
import RPi.GPIO as GPIO 
	
import Image
import ImageDraw
import ImageFont

BL = 18
DC = 23
RST = 24
SPI_PORT = 0
SPI_DEVICE = 0

GPIO.setmode(GPIO.BCM)
GPIO.setup(BL, GPIO.OUT)
GPIO.output(BL, True)

# Hardware SPI usage:
disp = LCD.PCD8544(DC, RST, spi=SPI.SpiDev(SPI_PORT, SPI_DEVICE, max_speed_hz=4000000))

# Initialize library.
disp.begin(contrast=40)

# Clear display.
disp.clear()
disp.display()

# Create blank image for drawing.
image = Image.new('1', (LCD.LCDWIDTH, LCD.LCDHEIGHT))

# Get drawing object to draw on image.
draw = ImageDraw.Draw(image)

# Draw a white filled box to clear the image.
draw.rectangle((0,0,LCD.LCDWIDTH,LCD.LCDHEIGHT), outline=255, fill=255)

font = ImageFont.truetype('pixel_font-7.ttf', 20)

text ="Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet."
maxwidth, height = draw.textsize(text, font=font)

diff = maxwidth-84

draw.text((0,0), text, font=font)

if diff > 0:
	while True:
		for i in range(0,diff+84+42):
			image = Image.new('1', (LCD.LCDWIDTH, LCD.LCDHEIGHT))
			draw = ImageDraw.Draw(image)
			draw.rectangle((0,0,LCD.LCDWIDTH,LCD.LCDHEIGHT), outline=255, fill=255)
			draw.text((-i,0), text, font=font)
			draw.text((maxwidth+42-i,0), text, font=font)
			time.sleep(0.03)
			disp.clear()
			disp.image(image)
			disp.display()
		

# Display image.
disp.image(image)
disp.display()
