public class Pathfinder extends Thread {
    boolean stuck = true;

    public Node findShortestPath(Map map, Node lastPathNode) {
        while (stuck) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        if (lastPathNode.type == Node.NodeType.END)
            return lastPathNode;

        discoverAdjacentNodes(map, lastPathNode);

        float minCost = getMinCost(map);
        System.out.println(lastPathNode.type);
        int next = isLowestCostAdjacent(minCost, map, lastPathNode);
        if (next == 0) {
            return findShortestPath(map, lastPathNode.lastPathNode);
        }

        return findShortestPath(map, getNextPathNode(next, lastPathNode, map));
    }

    private void discoverAdjacentNodes(Map map, Node lastPathNode) {
        int[] mods = new int[] { map.cols, -map.cols, 1, -1 };

        for (int i = 0; i < 4; i ++) {
            if (lastPathNode.index + mods[i] <= map.cols * map.rows - 1 && lastPathNode.index + mods[i] >= 0) {
                Node aNode = map.getNode(lastPathNode.index + mods[i]);
                if (aNode.type == Node.NodeType.UNKNOWN || aNode.type == Node.NodeType.DISCOVERED) {
                    aNode.type = Node.NodeType.DISCOVERED;
                    aNode.e_value = lastPathNode.e_value + aNode.i_value;
                }
            }
        }
    }

    private float getMinCost(Map map) {
        float minE_Value = Float.MAX_VALUE;
        for (Node node : map.map) {
            if (node.type == Node.NodeType.DISCOVERED || node.type == Node.NodeType.PATH)
                minE_Value = Math.min(minE_Value, node.e_value);
        }
        return minE_Value;
    }

    private int isLowestCostAdjacent(float lowestCost, Map map, Node lastPathNode) {
        int[] mods = new int[] { map.cols, -map.cols, 1, -1 };

        for (int i = 0; i < 4; i ++) {
            if (lastPathNode.index + mods[i] <= map.cols * map.rows - 1 && lastPathNode.index + mods[i] >= 0) {
                Node aNode = map.getNode(lastPathNode.index + mods[i]);
                if (aNode.e_value == lowestCost) {
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
            returnNode.i_value = Float.MAX_VALUE;
        }
        return returnNode;
    }
}
