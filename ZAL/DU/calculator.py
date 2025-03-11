import math

ADDITION = "ADDITION"
SUBTRACTION = "SUBTRACTION"
MULTIPLICATION = "MULTIPLICATION"
DIVISION = "DIVISION"
MOD = "MOD"
SECOND_POWER = "SECONDPOWER"
POWER = "POWER"
SECOND_RADIX = "SECONDRADIX"
MAGIC = "MAGIC"

VALUE_ERROR_EXCEPTION_MESSAGE = "This operation is not supported for given input parameters"

def addition(x, y):
    try:
        result = float(x + y)
        return result
    except ValueError:
        raise ValueError(VALUE_ERROR_EXCEPTION_MESSAGE)


def subtraction(x, y):
    try:
        result = float(x - y)
        return result
    except ValueError:
        raise ValueError(VALUE_ERROR_EXCEPTION_MESSAGE)


def multiplication(x, y):
    try:
        result = float(x * y)
        return result
    except ValueError:
        raise ValueError(VALUE_ERROR_EXCEPTION_MESSAGE)


def division(x, y):
    try:
        if y != 0:
            result = float(x / y)
            return result
        else:
            raise ValueError(VALUE_ERROR_EXCEPTION_MESSAGE)
    except ValueError:
        raise ValueError(VALUE_ERROR_EXCEPTION_MESSAGE)


def modulo(x, y):
    try:
        if x >= y and y > 0:
            result = float(x % y)
            return result
        else:
            raise ValueError(VALUE_ERROR_EXCEPTION_MESSAGE)
    except ValueError:
        raise ValueError(VALUE_ERROR_EXCEPTION_MESSAGE)


def secondPower(x):
    try:
        result = float(x * x)
        return result
    except ValueError:
        raise ValueError(VALUE_ERROR_EXCEPTION_MESSAGE)


def power(x, y):
    try:
        if y >= 0:
            result = float(x ** y)
            return result
        else:
            raise ValueError(VALUE_ERROR_EXCEPTION_MESSAGE)
    except ValueError:
            raise ValueError(VALUE_ERROR_EXCEPTION_MESSAGE)



def secondRadix(x):
    try:
        if x > 0:
            result = float(math.sqrt(x))
            return result
        else:
            raise ValueError(VALUE_ERROR_EXCEPTION_MESSAGE)
    except ValueError:
            raise ValueError(VALUE_ERROR_EXCEPTION_MESSAGE)


def magic(x, y, z, k):
    try:
        l = float(x + k)
        m = float(y + z)

        if m != 0:
            n = float((l / m) + 1)
            return n
        else:
            raise ValueError(VALUE_ERROR_EXCEPTION_MESSAGE)
    except ValueError:
        raise ValueError(VALUE_ERROR_EXCEPTION_MESSAGE)



def control(a, x, y, z, k):
    if a == ADDITION:
        return addition(x, y)
    elif a == SUBTRACTION:
        return subtraction(x, y)
    elif a == MULTIPLICATION:
        return multiplication(x, y)
    elif a == DIVISION:
        return division(x, y)
    elif a == MOD:
        return modulo(x, y)
    elif a == POWER:
        return power(x, y)
    elif a == SECOND_POWER:
        return secondPower(x)
    elif a == SECOND_RADIX:
        return secondRadix(x)
    elif a == MAGIC:
        return magic(x, y, z, k)
    else:
        raise ValueError(VALUE_ERROR_EXCEPTION_MESSAGE)







