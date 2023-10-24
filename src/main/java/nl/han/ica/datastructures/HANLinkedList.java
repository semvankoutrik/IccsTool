package nl.han.ica.datastructures;

import java.util.function.Function;

public class HANLinkedList<T> implements IHANLinkedList<T> {
    private final ListNode<T> header = new ListNode<>(null);

    @Override
    public void addFirst(T value) {
        ListNode<T> current = header.getNext();
        ListNode<T> newFirst = new ListNode<>(value);

        newFirst.setNext(current);
        header.setNext(newFirst);
    }

    @Override
    public void clear() {
        header.setNext(null);
    }

    @Override
    public void insert(int index, T value) {
        ListNode<T> toInsert = new ListNode<>(value);
        ListNode<T> current = header;

        for (int i = 0; i <= index; i++) {
            if(index == i) {
                toInsert.setNext(current.getNext());
                current.setNext(toInsert);

                return;
            } else {
                current = current.getNext();
            }
        }

        throw new IndexOutOfBoundsException();
    }

    @Override
    public void add(T value) {
        ListNode<T> current = header;

        while(current.getNext() != null) {
            current = current.getNext();
        }

        current.setNext(new ListNode<>(value));
    }

    @Override
    public void delete(int pos) {
        ListNode<T> current = header;

        for (int i = 0; i <= pos; i++) {
            if(pos == i) {
                current.setNext(current.getNext().getNext());

                return;
            } else {
                current = current.getNext();
            }
        }

        throw new IndexOutOfBoundsException();
    }

    @Override
    public T get(int pos) {
        ListNode<T> current = header;

        for (int i = 0; i <= pos; i++) {
            if(pos == i) {
                return current.getNext().getData();
            } else {
                current = current.getNext();
            }
        }

        return null;
    }

    @Override
    public void removeFirst() {
        header.setNext(header.getNext().getNext());
    }

    @Override
    public void removeLast() {
        ListNode<T> previous = header;
        ListNode<T> current = header;

        while(current.getNext() != null) {
            previous = current;
            current = current.getNext();
        }

        if(previous == current) header.setData(null);

        previous.setNext(null);
    }

    @Override
    public T getFirst() {
        return header.getNext().getData();
    }

    @Override
    public int getSize() {
        ListNode<T> current = header;
        int size = 0;

        while(current.getNext() != null) {
            current = current.getNext();
            size++;
        }

        return size;
    }
}
