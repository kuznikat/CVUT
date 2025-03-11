

class Food:
    def __init__(self, name, expiration):
        self.name = name
        self.expiration = expiration


def openFridge(fridge):
    print("Following items are in Homer's fridge:")
    for food in fridge:
        print("{0} (expires in: {1} days)".format(
            str(food.name), str(food.expiration))
        )
    print("")

# test vypisu - pri odevzdani smazte, nebo zakomentujte
# fridge = [Food("beer", 4), Food("steak", 1), Food("hamburger", 1), Food("donut", 3)]
# fridge = [ ]
# openFridge(fridge)


"""
Task #1
"""
def maxExpirationDay(fridge):
    if len(fridge) == 0:
        return -1
    max_expiration = fridge[0].expiration
    for food in fridge:
        if food.expiration > max_expiration:
            max_expiration = food.expiration
    return max_expiration

# test vypisu - pri odevzdani smazte, nebo zakomentujte
# print(maxExpirationDay(fridge))
# The command should print 4


"""
Task #2
"""
def histogramOfExpirations(fridge):
    max_expiration = maxExpirationDay(fridge)
    result = [0] * (max_expiration + 1)
    for food in fridge:
        expiration_day = food.expiration
        result[expiration_day] += 1
    return result

# test vypisu - pri odevzdani smazte, nebo zakomentujte
# print(histogramOfExpirations(fridge))
# The command should print [0, 2, 0, 1, 1]


"""
Task #3
"""
def cumulativeSum(histogram):
    cumulative = []  # Initialize an empty list for the cumulative sum
    total = 0  # Initialize a variable to track the running total

    for value in histogram:
        total += value  # Add the current value to the running total
        cumulative.append(total)  # Append the running total to the cumulative list

    return cumulative

# test vypisu - pri odevzdani smazte, nebo zakomentujte
# print(cumulativeSum([0, 2, 0, 1, 1]))
# The command should print [0, 2, 2, 3, 4]


"""
Task #4
"""
def sortFoodInFridge(fridge):
    # Create a copy of the original fridge to avoid modification
    sorted_fridge = fridge[:]

    # Get the histogram of expiration days
    histogram = histogramOfExpirations(sorted_fridge)

    # Calculate cumulative sum of the histogram
    cumulative = cumulativeSum(histogram)

    # Initialize a new list for sorted items
    sorted_items = [None] * len(fridge)

    for food in fridge:
        expiration_day = food.expiration
        pos_ind = cumulative[expiration_day] - 1
        sorted_items[pos_ind] = food
        cumulative[expiration_day] -= 1

    return sorted_items

# test vypisu - pri odevzdani smazte, nebo zakomentujte
# openFridge(sortFoodInFridge(fridge))
# The command should print
# Following items are in Homer's fridge:
# hamburger (expires in: 1 days)
# steak (expires in: 1 days)
# donut (expires in: 3 days)
# beer (expires in: 4 days)


"""
Task #5
"""
def reverseFridge(fridge):
    reversed_fridge = fridge[::-1]  # Create a reversed copy of the fridge
    return reversed_fridge


# test vypisu - pri odevzdani smazte, nebo zakomentujte
# openFridge(reverseFridge(fridge))
# The command should print
# Following items are in Homer's fridge:
# donut (expires in: 3 days)
# hamburger (expires in: 1 days)
# steak (expires in: 1 days)
# beer (expires in: 4 days)

# test vypisu - pri odevzdani smazte, nebo zakomentujte
# openFridge(sortFoodInFridge(reverseFridge(fridge)))
# The command should print
# Following items are in Homer's fridge:
# steak (expires in: 1 days)
# hamburger (expires in: 1 days)
# donut (expires in: 3 days)
# beer (expires in: 4 days)


"""
Task #6
"""
def eatFood(name, fridge):
    found_food = False
    new_fridge = fridge[:]  # Create a copy of the fridge

    min_exp = float('inf')
    to_remove = None

    for food in fridge:
        if food.name == name:
            found_food = True
            if food.expiration < min_exp:
                min_exp = food.expiration
                to_remove = food

    if to_remove:
        new_fridge.remove(to_remove)  # Remove the food item with the lowest expiration

    return new_fridge if found_food else fridge[:]


# test vypisu - pri odevzdani smazte, nebo zakomentujte
# openFridge(
#     eatFood("donut",
#         [Food("beer", 4), Food("steak", 1), Food("hamburger", 1),
#         Food("donut", 3), Food("donut", 1), Food("donut", 6)]
#     ))
# The command should print
# Following items are in Homer's fridge:
# beer (expires in: 4 days)
# steak (expires in: 1 days)
# hamburger (expires in: 1 days)
# donut (expires in: 3 days)
# donut (expires in: 6 days)
