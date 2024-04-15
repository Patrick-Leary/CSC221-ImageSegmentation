import java.lang.Math.*;
import java.awt.Color;
import java.util.HashMap;
import java.util.TreeSet;


/* This program gives the soolution to homework 7
 * 
 * @Author Patrick Leary
 * @Author Gilbert Deglau
 * 
 * Time Spent: 12 hours
 */
public class ImageSegmenter {
    
    /* This method separates a given image into segments
     * based on how similar surrounding pixels are given a certain granularity
     * 
     * @param rgbArray - color array corresponding to a given jpg
     * @param granularity - double that controls the size of the segments that are generated
     * 
     * @return rgbArray - color array of segmented jpg
     */
    public static Color[][] segment(Color[][] rgbArray, double granularity) {
        TreeSet<Edge> edgeSet = new TreeSet<>();
        Pixel[][] pixelArray = new Pixel[rgbArray.length][rgbArray[0].length];
        for (int i = 0; i < rgbArray.length; i++) {
            for (int j = 0; j < rgbArray[0].length; j++) { //loops through rgbArray to create edgeSet and pixelArray
                Pixel temp1 = new Pixel(i, j, rgbArray[i][j]);
                pixelArray[i][j] = temp1;
                if ((j+1) < rgbArray[0].length) {
                    Pixel temp2 = new Pixel(i, j+1, rgbArray[i][j+1]);
                    edgeSet.add(new Edge(temp1, temp2));
                }
                if ((i+1) < rgbArray.length) {
                    Pixel temp3 = new Pixel(i+1, j, rgbArray[i+1][j]);
                    edgeSet.add(new Edge(temp1, temp3));
                }
                if ((i+1 < rgbArray.length) && (j+1 < rgbArray[0].length)) {
                    Pixel temp4 = new Pixel(i+1, j+1, rgbArray[i+1][j+1]);
                    edgeSet.add(new Edge(temp1, temp4));
                }
                if ((i-1 >= 0) && (j+1 < rgbArray[0].length)) {
                    Pixel temp5 = new Pixel(i-1, j+1, rgbArray[i-1][j+1]);
                    edgeSet.add(new Edge(temp1, temp5));
                }
            }
        }
        DisjointSetForest setForest = new DisjointSetForest(pixelArray);
        
        for (Edge x: edgeSet) { // builds disjoint set forest using Kruskal's algorithm
            if (!setForest.find(setForest.getNode(x.getFirstPixel())).equals(setForest.find(setForest.getNode(x.getSecondPixel())))) {
                if (x.getWeight() < Math.min(setForest.find(setForest.getNode(x.getFirstPixel())).id + 
                (granularity / (double) setForest.find(setForest.getNode(x.getFirstPixel())).size),
                setForest.find(setForest.getNode(x.getSecondPixel())).id + 
                (granularity / (double) setForest.find(setForest.getNode(x.getSecondPixel())).size))) {
                    setForest.union(setForest.getNode(x.getFirstPixel()), setForest.getNode(x.getSecondPixel()));
                }
            }
        }

        HashMap<Pixel, Color> colorMap = new HashMap<>();
        ColorPicker colorGenerator = new ColorPicker();
        for (int i = 0; i < setForest.nodeArray.length; i++) {
            for (int j = 0; j < setForest.nodeArray[0].length; j++) { // assigns color to each segment
                Pixel temp = setForest.find(setForest.nodeArray[i][j]).pixel;
                if (!colorMap.containsKey(temp)) {
                    Color randomColor = colorGenerator.nextColor();
                    colorMap.put(temp, randomColor);
                }
            }
        } 


        for (int i = 0; i < pixelArray.length; i++) {
            for (int j = 0; j < pixelArray[0].length; j++) { // creates jpg
                Color temp = colorMap.get(setForest.find(setForest.getNode(pixelArray[i][j])).pixel);
                rgbArray[i][j] = temp;
            }
        }

        return rgbArray; 
    }
}

