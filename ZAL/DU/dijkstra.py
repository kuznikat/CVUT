 class Vertex:
    def __init__(self, id: int, name: str, ):
        self.id = id
        self.name = name
        self.edges = []
        self.minDistance = float('inf')
        self.previousVertex = None


class Edge:
    def __init__(self, source: int, target: int, weight: int):
        self.source = source
        self.target = target
        self.weight = weight


class Dijkstra:
    def __init__(self):
        self.minDistance = None
        self.vertexes = []

    def createGraph(self, vertexes, edgesToVertexes):
        self.vertexes = vertexes
        for edge in edgesToVertexes:
            for vertex in self.vertexes:
                if vertex.id == edge.source:
                    vertex.edges.append(edge)

    def getVertexes(self):
        return self.vertexes

    def computePath(self, sourceId):
        visited = []
        source_vertex = self.getsVertex(sourceId)
        source_vertex.minDistance = 0
        queue = [source_vertex]
        while len(queue) != 0:
            queue.sort(key=lambda x: x.minDistance)
            actual_vertex = queue.pop(0)
            if actual_vertex not in visited:
                visited.append(actual_vertex)

                for edge in actual_vertex.edges:
                    neighbour = self.getsVertex(edge.target)

                    if neighbour not in visited:
                        new_distance = actual_vertex.minDistance + edge.weight
                        if new_distance < neighbour.minDistance:
                            neighbour.minDistance = new_distance
                            neighbour.previousVertex = actual_vertex
                        if neighbour not in queue:
                            queue.append(neighbour)

    def getsVertex(self, id: int):
        for vertex in self.vertexes:
            if vertex.id == id:
                return vertex

    def getShortestPathTo(self, targetId):
        target = self.getsVertex(targetId)
        vertexes_list = []
        while target is not None:
            vertexes_list.append(target)
            target = target.previousVertex

        vertexes_list.reverse()
        return vertexes_list

    def resetDijkstra(self):
        for vertex in self.vertexes:
            vertex.minDistance = float('inf')
            vertex.previousVertex = None
