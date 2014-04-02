package de.codepitbull.worlddomination.routing.nav

import static java.lang.Math.abs
import static java.lang.Math.sqrt

/**
 *
 * @author Jochen Mader
 */
class AStarBasedPathfinder {
    EnvironmentMapWithWeight map
    PriorityQueue<WeightedPosition> openSet
    Set<WeightedPosition> closedSet

    List<WeightedPosition> route(WeightedPosition start, WeightedPosition end, EnvironmentMapWithWeight map) {
        this.map=map
        openSet = new PriorityQueue<>()
        closedSet = new HashSet<>()

        start.costToGetHere=0
        start.distance=distanceBetweenTwoNodes(start, end)
        openSet.add(start)
        while(!openSet.isEmpty()) {
            WeightedPosition currentNode = openSet.min()
            openSet.remove(currentNode)
            closedSet.add(currentNode)
            if(currentNode.equals(end))
                break
            expandNode(currentNode)
        }

        List<WeightedPosition> path= new ArrayList<>()
        if(end.predecessor) {
            WeightedPosition current = end.predecessor
            while (current!=start) {
                path.add(current)
                current=current.predecessor
            }
            path.add(current)
        }
        return path.reverse()
    }

    private void expandNode(WeightedPosition currentNode) {
        getSuccessors(currentNode).each { successor ->
            if(!openSet.contains(successor) && !closedSet.contains(successor)) {
                successor.setPredecessor(currentNode)
                successor.setCostToGetHere(currentNode.costToGetHere+calculateMovementCostBetweenTwoNodes(currentNode, successor))
                successor.setDistance(distanceBetweenTwoNodes(currentNode, successor))
                openSet.add(successor)
            }
            else if(openSet.contains(successor)) {
                Float newCost = currentNode.costToGetHere+calculateMovementCostBetweenTwoNodes(currentNode, successor)
                if(successor.costToGetHere>newCost) {
                    successor.costToGetHere=newCost
                    successor.predecessor=currentNode
                }
            }
        }
    }

    private Double distanceBetweenTwoNodes(WeightedPosition node1, WeightedPosition node2) {
        Integer absA = abs(node1.x - node2.x);
        Integer absB = abs(node2.y - node2.y);
        return sqrt(absA * absA + absB * absB)
    }

    private Float calculateMovementCostBetweenTwoNodes(WeightedPosition node1, WeightedPosition node2) {
        if(node1.x!=node2.x && node1.y!=node2.y)
            return 1.4
        return 1
    }

    private Set<WeightedPosition> getSuccessors(WeightedPosition current) {
        Set<WeightedPosition> ret = new HashSet<>()
        int x = current.x
        int y = current.y

        if(y>0) {
            checkAndAdd(map.get(x, y-1), ret)
            if(x<map.sideLength-1)
                checkAndAdd(map.get(x+1, y-1), ret)
            if(x>0)
                checkAndAdd(map.get(x-1, y-1), ret)
        }

        if(x>0)
            checkAndAdd(map.get(x-1, y), ret)
        if(x<map.sideLength-1)
            checkAndAdd(map.get(x+1, y), ret)

        if(y<map.sideLength-1) {
            checkAndAdd(map.get(x, y+1), ret)
            if(x<map.sideLength-1)
                checkAndAdd(map.get(x+1, y+1), ret)
            if(x>0)
                checkAndAdd(map.get(x-1, y+1), ret)
        }
        return ret
    }

    private void checkAndAdd(WeightedPosition pos, Set<WeightedPosition> theSet) {
        if(pos.value==0)
            theSet.add(pos)
    }
}
