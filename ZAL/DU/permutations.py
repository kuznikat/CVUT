def permutations(array: list):
    result = []
    if len(array) == 0:
        return [array[:]]

    for i in range(len(array)):
        first_value = array.pop(0)
        perms = permutations(array)
        for perm in perms:
            result.append([first_value] + perm)
        array.append(first_value)
    return result
