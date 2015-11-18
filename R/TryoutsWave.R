library(tuneR)
library(seewave)

wave = readWave("/Users/daniel/Documents/Arbeiten/Diplomarbeit AudioAnt/Workspace/Audio Testfiles/Doorbell_d01.wav")

resultMFCC = melfcc(wave)

resultZCR = zcr(wave)

