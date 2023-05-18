package Pinecone.Framework.Util.Convert;

public class SuperConvert {
    public static double[] object2Float64Array(Object[] objects){
        double[] arrayBuf = new double[objects.length];
        for(int i=0;i<arrayBuf.length;i++){
            arrayBuf[i] = (double)objects[i];
        }
        return arrayBuf;
    }

    public static float[] object2Float32Array(Object[] objects){
        float[] arrayBuf = new float[objects.length];
        for(int i=0;i<arrayBuf.length;i++){
            arrayBuf[i] = (float)objects[i];
        }
        return arrayBuf;
    }

    public static int[] object2Int32Array(Object[] objects){
        int[] arrayBuf = new int[objects.length];
        for(int i=0;i<arrayBuf.length;i++){
            arrayBuf[i] = (int)objects[i];
        }
        return arrayBuf;
    }

    public static long[] object2Int64Array(Object[] objects){
        long[] arrayBuf = new long[objects.length];
        for(int i=0;i<arrayBuf.length;i++){
            arrayBuf[i] = (long)objects[i];
        }
        return arrayBuf;
    }

    public static String[] object2StringArray(Object[] objects){
        String[] arrayBuf = new String[objects.length];
        for(int i=0;i<arrayBuf.length;i++){
            arrayBuf[i] = (String) objects[i];
        }
        return arrayBuf;
    }

}
