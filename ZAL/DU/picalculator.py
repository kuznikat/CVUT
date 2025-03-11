

def leibnizPi(iteration):
    numerator = 4
    denominator = 1
    result = 0
    znam = -1
    for i in range(iteration):
        if znam == - 1:
            znam = + 1
        elif znam == + 1:
            znam = - 1
        denominator = 2 * i + 1
        a = numerator/denominator
        result += znam * a
    return result






def nilakanthaPi(iterations):
    result = 3
    numerator = 4
    divider = 2
    sign = 1
    for i in range(iterations - 1):
        a = numerator / (divider * (divider + 1) * (divider + 2))
        result += sign * a
        sign = sign * (-1)
        divider += 2
    return result






import math
def newtonPi(init):
    x_k0 = init
    x_k1 = init - (math.sin(init) / (math.cos(init)))
    while x_k1 != x_k0:
        x_k0, x_k1 = x_k1, x_k0
        x_k0 = init - (math.sin(init) / (math.cos(init)))
        init = x_k0
    return x_k1








