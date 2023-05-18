package Pinecone.Framework.Util.Math.BigNumberMath;

import java.math.BigDecimal;
import java.math.BigInteger;

final class BigNumberMathAchieve extends BigNumberMath {
    private static BigDecimal E = new BigDecimal("2.7182818284590452353602874713526624977572470936999595749669676277240766");
    private static BigDecimal PI = new BigDecimal("3.1415926535897932384626433832795028841971693993751058209749445923078164");
    private static BigDecimal PI2;
    private static BigDecimal bim;
    private static BigDecimal B180;
    private static BigDecimal N1;
    private static BigDecimal B2;
    static BigDecimal[] PREC_CACHE;

    static {
        PI2 = PI.multiply(BigDecimal.valueOf(2L));
        bim = BigDecimal.valueOf(2147483647L);
        B180 = BigDecimal.valueOf(180L);
        N1 = BigDecimal.valueOf(-1L);
        B2 = BigDecimal.valueOf(2L);
        PREC_CACHE = new BigDecimal[101];
        BigDecimal current = BigDecimal.ONE;

        for(int i = 0; i < PREC_CACHE.length; ++i) {
            PREC_CACHE[i] = current;
            current = current.divide(BigDecimal.TEN);
        }

    }

    BigNumberMathAchieve(PrecisionHolder precision) {
        super(precision);
    }

    public BigDecimal sin(BigDecimal x) {
        int precision = super.precision();
        x = x.remainder(PI2);
        BigDecimal P = this.ofPrecision(precision);
        boolean neg = x.compareTo(BigDecimal.ZERO) == -1;
        if (neg) {
            x = x.abs();
        }

        BigDecimal result = BigDecimal.ONE;
        BigDecimal cons = x.subtract(PI.divide(BigDecimal.valueOf(2L), precision + 2, 6)).pow(2);
        BigDecimal curf = BigDecimal.valueOf(2L);
        BigDecimal curr = cons;
        int i = 3;
        boolean n = true;

        while(true) {
            BigDecimal temp;
            if (n) {
                temp = result.subtract(curr.divide(curf, precision + 2, 6));
            } else {
                temp = result.add(curr.divide(curf, precision + 2, 6));
            }

            if (result.subtract(temp).abs().compareTo(P) != 1) {
                result = temp;
                if (neg) {
                    result = temp.negate();
                }

                return result.setScale(precision, 6);
            }

            result = temp;
            curr = curr.multiply(cons).setScale(precision + 2, 6);
            n = !n;
            curf = curf.multiply(BigDecimal.valueOf((long)(i++ * i++)));
        }
    }

    public BigDecimal cos(BigDecimal x) {
        int precision = super.precision();
        x = x.remainder(PI2);
        BigDecimal P = this.ofPrecision(precision);
        x = x.abs();
        BigDecimal result = BigDecimal.ONE;
        BigDecimal cons = x.pow(2);
        BigDecimal curf = BigDecimal.valueOf(2L);
        BigDecimal curr = cons;
        int i = 3;
        boolean n = true;

        while(true) {
            BigDecimal temp;
            if (n) {
                temp = result.subtract(curr.divide(curf, precision + 2, 6));
            } else {
                temp = result.add(curr.divide(curf, precision + 2, 6));
            }

            if (result.subtract(temp).abs().compareTo(P) != 1) {
                return temp.setScale(precision, 6);
            }

            result = temp;
            curr = curr.multiply(cons).setScale(precision + 2, 6);
            n = !n;
            curf = curf.multiply(BigDecimal.valueOf((long)(i++ * i++)));
        }
    }

    public BigDecimal tan(BigDecimal x) {
        int precision = super.precision();
        super.applyForCachePrecision();
        BigDecimal result = this.sin(x).divide(this.cos(x), precision, 6);
        super.clearCachePrecision();
        return result.setScale(precision);
    }

