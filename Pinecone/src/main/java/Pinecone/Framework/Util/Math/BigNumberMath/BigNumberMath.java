package Pinecone.Framework.Util.Math.BigNumberMath;
import Pinecone.Pinecone;

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

    public abstract BigDecimal sin(BigDecimal var1);

    public abstract BigDecimal cos(BigDecimal var1);

    public abstract BigDecimal tan(BigDecimal var1);

    public abstract BigDecimal asin(BigDecimal var1);

    public abstract BigDecimal acos(BigDecimal var1);

    public abstract BigDecimal atan(BigDecimal var1);

    public abstract BigDecimal pow(BigDecimal var1, BigDecimal var2);

    public abstract BigDecimal pow(double var1, double var2);

    public abstract BigDecimal sqrt(BigDecimal var1);

    public abstract BigDecimal cbrt(BigDecimal var1);

    public abstract BigDecimal root(BigDecimal var1, BigDecimal var2);

    public abstract BigDecimal log10(BigDecimal var1);

    public abstract BigDecimal log(BigDecimal var1, BigDecimal var2);

    public abstract BigDecimal ln(BigDecimal var1);

    public abstract BigDecimal exp(BigDecimal var1);

    public abstract BigDecimal sinh(BigDecimal var1);

    public abstract BigDecimal cosh(BigDecimal var1);

    public abstract BigDecimal tanh(BigDecimal var1);

    public abstract BigDecimal asinh(BigDecimal var1);

    public abstract BigDecimal acosh(BigDecimal var1);

    public abstract BigDecimal atanh(BigDecimal var1);

    public abstract BigDecimal deg(BigDecimal var1);

    public abstract BigDecimal rad(BigDecimal var1);
}