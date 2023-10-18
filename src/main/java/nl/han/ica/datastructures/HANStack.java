package nl.han.ica.datastructures;

public class HANStack<T> implements IHANStack<T> {
    private IHANLinkedList<T> linkedList = new HANLinkedList<>();

    @Override
    public void push(T value) {
        linkedList.insert(linkedList.getSize(), value);
    }

    @Override
    public T pop() {
        T value = linkedList.get(linkedList.getSize() - 1);
        if(value != null)linkedList.delete(linkedList.getSize() - 1);

        return value;
    }

    @Override
    public T peek() {
        return linkedList.get(linkedList.getSize() - 1);
    }
}