    public BigDecimal asin(BigDecimal x) {
        if (x.abs().compareTo(BigDecimal.ONE) == 1) {
            try {
                throw new Exception("Illegal input of asin(x)");
            } catch (Exception var6) {
                var6.printStackTrace();
            }
        }

        int precision = super.precision();
        super.applyForCachePrecision();
        BigDecimal P = this.ofPrecision(precision);
        BigDecimal result = BigDecimal.valueOf(Math.asin(x.doubleValue()));

        while(true) {
            BigDecimal temp = result.subtract(this.sin(result).subtract(x).divide(this.cos(result), precision + 3, 6));
            if (result.subtract(temp).abs().compareTo(P) != 1) {
                super.clearCachePrecision();
                return temp.setScale(precision, 6);
            }

            result = temp;
        }
    }

    public BigDecimal acos(BigDecimal x) {
        if (x.abs().compareTo(BigDecimal.ONE) == 1) {
            try {
                throw new Exception("Illegal input of acos(x)");
            } catch (Exception var6) {
                var6.printStackTrace();
            }
        }

        int precision = super.precision();
        super.applyForCachePrecision();
        BigDecimal P = this.ofPrecision(precision);
        BigDecimal result = BigDecimal.valueOf(Math.acos(x.doubleValue()));

        while(true) {
            BigDecimal temp = result.subtract(this.cos(result).subtract(x).divide(this.sin(result).negate(), precision + 3, 6));
            if (result.subtract(temp).abs().compareTo(P) != 1) {
                super.clearCachePrecision();
                return temp.setScale(precision, 6);
            }

            result = temp;
        }
    }

    public BigDecimal atan(BigDecimal x) {
        int precision = super.precision();
        super.applyForCachePrecision();
        BigDecimal P = this.ofPrecision(precision);
        BigDecimal result = BigDecimal.valueOf(Math.atan(x.doubleValue()));

        while(true) {
            BigDecimal temp = result.subtract(this.tan(result).subtract(x).multiply(this.cos(result).pow(2)));
            if (result.subtract(temp).abs().compareTo(P) != 1) {
                super.clearCachePrecision();
                return temp.setScale(precision, 6);
            }

            result = temp.setScale(precision + 3, 6);
        }
    }

    public BigDecimal pow(BigDecimal a, BigDecimal b) {
        int precision = super.precision();
        super.applyForCachePrecision();
        BigDecimal P = this.ofPrecision(precision);
        if (b.abs().compareTo(bim) == 1) {
            throw new IllegalArgumentException("计算幂的过程中指数的绝对值太大！");
        } else if (b.compareTo(BigDecimal.ZERO) == 0) {
            if (a.compareTo(BigDecimal.ZERO) == 0) {
                throw new IllegalArgumentException("计算幂的过程中遇到0的0次方");
            } else {
                return BigDecimal.ONE;
            }
        } else if (b.compareTo(BigDecimal.ONE) == 0) {
            return a;
        } else if (b.compareTo(N1) == 0) {
            return BigDecimal.ONE.divide(a, precision, 6);
        } else {
            boolean below = b.signum() == -1;
            b = b.abs();
            if (b.stripTrailingZeros().precision() <= 0) {
                return a.pow(b.intValue());
            } else {
                BigDecimal result = a.pow(b.intValue());
                BigDecimal constant = BigDecimal.ONE.add(b.multiply(this.ln(a)));
                if (result.equals(BigDecimal.ZERO)) {
                    result = BigDecimal.ONE;
                }

                while(true) {
                    BigDecimal temp = result.multiply(constant.subtract(this.ln(result)));
                    if (temp.subtract(result).compareTo(P) != 1) {
                        if (below) {
                            return BigDecimal.ONE.divide(temp, precision, 6);
                        } else {
                            super.clearCachePrecision();
                            return temp.setScale(precision, 6);
                        }
                    }

                    result = temp;
                }
            }
        }
    }

    public BigDecimal pow(double a, double b) {
        return this.pow(new BigDecimal(a),new BigDecimal(b));
    }

