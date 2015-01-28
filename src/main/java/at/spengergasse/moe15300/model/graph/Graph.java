package at.spengergasse.moe15300.model.graph;

import at.spengergasse.moe15300.model.matrix.IntegerSquareMatrix;
import at.spengergasse.moe15300.model.matrix.implementation.SquareMatrixArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Component
public class Graph {
    private static final Logger log = LogManager.getLogger(Graph.class);
    public static final int INFINITE = -1;

    private List<Node> nodes = new ArrayList<>();

    private IntegerSquareMatrix adjacencyMatrix;
    private IntegerSquareMatrix wegMatrix;
    private IntegerSquareMatrix distanceMatrix;

    private boolean needToRebuildMatrices = true;

    public Graph() {

    }

    public Graph(Graph g) {
        for (Node n : g.nodes) {
            nodes.add(new Node(n));
        }

        adjacencyMatrix = (IntegerSquareMatrix) g.adjacencyMatrix.clone();
        wegMatrix = new SquareMatrixArrayList();
        distanceMatrix = new SquareMatrixArrayList();
    }

    public void init() {
        adjacencyMatrix = new SquareMatrixArrayList();
        wegMatrix = new SquareMatrixArrayList();
        distanceMatrix = new SquareMatrixArrayList();
    }

    public void addEdge(Node from, Node to) {
        changeEdgeInMatrix(from, to, true);

        from.addEdge(to);
        to.addEdge(from);
    }

    public void removeEdge(Node from, Node to) {
        changeEdgeInMatrix(from, to, false);

        from.removeEdge(to);
        to.removeEdge(from);
    }

    private void changeEdgeInMatrix(Node from, Node to, boolean add) {
        adjacencyMatrix.set(from.getId(), to.getId(), add ? 1 : 0);
        adjacencyMatrix.set(to.getId(), from.getId(), add ? 1 : 0);

        needToRebuildMatrices = true;
    }

    public IntegerSquareMatrix getWegMatrix() {
        renewMatricesIfNeeded();

        return (IntegerSquareMatrix) wegMatrix.clone();
    }

    public IntegerSquareMatrix getDistanzMatrix() {
        renewMatricesIfNeeded();

        return (IntegerSquareMatrix) distanceMatrix.clone();
    }

    public int getVertices() {
        return adjacencyMatrix.side();
    }

    public void addNode() {
        nodes.add(new Node(adjacencyMatrix.side()));

        adjacencyMatrix.expand(1);
        wegMatrix.expand(1);
        distanceMatrix.expand(1);
        needToRebuildMatrices = true;
    }

    public void removeLastNode() {
        removeNode(nodes.get(nodes.size() - 1));
    }

    public void removeNode(Node node) {
        nodes.remove(node);
        node.getConnectedNodes().forEach(connectedNode -> connectedNode.removeEdge(node));

        // Wenns der letzte Knoten war, dann die Matrizzen verkleinern, ansonsten "ausnullen"
        if (node.getId() == adjacencyMatrix.side() - 1) {
            adjacencyMatrix.shrink(1);
        } else {
            adjacencyMatrix.setValueInRowAndColumn(node.getId(), 0);
        }

        needToRebuildMatrices = true;
    }

    private void renewMatricesIfNeeded() {
        if (!needToRebuildMatrices) {
            return;
        }

        IntegerSquareMatrix multiplyMatrix = (IntegerSquareMatrix) adjacencyMatrix.clone();
        IntegerSquareMatrix multiplyMatrixFromBefore = null;
        IntegerSquareMatrix multiplyMatrixFromBeforeBefore = null;

        // Distanzen werden als unendlich angenommen, außer von einem Knoten zu sich selbst
        distanceMatrix.setAll(INFINITE);
        distanceMatrix.setWholeDiagonal(0);

        // Wege werden als fehlend angenommen, außer von einem Knoten zu sich selbst
        wegMatrix.setAll(0);
        wegMatrix.setWholeDiagonal(1);

        final int nodes = adjacencyMatrix.side();

        // Die Distanz kann nicht höher sein als die Anzahl der Knoten.
        // Außerdem kann die Suche abgebrochen werden,
        // wenn das Ergebnis der multiplizierten Adjazenzmatrix gleich des Ergebnisses des vorletzten Durchganges ist
        for (int distance = 1; distance < nodes && !multiplyMatrix.equals(multiplyMatrixFromBeforeBefore); distance++) {
            log.trace("Trying with distance set to " + distance);
            for (int row = 0; row < nodes; row++) {
                for (int column = 0; column < nodes; column++) {
                    // erstes mal eine Distanz gefunden?
                    if (distanceMatrix.get(row, column) == INFINITE && multiplyMatrix.get(row, column) != 0) {
                        distanceMatrix.set(row, column, distance);
                        wegMatrix.set(row, column, 1);
                    }
                }
            }

            // "nachrücken" der matrizen
            multiplyMatrixFromBeforeBefore = multiplyMatrixFromBefore;
            multiplyMatrixFromBefore = (IntegerSquareMatrix) multiplyMatrix.clone();
            multiplyMatrix.multiply(adjacencyMatrix);
        }

        needToRebuildMatrices = false;
    }

