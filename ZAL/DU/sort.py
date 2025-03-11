

def sortNumbers(lst: list[int], condition: str):
    if condition == 'ASC':
        for i in range(len(lst)):
            for j in range(0, len(lst) - 1 - i):
                if lst[j] > lst[j + 1]:
                    lst[j], lst[j + 1] = lst[j + 1], lst[j]
        return lst

    elif condition == 'DESC':
        for p in range(len(lst)):
            for n in range(p, 0, -1):
                if lst[n] > lst[n - 1]:
                    lst[n], lst[n - 1] = lst[n - 1], lst[n]
        return lst


def sortData(reputation: list[int], data: list[str], condition: str):
    if len(reputation) != len(data):
        raise ValueError('Invalid input data')
    if condition == 'ASC':
        for i in range(len(reputation)):
            for p in range(0, len(reputation) - 1 - i):
                if reputation[p] > reputation[p + 1]:
                    reputation[p], reputation[p + 1] = reputation[p + 1], reputation[p]
                    data[p], data[p + 1] = data[p + 1], data[p]
        return data

    elif condition == 'DESC':
        for n in range(len(reputation)):
            for j in range(n, 0, -1):
                if reputation[j] > reputation[j - 1]:
                    reputation[j], reputation[j - 1] = reputation[j - 1], reputation[j]
                    data[j], data[j - 1] = data[j - 1], data[j]
        return data