    public BigDecimal sqrt(BigDecimal x) {
        int precision = super.precision();
        BigDecimal n = BigDecimal.ONE;
        BigDecimal l = BigDecimal.ZERO;
        BigDecimal P = this.ofPrecision(precision);
        BigDecimal B = new BigDecimal(2);

        while(true) {
            n = n.subtract(n.pow(2).subtract(x).divide(B.multiply(n), precision + 4, 6));
            if (n.subtract(l).abs().compareTo(P) != 1) {
                return n.setScale(precision, 6);
            }

            l = n;
        }
    }

    public BigDecimal cbrt(BigDecimal x) {
        int precision = super.precision();
        BigDecimal n = BigDecimal.ONE;
        BigDecimal l = BigDecimal.ZERO;
        BigDecimal P = this.ofPrecision(precision);
        BigDecimal B = new BigDecimal(3);

        while(true) {
            n = n.subtract(n.pow(3).subtract(x).divide(B.multiply(n.pow(2)), precision + 4, 6));
            if (n.subtract(l).abs().compareTo(P) != 1) {
                return n.setScale(precision, 6);
            }

            l = n;
        }
    }

    public BigDecimal root(BigDecimal a, BigDecimal b) {
        int precision = super.precision();
        super.applyForCachePrecision();
        BigDecimal result = this.pow(a, BigDecimal.ONE.divide(b, precision, 6));
        super.clearCachePrecision();
        return result.setScale(precision, 6);
    }

    public BigDecimal log10(BigDecimal x) {
        return this.log(x, BigDecimal.TEN);
    }

    public BigDecimal log(BigDecimal a, BigDecimal b) {
        int precision = super.precision();
        super.applyForCachePrecision();
        BigDecimal result = this.ln(a).divide(this.ln(b), precision, 6);
        super.clearCachePrecision();
        return result.setScale(precision, 6);
    }

    public BigDecimal ln(BigDecimal x) {
        if (x.signum() != 1) {
            throw new IllegalArgumentException("Invalid input of ln(x)");
        } else {
            int precision = super.precision();
            super.applyForCachePrecision();
            BigDecimal sc = this.ofPrecision(precision);
            int btl = x.toBigInteger().bitLength();
            BigDecimal result = BigDecimal.valueOf((double)btl - Math.ceil((double)(3 * (btl - 3) / 10 + 1)));

            while(true) {
                BigDecimal bpk = this.exp(result);
                BigDecimal tmp = result.subtract(bpk.subtract(x).divide(bpk, precision + 4, 6));
                if (tmp.subtract(result).abs().compareTo(sc) != 1) {
                    super.clearCachePrecision();
                    return tmp.setScale(precision, 6);
                }

                result = tmp;
            }
        }
    }

    public BigDecimal exp(BigDecimal x) {
        int precision = super.precision();
        super.applyForCachePrecision();
        BigDecimal P = this.ofPrecision(precision);
        if (x.abs().compareTo(bim) == 1) {
            throw new IllegalArgumentException("计算幂的过程中指数的绝对值太大！");
        } else {
            boolean ng = x.signum() == -1;
            x = x.abs();
            BigDecimal eix = E.setScale(precision + 3, 4).pow(x.setScale(0, 0).intValue()).setScale(precision + 3, 4);
            BigDecimal p0 = x.setScale(0, 0);
            BigDecimal cons = x.subtract(p0);
            BigDecimal curr = BigDecimal.ONE;
            BigDecimal curr2 = cons;
            BigDecimal result = eix;
            int var11 = 2;

            while(true) {
                BigDecimal temp = result.add(eix.multiply(curr2).divide(curr, precision + 5, 6));
                if (temp.subtract(result).abs().compareTo(P) != 1) {
                    super.clearCachePrecision();
                    return ng ? BigDecimal.ONE.divide(temp, precision, 6) : temp.setScale(precision, 6);
                }

                curr2 = curr2.multiply(cons);
                curr = curr.multiply(BigDecimal.valueOf((long)(var11++)));
                result = temp;
            }
        }
    }

