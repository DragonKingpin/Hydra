package Pinecone.Framework.System.util.comparator;

import Pinecone.Framework.System.util.Assert;

import java.io.Serializable;
import java.util.Comparator;

public class InvertibleComparator<T> implements Comparator<T>, Serializable {
    private final Comparator<T> comparator;
    private boolean ascending = true;

    public InvertibleComparator(Comparator<T> comparator) {
        Assert.notNull(comparator, "Comparator must not be null");
        this.comparator = comparator;
    }

    public InvertibleComparator(Comparator<T> comparator, boolean ascending) {
        Assert.notNull(comparator, "Comparator must not be null");
        this.comparator = comparator;
        this.setAscending(ascending);
    }

    public void setAscending(boolean ascending) {
        this.ascending = ascending;
    }

    public boolean isAscending() {
        return this.ascending;
    }

    public void invertOrder() {
        this.ascending = !this.ascending;
    }

    public int compare(T o1, T o2) {
        int result = this.comparator.compare(o1, o2);
        if (result != 0) {
            if (!this.ascending) {
                if (-2147483648 == result) {
                    result = 2147483647;
                } else {
                    result *= -1;
                }
            }

            return result;
        } else {
            return 0;
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof InvertibleComparator)) {
            return false;
        } else {
            InvertibleComparator<T> other = (InvertibleComparator)obj;
            return this.comparator.equals(other.comparator) && this.ascending == other.ascending;
        }
    }

    public int hashCode() {
        return this.comparator.hashCode();
    }

    public String toString() {
        return "InvertibleComparator: [" + this.comparator + "]; ascending=" + this.ascending;
    }
}
