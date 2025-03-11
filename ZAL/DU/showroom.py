class Car:
    def __init__(self, identification: int, name: str, brand: str, price: int, active: bool):
        self.identification = identification
        self.name = name
        self.brand = brand
        self.price = price
        self.active = active

    def __str__(self):
        return f"Car: identification{self.identification}, name{self.name}, brand{self.brand}, price{self.price}, active {self.active}"


class Node:
    def __init__(self, data: Car):
        self.nextNode = None
        self.prevNode = None
        self.data = data

    def __str__(self):
        return str(self.data)


class LinkedList:
    def __init__(self):
        self.head = None

    def push(self, data: Car):
        new_node = Node(data)
        if self.head is None:
            self.head = new_node
        elif self.head.data.price > new_node.data.price:
            new_node.nextNode = self.head
            self.head.prevNode = new_node
            self.head = new_node
        else:
            tmp_node = self.head
            while tmp_node.nextNode is not None and tmp_node.nextNode.data.price <= new_node.data.price:
                tmp_node = tmp_node.nextNode
            new_node.nextNode = tmp_node.nextNode
            new_node.prevNode = tmp_node
            if tmp_node.nextNode is not None:
                tmp_node.nextNode.prevNode = new_node
            tmp_node.nextNode = new_node


db = LinkedList()


def init(cars: list[Car]):
    for car in cars:
        db.push(car)


def getDatabase():
    return db


def getDatabaseHead():
    return db.head


def clean():
    db.head = None


def add(data: Car):
    db.push(data)


def updateName(identification, name):
    tmp_node = db.head
    while tmp_node:
        if tmp_node.data.identification == identification:
            tmp_node.data.name = name
            return
        tmp_node = tmp_node.nextNode
    return None


def updateBrand(identification, brand):
    tmp_node = db.head
    while tmp_node:
        if tmp_node.data.identification == identification:
            tmp_node.data.brand = brand
            return
        tmp_node = tmp_node.nextNode
    return None


def activateCar(identification):
    tmp_node = db.head
    while tmp_node:
        if tmp_node.data.identification == identification:
            tmp_node.data.active = True
            return
        tmp_node = tmp_node.nextNode
    return None


def deactivateCar(identification):
    tmp_node = db.head
    while tmp_node:
        if tmp_node.data.identification == identification:
            tmp_node.data.active = False
            return
        tmp_node = tmp_node.nextNode
    return None


def calculateCarPrice():
    total_price = 0
    tmp_node = db.head
    while tmp_node:
        if tmp_node.data.active:
            total_price += tmp_node.data.price
        tmp_node = tmp_node.nextNode
    return total_price


def print_cars():
    tmp_node = db.head
    while tmp_node:
        print(tmp_node)
        tmp_node = tmp_node.nextNode


if __name__ == '__main__':

    car1 = Car(23, "Octavia2", "Skoda", 123000, True)
    car2 = Car(88, "Felicia", "Skoda", 5000, True)
    car3 = Car(82, "Superb", "Skoda", 54000, False)
    car4 = Car(12, "156", "Alfa Romeo", 250000, True)
    car5 = Car(86, "Brera", "Alfa Romeo", 250000, True)
    car6 = Car(19287, "370Z", "Mazda", 250000, True)


    cars_list = [car1, car2, car3, car4, car5, car6]
    init(cars_list)


    print_cars()
    print(calculateCarPrice())

    updateName(88,'Arrr')
    updateBrand(82, 'Mers')
    activateCar(82)
    deactivateCar(23)
    print_cars()




