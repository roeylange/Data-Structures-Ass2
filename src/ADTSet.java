

public interface ADTSet<T> {
    T search(int k);
    void insert(T x);
    void delete(T x);
    T minimum();
    T maximum();
    T successor(T x);
    T predecessor(T x);
    void print();
}
