import java.util.*;

/* This class creates a disjoint set forest
 * 
 * @author Patrick Leary
 * @author Gilbert Deglau
 */
public class DisjointSetForest {
    public Node[][] nodeArray;

    /* subclass of nodes used to create disjoint set forest
     * 
     */
    public static class Node {
        Pixel pixel;
        Node parent;
        int rank;
        int size;
        double id;
        /* Constructs node with a given pixel
         * 
         * @param pixel
         */
        public Node(Pixel pixel) {
            this.pixel = pixel;
            this.parent = null;
            this.rank = 0;
            this.size = 1;
            this.id = 0;
        }
    }

    /* Contructs disjoint set forest from a given pixel array
     * 
     * @param pixel[][] pixelArray
     */
    public DisjointSetForest(Pixel[][] pixelArray) {
        nodeArray = new Node[pixelArray.length][pixelArray[0].length];
        for (int i = 0; i < pixelArray.length; i++) {
            for (int j = 0; j < pixelArray[0].length; j++) {
                Node temp = new Node(pixelArray[i][j]);
                nodeArray[i][j] = temp;
            }
        }
    }

    /* Finds representative node of given node
     * 
     * @param node
     * 
     * @return representative node
     */
    public Node find(Node node) {
        ArrayList<Node> nodeList = new ArrayList<>();
        while(node.parent != null) {
            nodeList.add(node);
            node = node.parent;
        }
        for (Node x: nodeList) {
            x.parent = node;
        }
        return node;
    }

    /* combines two segments from disjoint set forest
     * 
     * @param node1, node2
     */
    public void union(Node node1, Node node2) {
        Node rep1 = find(node1);
        Node rep2 = find(node2);
        Edge temp = new Edge(node1.pixel, node2.pixel);
        if (rep1.rank == rep2.rank) {
            rep2.parent = rep1;
            rep1.rank++;
            rep1.size += rep2.size;
            if (rep1.id < temp.getWeight()) {
            rep1.id = temp.getWeight();
        }
        }
        else if (rep1.rank > rep2.rank) {
            rep2.parent = rep1;
            rep1.size += rep2.size;
            if (rep1.id < temp.getWeight()) {
                rep1.id = temp.getWeight();
            }
        }
        else if (rep1.rank < rep2.rank) {
            rep1.parent = rep2;
            rep2.size += rep1.size;
            if (rep2.id < temp.getWeight()) {
                rep2.id = temp.getWeight();
            }
        }
    }

    /* gets node of given pixel
     * 
     * @param pixel
     * 
     * @return node
     */
    public Node getNode(Pixel pixel) {
        return nodeArray[pixel.getRow()][pixel.getCol()];
    }
}
