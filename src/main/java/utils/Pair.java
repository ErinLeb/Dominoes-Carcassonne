package utils;

/**
 * This class represents a pair of objects of type {@code A} and {@code B}.
 */
public class Pair<A, B> {

    public A first; // first object of the pair
    public B second;// second object of the pair

    // Constructor

    public Pair(A a, B b) {
        this.first = a;
        this.second = b;
    }

    // Methods

    @Override
    public String toString() {
        return "Pair [first=" + first + ", second=" + second + "]";
    }
}
