
def polyEval(poly: list[float], x):
    polynom = poly[0]
    for i in range(1, len(poly)):
        polynom += (x ** i) * poly[i]
    return polynom


def polySum(poly1: list[float], poly2: list[float]):
    result: list[float] = []
    if len(poly1) < len(poly2):
        poly1 += [0] * (len(poly2) - len(poly1))
    elif len(poly2) < len(poly1):
        poly2 += [0] * (len(poly1) - len(poly2))
    for i in range(0, len(poly2)):
        result.append(poly1[i] + poly2[i])
    while result[-1] == 0:
        result.pop()
    return result


def polyMultiply(poly1, poly2):
    result = [0]*((len(poly1) + len(poly2)) - 1)
    degree_1 = len(poly1) - 1
    degree_2 = len(poly2) - 1
    for i in range(len(poly1)):
        for p in range(len(poly2)):
            result[i + p] += poly1[i] * poly2[p]
    return result