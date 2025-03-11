class Node:
    def __init__(self, value: int):
        self.value = value
        self.left = None
        self.right = None


class BinarySearchTree:
    def __init__(self):
        self.root = None
        self.visited_nodes = 0

    def insert(self, value: int):
        new_node = Node(value)
        if self.root is None:
            self.root = new_node
        else:
            current = self.root
            while True:

                if value < current.value:
                    if current.left is None:
                        current.left = new_node
                        break
                    else:
                        current = current.left

                elif value > current.value:
                    if current.right is None:
                        current.right = new_node
                        break
                    else:
                        current = current.right

                else:
                    break

    def fromArray(self, array):
        for value in array:
            self.insert(value)

    def search(self, value: int):
        current = self.root
        self.visited_nodes = 0
        while current is not None:
            self.visited_nodes += 1
            if value == current.value:
                return True
            elif value < current.value:
                current = current.left
            else:
                current = current.right
        return False

    def min(self):
        current = self.root
        self.visited_nodes = 1
        if current is None:
            return None
        while current.left is not None:
            self.visited_nodes += 1
            current = current.left
        return current.value

    def max(self):
        current = self.root
        self.visited_nodes = 1
        if current is None:
            return None
        while current.right is not None:
            self.visited_nodes += 1
            current = current.right
        return current.value

    def visitedNodes(self):
        return self.visited_nodes





