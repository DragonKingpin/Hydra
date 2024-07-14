package com.pinecone.framework.util;

import com.pinecone.Pinecone;
import com.pinecone.framework.system.prototype.Pinenut;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Random;

public class Randomium extends Random implements Pinenut {
    private static final String S_ALP_NUM_STRING_DICT = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public Randomium(){
        super();
    }

    public Randomium( long seed ){
        super( seed );
    }

    public int nextInteger ( int from, int to ) {
        if ( from > to ) {
            throw new IllegalArgumentException( "'from' cannot be greater than 'to'!" );
        }
        return this.nextInt(to - from + 1) + from;
    }

    public long nextLong ( long from, long to ) {
        if ( from > to ) {
            throw new IllegalArgumentException( "'from' cannot be greater than 'to'!" );
        }
        return from + (long) ( this.nextLong() * (to - from + 1) / (Long.MAX_VALUE + 1.0) );
    }

    public short nextShort( short from, short to ) {
        if (from > to) {
            throw new IllegalArgumentException( "'from' cannot be greater than 'to'!" );
        }
        return (short) ( this.nextInt(to - from + 1) + from );
    }

    public byte nextByte( byte from, byte to ) {
        if ( from > to ) {
            throw new IllegalArgumentException( "'from' cannot be greater than 'to'!" );
        }
        return (byte) ( this.nextInt(to - from + 1) + from );
    }

    public char nextCharacter( char from, char to ) {
        if ( from > to ) {
            throw new IllegalArgumentException( "'from' char cannot be greater than 'to' char!" );
        }
        return (char) ( this.nextInt(to - from + 1) + from );
    }

    public float nextFloat32( float from, float to ) {
        if ( from > to ) {
            throw new IllegalArgumentException("'from' cannot be greater than 'to'!");
        }
        return from + this.nextFloat() * (to - from);
    }

    public double nextFloat64( double from, double to ) {
        if ( from > to ) {
            throw new IllegalArgumentException("'from' cannot be greater than 'to'!");
        }
        return from + this.nextDouble() * (to - from);
    }

    public BigDecimal nextBigDecimal( BigDecimal from, BigDecimal to, int scale ) {
        if ( from.compareTo(to) > 0 ) {
            throw new IllegalArgumentException("'from' cannot be greater than 'to'!");
        }
        BigDecimal randomBigDecimal = from.add(new BigDecimal(this.nextDouble()).multiply(to.subtract(from)));
        return randomBigDecimal.setScale( scale, RoundingMode.HALF_UP );
    }

    public BigInteger nextBigInteger( BigInteger from, BigInteger to ) {
        if ( from.compareTo(to) > 0 ) {
            throw new IllegalArgumentException( "'from' cannot be greater than 'to'!" );
        }

        BigInteger range = to.subtract( from ).add( BigInteger.ONE ); // Calculate the range
        BigInteger randomNumber;

        do {
            randomNumber = new BigInteger(range.bitLength(), this);
        } while (randomNumber.compareTo(range) >= 0);

        return randomNumber.add( from );
    }

    public String nextString( char from, char to, int scale ) {
        if( from > to ){
            throw new IllegalArgumentException("'from' char can't beyond 'to' char !");
        }
        if( scale > Pinecone.COMMON_ACCURACY_LIMIT ){
            throw new ArithmeticException("Randomium scale is too big limit '" + Pinecone.COMMON_ACCURACY_LIMIT + "' !");
        }
        String randomDict = Randomium.S_ALP_NUM_STRING_DICT ;
        int fromIndex = randomDict.indexOf(from), toIndex = randomDict.indexOf(to);
        StringBuilder sb = new StringBuilder();
        for( int i=0; i < scale; ++i ){
            sb.append(randomDict.charAt( this.nextInt(toIndex - fromIndex + 1) + fromIndex) ) ;
        }
        return sb.toString();
    }

    public String nextString( int scale ){
        return nextString('0','z',scale);
    }

    public String nextString(){
        return nextString(10);
    }

    public String nextMBString( char from, char to, int scale ) {
        if ( from > to ) {
            throw new IllegalArgumentException( "'from' char can't be beyond 'to' char!" );
        }
        if ( scale < 0 ) {
            throw new IllegalArgumentException( "Scale cannot be negative!" );
        }
        StringBuilder sb = new StringBuilder();
        for ( int i = 0; i < scale; ++i ) {
            sb.append((char) (this.nextInt(to - from + 1) + from));
        }
        return sb.toString();
    }

    public String nextMBString( int scale ){
        return nextMBString('0','z',scale);
    }

    public String nextMBString(){
        return nextMBString(10);
    }


    public double nextGaussian( double mean, double stddev ) {
        return mean + stddev * this.nextGaussian();
    }

    public int nextPoisson( double lambda ) {
        double L = Math.exp(-lambda);
        int k = 0;
        double p = 1.0;
        do {
            k++;
            p *= this.nextDouble();
        }
        while (p > L);
        return k - 1;
    }

    public double nextBias( double from, double to, double bias ) {
        double randomValue = this.nextDouble();
        double biasedValue = Math.pow( randomValue, bias );
        return from + (to - from) * biasedValue;
    }


    public static Randomium newInstance() {
        return new Randomium();
    }

    public static Randomium newInstance( long seed ) {
        return new Randomium( seed );
    }
}
