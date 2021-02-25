import java.util.ArrayList;

public class Map {
    public ArrayList<Node> map;
    public final int rows;
    public final int cols;

    public Map(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        map = new ArrayList<>();
    }

    public void generateMap(int endIndex) {
        for (int i = 0; i < cols * rows; i++) {
            map.add(new Node(Node.NodeType.UNKNOWN, i, (float)Math.random()));
        }
        map.set(endIndex, new Node(Node.NodeType.END, endIndex, 0f));
        map.set(0, new Node(Node.NodeType.START, 0, 0f));

    }

    public synchronized Node getNode(int index) {
        return map.get(index);
    }
}

class Node {
    enum NodeType {
        START,
        END,
        UNKNOWN,
        DISCOVERED,
        PATH
    }

    public NodeType type;
    public int index;
    public float i_value;
    public float e_value;
    public Node lastPathNode;

    public Node(NodeType type, int index, float i_value) {
        this.type = type;
        this.index = index;
        this.i_value = i_value;
        e_value = 0;
        lastPathNode = null;
    }
}