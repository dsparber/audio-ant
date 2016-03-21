import time
import datetime
from thread import *

import Adafruit_Nokia_LCD as LCD
import Adafruit_GPIO.SPI as SPI
import RPi.GPIO as GPIO

import Image
import ImageDraw
import ImageFont

import PINS
import CONFIG

class Display:

    def __init__(self):

        GPIO.setup(PINS.DisplayBL, GPIO.OUT)

        self.disp = LCD.PCD8544(PINS.DisplayDC, PINS.DisplayRST, spi=SPI.SpiDev(PINS.DisplaySPI_PORT, PINS.DisplaySPI_DEVICE, max_speed_hz=4000000))
        self.disp.begin(contrast=50)
        self.disp.clear()
        self.disp.display()

        self.image = Image.new('1', (LCD.LCDWIDTH, LCD.LCDHEIGHT))
        self.draw = ImageDraw.Draw(self.image)
        self.draw.rectangle((0,0,LCD.LCDWIDTH,LCD.LCDHEIGHT), outline=255, fill=255)
        self.font = ImageFont.truetype(CONFIG.displayFont, 20)

        self.displayingText = False
        self.showClock = False
        self.text = ""

    def light(self, on):
        GPIO.output(PINS.DisplayBL, on)

    def displayClock(self):
        self.displayingText = False
        self.showClock = False
        time.sleep(CONFIG.displaySleepTime*2)
        start_new_thread(self.clockThread ,())

    def clockThread(self):
        self.text = ""
        self.showClock = True

        while self.showClock:

            currentTime = datetime.datetime.time(datetime.datetime.now())
            text = "%02d" % (currentTime.hour,) + ":" + "%02d" % (currentTime.minute,)

            self.font = ImageFont.truetype(CONFIG.displayFont, 30)
            maxwidth, height = self.draw.textsize(text, font=self.font)

            self.draw.rectangle((0,0,84,48), outline=255, fill=255)
            self.set(text, (84-maxwidth)/2, (48-height)/2, 30)

            time.sleep(CONFIG.displaySleepTime)
        self.cleanup()

    def cleanup(self):
        self.displayingText = False
        self.showClock = False
        self.draw.rectangle((0,0,84,48), outline=255, fill=255)
        self.disp.image(self.image)
        self.disp.display()

    def addText(self, text):
        if self.text == "":
            self.text = text
        else:
            self.text = self.text + ", " + text

    def setText(self, text):
        self.text = text

    def displayText(self):
        self.showClock = False
        time.sleep(CONFIG.displaySleepTime*2)
        if not self.displayingText:
            start_new_thread(self.writeThread ,(20,30))

    def writeThread(self, y, size):

        self.displayingText = True
        self.set(CONFIG.displayText,0,0,20)

        maxwidth, height = self.draw.textsize(self.text, font=self.font)
        self.font = ImageFont.truetype(CONFIG.displayFont, size)
        self.draw.rectangle((0,y,LCD.LCDWIDTH,height), outline=255, fill=255)
        self.disp.image(self.image)
        self.disp.display()

        text = self.text

        while self.displayingText:

            maxwidth, height = self.draw.textsize(self.text, font=self.font)
            diff = maxwidth-84

            if diff > 0:
                for i in range(0,diff+84+21):

                    if not text == self.text:
                        text = self.text
                        break

                    if self.displayingText:
                         self.set(self.text,-i,y,size)
                         self.set(self.text,maxwidth+21-i,y,size)
                         time.sleep(CONFIG.displaySleepTime)
                    else:
                         break
            else:
                self.set(self.text,0,y,size)
                time.sleep(CONFIG.displaySleepTime)

    def set(self, text, x, y, size):

        self.font = ImageFont.truetype(CONFIG.displayFont, size)

        maxwidth, height = self.draw.textsize(text, font=self.font)
        self.draw.rectangle((x,y,maxwidth+x,height+y), outline=255, fill=255)

        self.draw.text((x,y), text, font=self.font)

        self.disp.image(self.image)
        self.disp.display()