    public BigDecimal sinh(BigDecimal x) {
        int precision = super.precision();
        super.applyForCachePrecision();
        BigDecimal result = this.exp(x).subtract(this.exp(x.negate())).divide(B2, precision, 6);
        super.clearCachePrecision();
        return result.setScale(precision, 6);
    }

    public BigDecimal cosh(BigDecimal x) {
        int precision = super.precision();
        super.applyForCachePrecision();
        BigDecimal result = this.exp(x).add(this.exp(x.negate())).divide(B2, precision, 6);
        super.clearCachePrecision();
        return result.setScale(precision, 6);
    }

    public BigDecimal tanh(BigDecimal x) {
        int precision = super.precision();
        super.applyForCachePrecision();
        BigDecimal e2 = this.exp(x);
        BigDecimal eN2 = BigDecimal.ONE.divide(e2, precision + 3, 6);
        BigDecimal result = e2.subtract(eN2).divide(e2.add(eN2), precision + 3, 6);
        super.clearCachePrecision();
        return result.setScale(precision, 6);
    }

    public BigDecimal asinh(BigDecimal x) {
        int precision = super.precision();
        super.applyForCachePrecision();
        BigDecimal P = this.ofPrecision(precision);
        boolean ng = x.signum() == -1;
        if (x.signum() == 0) {
            return BigDecimal.ZERO;
        } else {
            x = x.abs();
            BigDecimal result = this.ln(x);

            while(true) {
                BigDecimal temp = result.subtract(this.sinh(result).subtract(x).divide(this.cosh(result), precision + 3, 6));
                if (result.subtract(temp).abs().compareTo(P) != 1) {
                    result = temp;
                    if (ng) {
                        result = temp.negate();
                    }

                    super.clearCachePrecision();
                    return result.setScale(precision, 6);
                }

                result = temp;
            }
        }
    }

    public BigDecimal acosh(BigDecimal x) {
        if (x.compareTo(BigDecimal.ONE) == -1) {
            try {
                throw new Exception("Illegal input of acosh(x)");
            } catch (Exception var6) {
                var6.printStackTrace();
            }
        }

        if (x.compareTo(BigDecimal.ONE) == 0) {
            return BigDecimal.ZERO;
        } else {
            int precision = super.precision();
            super.applyForCachePrecision();
            BigDecimal P = this.ofPrecision(precision);
            BigDecimal result = this.ln(x);

            while(true) {
                BigDecimal temp = result.subtract(this.cosh(result).subtract(x).divide(this.sinh(result), precision + 3, 6));
                if (result.subtract(temp).abs().compareTo(P) != 1) {
                    super.clearCachePrecision();
                    return temp.setScale(precision, 6);
                }

                result = temp;
            }
        }
    }

    public BigDecimal atanh(BigDecimal x) {
        if (x.abs().compareTo(BigDecimal.ONE) != -1) {
            try {
                throw new Exception("Illegal input of atanh(x)");
            } catch (Exception var6) {
                var6.printStackTrace();
            }
        }

        int precision = super.precision();
        super.applyForCachePrecision();
        BigDecimal P = this.ofPrecision(precision);
        BigDecimal result = x.setScale(precision, 6);

        while(true) {
            BigDecimal temp = result.subtract(this.tanh(result).subtract(x).multiply(this.cosh(result).pow(2)));
            if (result.subtract(temp).abs().compareTo(P) != 1) {
                super.clearCachePrecision();
                return temp.setScale(precision, 6);
            }

            result = temp.setScale(precision + 5, 6);
        }
    }

    public BigDecimal deg(BigDecimal x) {
        return x.multiply(B180).divide(PI, super.precision(), 6);
    }

    public BigDecimal rad(BigDecimal x) {
        return x.multiply(PI).divide(B180, super.precision(), 6);
    }

    private BigDecimal ofPrecision(int precision) {
        if (precision <= 0) {
            return PREC_CACHE[0];
        } else {
            return precision < PREC_CACHE.length ? PREC_CACHE[precision] : BigDecimal.ONE.divide(new BigDecimal(BigInteger.TEN.pow(precision)));
        }
    }
}
