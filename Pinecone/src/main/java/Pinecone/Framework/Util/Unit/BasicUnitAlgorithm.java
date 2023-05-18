package Pinecone.Framework.Util.Unit;

import java.util.Vector;

public class BasicUnitAlgorithm {
    static public int accumulateInt(int from, int to, Vector<Integer> vector){
        int sum = 0;
        for(int i=from; i<to; i++){
            sum += vector.elementAt(i);
        }
        return sum;
    }

    static public int accumulateInt(Vector<Integer> vector) {
        return accumulateInt(0, vector.size(), vector);
    }

    static public double accumulateDouble(int from, int to, Vector<Double> vector){
        double sum = 0;
        for(int i=from; i<to; i++){
            sum += vector.elementAt(i);
        }
        return sum;
    }

    static public double accumulateDouble(Vector<Double> vector) {
        return accumulateDouble(0, vector.size(), vector);
    }
}