    /**
     * Aktuell gibt es keine Möglichkeit, gerichtete Graphen über das UI zu erzeugen
     *
     * @return
     */
    public boolean isDirected() {
        return false;
    }

    public List<List<Node>> getComponents() {
        List<List<Node>> components = new ArrayList<>();

        for (Node n : nodes) {
            List<Node> connectionComponents = getConnectionComponents(n);

            boolean componentAlreadyThere = false;
            for (List<Node> component : components) {
                if (component.containsAll(connectionComponents) && connectionComponents.containsAll(component)) {
                    componentAlreadyThere = true;
                    break;
                }
            }

            if (!componentAlreadyThere) {
                components.add(connectionComponents);
            }
        }

        return components;
    }

    public List<Node> getNodes() {
        return Collections.unmodifiableList(nodes);
    }

    /**
     * Zusammenhangskomponenten
     * @param n
     * @return
     */
    public List<Node> getConnectionComponents(Node n) {
        List<Node> reachableNodes = new ArrayList<>();

        for (int i = 0; i < wegMatrix.side(); i++) {
            if (wegMatrix.get(n.getId(), i) == 1) {
                reachableNodes.add(nodes.get(i));
            }
        }

        return reachableNodes;
    }

    public boolean isConnected() {
        // oder: haben alle knoten zu mindestens einem anderen knoten einen weg?
        return getComponents().size() == 1;
    }

    public int getEccentricity(Node node) {
        if (node == null) {
            throw new IllegalArgumentException("Node must not be null!");
        }
        renewMatricesIfNeeded();

        int highestDistance = 0;
        for (int i = 0; i < distanceMatrix.side(); i++) {
            int distance = distanceMatrix.get(node.getId(), i);
            if (distance == INFINITE) {
                return INFINITE;
            } else if (distance > highestDistance) {
                highestDistance = distance;
            }
        }

        return highestDistance;
    }

    public int getDiameter() {
        int highestEccentricity = 0;
        for (Node n : nodes) {
            int eccentricity = getEccentricity(n);
            if (eccentricity == INFINITE) {
                return INFINITE;
            } else if (eccentricity > highestEccentricity) {
                highestEccentricity = eccentricity;
            }
        }

        return highestEccentricity;
    }

    public int getRadius() {
        int lowestEccentricity = Integer.MAX_VALUE;
        for (Node n : nodes) {
            int eccentricity = getEccentricity(n);
            if (eccentricity == INFINITE) {
                return INFINITE;
            } else if (eccentricity < lowestEccentricity) {
                lowestEccentricity = eccentricity;
            }
        }

        return lowestEccentricity;
    }

    public List<Node> getCenters() {
        List<Node> centers = new ArrayList<>();
        int radius = getRadius();

        if (radius != INFINITE) {
            for (Node n : nodes) {
                if (getEccentricity(n) == radius) {
                    centers.add(n);
                }
            }
        }

        return centers;
    }

    public List<Node> getArticulations() {
        List<Node> articulations = new ArrayList<>();

        for (Node node : nodes) {
            Graph newGraph = new Graph(this);

            int componentsBeforeDeletion = newGraph.getComponents().size();

            newGraph.removeNode(node);

            int componentsAfterDeletion = newGraph.getComponents().size();

            if (componentsAfterDeletion > componentsBeforeDeletion) {
                articulations.add(node);
            }
        }

        return articulations;
    }

    public List<String> getBridges() {
        List<String> bridges = new ArrayList<>();

        // this way, it won't work for directed graphs
        for (int row = 0; row < adjacencyMatrix.side() - 1; row++) {
            for (int column = row + 1; column < adjacencyMatrix.side(); column++) {
                if (adjacencyMatrix.get(row, column) == 1) {
                    int componentsBeforeDeletion = getComponents().size();
                    adjacencyMatrix.set(row, column, 0);
                    needToRebuildMatrices = true;
                    int componentsAfterDeletion = getComponents().size();

                    if (componentsAfterDeletion > componentsBeforeDeletion) {
                        bridges.add("{" + row + ";" + column + "}");
                    }

                    adjacencyMatrix.set(row, column, 1);
                    needToRebuildMatrices = true;
                }
            }
        }

        return bridges;
    }
}
