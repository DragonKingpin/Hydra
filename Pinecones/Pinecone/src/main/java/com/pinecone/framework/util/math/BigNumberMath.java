package com.pinecone.framework.util.math;
import com.pinecone.Pinecone;

import java.math.BigDecimal;

public abstract class BigNumberMath {
    private PrecisionHolder precision;
    protected int mp = 0;
    private int lastPrecision = 0;
    private int stp = 0;

    protected BigNumberMath(PrecisionHolder precision) {
        this.precision = precision;
    }

    protected int precision() {
        if (this.mp != 0) {
            return this.lastPrecision + this.mp;
        } else {
            this.lastPrecision = this.precision.getPrecision();
            this.stp = this.lastPrecision / 5;
            return this.lastPrecision;
        }
    }

    protected void applyForCachePrecision() {
        this.mp += this.stp;
    }

    protected void clearCachePrecision() {
        this.mp -= this.stp;
        if (this.mp < 0) {
            this.mp = 0;
        }

    }

    public static BigNumberMath getDefaultBigNumberMath(PrecisionHolder precision) {
        return new BigNumberMathAchieve(precision);
    }

    public static BigNumberMath getDefaultBigNumberMath(int precision) {
        PrecisionHolder precisionHolder = new PrecisionHolder(){
            @Override
            public int getPrecision() {
                return precision;
            }
        };
        return new BigNumberMathAchieve(precisionHolder);
    }

    public static BigNumberMath getDefaultBigNumberMath() {
        PrecisionHolder precisionHolder = new PrecisionHolder(){
            @Override
            public int getPrecision() {
                return Pinecone.FLOAT_ACCURACY;
            }
        };
        return new BigNumberMathAchieve(precisionHolder);
    }

    public abstract BigDecimal sin(BigDecimal decimal);

    public abstract BigDecimal cos(BigDecimal decimal);

    public abstract BigDecimal tan(BigDecimal decimal);

    public abstract BigDecimal asin(BigDecimal decimal);

    public abstract BigDecimal acos(BigDecimal decimal);

    public abstract BigDecimal atan(BigDecimal decimal);

    public abstract BigDecimal pow(BigDecimal decimal, BigDecimal decimal2);

    public abstract BigDecimal pow(double var1, double var2);

    public abstract BigDecimal sqrt(BigDecimal decimal);

    public abstract BigDecimal cbrt(BigDecimal decimal);

    public abstract BigDecimal root(BigDecimal decimal, BigDecimal decimal2);

    public abstract BigDecimal log10(BigDecimal decimal);

    public abstract BigDecimal log(BigDecimal decimal, BigDecimal decimal2);

    public abstract BigDecimal ln(BigDecimal decimal);

    public abstract BigDecimal exp(BigDecimal decimal);

    public abstract BigDecimal sinh(BigDecimal decimal);

    public abstract BigDecimal cosh(BigDecimal decimal);

    public abstract BigDecimal tanh(BigDecimal decimal);

    public abstract BigDecimal asinh(BigDecimal decimal);

    public abstract BigDecimal acosh(BigDecimal decimal);

    public abstract BigDecimal atanh(BigDecimal decimal);

    public abstract BigDecimal deg(BigDecimal decimal);

    public abstract BigDecimal rad(BigDecimal decimal);
}