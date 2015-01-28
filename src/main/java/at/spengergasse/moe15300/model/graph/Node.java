package at.spengergasse.moe15300.model.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Markus Möslinger
 */
public class Node {
    private int id;
    private final List<Node> connectedNodes = new ArrayList<>();

    public Node(int id) {
        this.id = id;
    }

    public Node(Node n) {
        this.id = n.id;

    }

    public void addEdge(Node n) {
        if (!equals(n)) {
            connectedNodes.add(n);
        }
    }

    public void removeEdge(Node n) {
        connectedNodes.remove(n);
    }

    public List<Node> getConnectedNodes() {
        return Collections.unmodifiableList(connectedNodes);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDegree() {
        // not aware of direction!
        return connectedNodes.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        if (id != node.id) return false;
        if (connectedNodes != null ? !connectedNodes.equals(node.connectedNodes) : node.connectedNodes != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (connectedNodes != null ? connectedNodes.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Node " + (id + 1);
    }
}
