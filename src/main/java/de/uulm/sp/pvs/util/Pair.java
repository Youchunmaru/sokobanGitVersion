package de.uulm.sp.pvs.util;
/**
 * @author Samuel Gröner
 * implements a pair of to objects
 * @version 21.05.20
 *          updated JavaDoc
 * */
public final class Pair<F extends Comparable,S extends Comparable> implements Comparable<Pair<F,S>> {
    /**
     * is the first object in the Pair
     * */
    private final F first;
    /**
     * is the second object in the pair
     * */
    private final S second;
    /**
     * the constructor of the class {@link Pair}
     * @param first the first of the Pair
     * @param second the second of the Pair
     * */
    Pair(F first,S second){

        this.first = first;
        this.second = second;
    }
    /**
     * standard equals
     * @param o object to check with
     * @return if they equal or not
     * */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pair)) return false;

        Pair<?, ?> pair = (Pair<?, ?>) o;

        if (!first.equals(pair.first)) return false;
        return second.equals(pair.second);
    }
    /**
     * standard hashCode
     * @return the hashCode
     * */
    @Override
    public int hashCode() {
        int result = first.hashCode();
        result = 31 * result + second.hashCode();
        return result;
    }
    /**
     * compares two {@link Pair} with another
     * the first object of the {@link Pair} is more weighted than the second
     * @param fsPair the pair to compare to
     * @return 1 if it is greater, 0 if it is the same, -1 if it is less
     * */
    @Override
    public int compareTo(Pair<F,S> fsPair) {
        F firstPair = fsPair.getFirst();
        S secondPair = fsPair.getSecond();

        int firstCompared = firstPair.compareTo(this.first);
        int secondCompared = secondPair.compareTo(this.second);
        //conditional operator if the first ones are the same
        // we state the compare of the second -> weight condition
        return (firstCompared != 0) ? firstCompared : secondCompared;
    }
    /**
     * getter function for the first object
     * @return this.first
     * */
    public F getFirst() {
        return first;
    }
    /**
     * getter function for the second object
     * @return this.second
     * */
    public S getSecond() {
        return second;
    }
    /**
     * turns the Pair into a String
     * @return turns this.Pair into a String
     * */
    public String toString(){
        return "Pair:{first: " + first + "; second: " + second + "}";
    }
}
