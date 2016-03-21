import subprocess
import time
from thread import *
from math import exp
import CONFIG

class Sound:

    def __init__(self):
        self.startVolume = CONFIG.soundStartVolume
        self.running = False

    def play(self, soundpath):
        if not self.running:
            self.soundpath = soundpath
            self.running = True
            start_new_thread(self.playThread ,())
            start_new_thread(self.volumeThread ,())

    def stop(self):
        self.running = False

    def playThread(self):
        while self.running:
            subprocess.call(["scripts/play.sh", self.soundpath])
            time.sleep(0.2)

    def volumeThread(self):
        i = 0.0
        while self.running:
            diff = 100 - self.startVolume
            volume = self.startVolume + diff * (1 - exp(-1*i/(diff/2)))
            subprocess.call(["amixer", "-q", "-c","0","set", "PCM", "--", str(volume)+"%"])
            i = i+1
            time.sleep(1)
