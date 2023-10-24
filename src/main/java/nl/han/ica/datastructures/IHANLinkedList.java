package nl.han.ica.datastructures;

import java.util.function.Function;

public interface IHANLinkedList<T> {
    /**
     * Adds value to the front of the list
     * @param value generic value to be added
     */
    void addFirst(T value);

    /**
     * Clears list. Size equals 0 afterwards
     */
    void clear();

    /**
     * Adds value to index position
     * @param index the position
     * @param value the value to add at index
     */
    void insert(int index, T value);

    /**
     * Adds a value to the highest index + 1 (at the end).
     * @param value The value to add
     */
    void add(T value);

    /**
     * Deletes value at position
     * @param pos position where value is deleted
     */
    void delete(int pos);

    /**
     * Returns generic value T at position
     * @param pos position to look up value
     * @return value at position pos
     */
    T get(int pos);

    /**
     * Removes first element
     */
    void removeFirst();

    /**
     * Removes last element
     */
    void removeLast();

    /**
     * Returns first element in O(n) time
     * @return first element
     */
    T getFirst();

    T getWhere(Function<T, Boolean> func);

    /**
     * Determines size of the list, equals the number of stored items but not the header node
     * @return number of items in list
     */
    int getSize();
}
