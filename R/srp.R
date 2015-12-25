sum <- sum(spec[,2])

current <- 0.0
index <- 0

while (current <= (0.85 * sum)){
  
  index <- index + 1
  current = current + spec[index, 2]
  
}

spec[index,1] * 1000