public class Pathfinder2 extends Thread {
    boolean stuck = true;

    public Node findShortestPath(Map map, Node lastPathNode) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        if (lastPathNode.type == Node.NodeType.END)
            return lastPathNode;

        discoverAdjacentNodes(map, lastPathNode);
        while (stuck) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        float minCost = getMinCost(map, lastPathNode.index);
        int next = isLowestCostAdjacent(minCost, map, lastPathNode);
        if (next == 0)
            return findShortestPath(map, lastPathNode.lastPathNode);

        return findShortestPath(map, getNextPathNode(next, lastPathNode, map));
    }

    private void discoverAdjacentNodes(Map map, Node lastPathNode) {
        int[] mods = new int[] { map.cols, -1, -map.cols, 1 };
        for (int i = 0; i < 4; i ++) {
            if (checkOverflow(lastPathNode.index, mods[i], map)) {
                Node aNode = map.getNode(lastPathNode.index + mods[i]);
                if (aNode.type == Node.NodeType.UNKNOWN || aNode.type == Node.NodeType.DISCOVERED) {
                    aNode.type = Node.NodeType.DISCOVERED;
                    aNode.e_value = lastPathNode.e_value + aNode.i_value;
                }
            }
        }
    }

    private float getMinCost(Map map, int lastPathIndex) {
        int[] mods = new int[] { map.cols, -1, -map.cols, 1 };
        float minE_Value = Float.MAX_VALUE;

        for (int i = 0; i < 4; i ++) {
            if (checkOverflow(lastPathIndex, mods[i], map)) {
                Node aNode = map.getNode(lastPathIndex + mods[i]);
                if (aNode.type == Node.NodeType.DISCOVERED || aNode.type == Node.NodeType.END)
                    minE_Value = Math.min(minE_Value, aNode.i_value);
            }
        }
        return minE_Value;
    }

    private int isLowestCostAdjacent(float lowestCost, Map map, Node lastPathNode) {
        int[] mods = new int[] { map.cols, -map.cols, 1, -1 };

        for (int i = 0; i < 4; i ++) {
            if (checkOverflow(lastPathNode.index, mods[i], map)) {
                Node aNode = map.getNode(lastPathNode.index + mods[i]);
                if (aNode.i_value == lowestCost) {
                    return mods[i];
                }
            }
        }
        return 0;
    }

    private Node getNextPathNode(int nextCell, Node lastPathNode, Map map) {
        Node returnNode = map.getNode(nextCell + lastPathNode.index);
        if (returnNode.type == Node.NodeType.DISCOVERED || returnNode.type == Node.NodeType.PATH) {
            returnNode.type = Node.NodeType.PATH;
            returnNode.lastPathNode = lastPathNode;
        }
        return returnNode;
    }

    private boolean checkOverflow(int checkIndex, int target, final Map map) {
        if (checkIndex + target >= map.cols * map.rows || checkIndex + target < 0) // Check movement to row
            return false;

        switch (target) { // Check movement to column
            case 1:
                if ((checkIndex + target) % map.cols == 0)
                    return false;
                break;
            case -1:
                if (checkIndex % map.cols == 0)
                    return false;
                break;
        }
        return true;
    }
}
