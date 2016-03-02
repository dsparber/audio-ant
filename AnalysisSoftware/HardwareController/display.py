import time
 
import Adafruit_Nokia_LCD as LCD
import Adafruit_GPIO.SPI as SPI
import RPi.GPIO as GPIO 
    
import Image
import ImageDraw
import ImageFont

import PINS

class Display:
    
    def __init__(self):
        
        GPIO.setmode(GPIO.BOARD)
        GPIO.setup(PINS.DisplayBL, GPIO.OUT)
        
        self.disp = LCD.PCD8544(PINS.DisplayDC, PINS.DisplayRST, spi=SPI.SpiDev(PINS.DisplaySPI_PORT, PINS.DisplaySPI_DEVICE, max_speed_hz=4000000))
        self.disp.begin(contrast=64)
        self.disp.clear()
        self.disp.display()
        
        self.image = Image.new('1', (LCD.LCDWIDTH, LCD.LCDHEIGHT))
        self.draw = ImageDraw.Draw(image)
        self.draw.rectangle((0,0,LCD.LCDWIDTH,LCD.LCDHEIGHT), outline=255, fill=255)
        self.font = ImageFont.truetype('fonts/pixel_font-7.ttf', 20)
        
        self.displayText = False
        
    def light(self, on):
        GPIO.output(PINS.DisplayBL, on)
        
        
    def write(self, text, y, amp):
        self.displayText == False
        
        self.font = ImageFont.truetype('fonts/pixel_font-7.ttf', 20*amp)
        
        maxwidth, height = draw.textsize(text, font=font)
        diff = maxwidth-84
        
        self.draw.rectangle((0,y,LCD.LCDWIDTH,height), outline=255, fill=255)
        self.disp.image(self.image)
        self.disp.display()
        
        time.sleep(CONFIG.displaySleepTime*5)
        self.displayText == True
        
        if diff > 0:
             while self.displayText == True:
                 for i in range(0,diff+84+42):
                     displayText(text,-i,y,amp)
                     displayText(text,maxwidth+height*1.5-i,y,amp)
                     if self.displayText == False:
                         break
                     time.sleep(CONFIG.displaySleepTime)        
        else:
            displayText(text,0,y,amp)
            
    def displayText(self, text, x, y, amp):
        
        self.font = ImageFont.truetype('fonts/pixel_font-7.ttf', 20*amp)
        
        maxwidth, height = draw.textsize(text, font=self.font)    
        self.draw.rectangle((0,y,LCD.LCDWIDTH,height), outline=255, fill=255)
        
        self.draw.text((x,y), text, font=self.font)
        
        self.disp.clear()
        self.disp.image(self.image)
        self.disp.display()      