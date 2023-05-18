package Pinecone.Framework.Util.Random;

import Pinecone.Pinecone;

import java.util.Random;

public class SeniorRandom extends Random {
    private static final String S_ALP_NUM_STRING_DICT = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public SeniorRandom(){
        super();
    }

    public SeniorRandom( long seed ){
        super( seed );
    }


    public String nextString( char from, char to, int scale ) {
        if(from > to){
            throw new IllegalArgumentException("'from' char can't beyond 'to' char !");
        }
        if(scale > Pinecone.COMMON_ACCURACY_LIMIT){
            throw new ArithmeticException("SeniorRandom scale is too big limit '" + Pinecone.COMMON_ACCURACY_LIMIT + "' !");
        }
        String randomDict = SeniorRandom.S_ALP_NUM_STRING_DICT ;
        int fromIndex = randomDict.indexOf(from), toIndex = randomDict.indexOf(to);
        StringBuilder sb = new StringBuilder();
        for( int i=0; i < scale; ++i ){
            sb.append(randomDict.charAt( this.nextInt(toIndex - fromIndex + 1) + fromIndex) ) ;
        }
        return sb.toString();
    }

    public String nextString(int scale){
        return nextString('0','z',scale);
    }

    public String nextString(){
        return nextString(10);
    }
}
