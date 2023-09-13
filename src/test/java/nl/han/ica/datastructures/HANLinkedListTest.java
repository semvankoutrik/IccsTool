package nl.han.ica.datastructures;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HANLinkedListTest {
    @Test
    public void returnsCorrectSize() {
        HANLinkedList<String> list = create();

        assertEquals(list.getSize(), 5);

        list.removeFirst();
        list.removeFirst();

        assertEquals(list.getSize(), 3);

        list.removeFirst();
        list.removeFirst();
        list.removeFirst();

        assertEquals(list.getSize(), 0);
    }

    @Test
    public void listReturnsCorrectItemAtPos() {
        HANLinkedList<String> list = create();

        String valueAtStart = list.get(0);
        String valueAtEnd = list.get(4);
        String valueAt3 = list.get(3);

        assertEquals("a", valueAtStart);
        assertEquals("e",valueAtEnd);
        assertEquals("d", valueAt3);
    }

    @Test
    public void itemInsertedAtCorrectPosition() {
        HANLinkedList<String> list = create();

        list.insert(0, "insert1");
        assertEquals( "insert1", list.getFirst());

        list.insert(5, "insert2");

        assertEquals("insert2", list.get(5));
        assertEquals("a", list.get(1));
    }

    private HANLinkedList<String> create() {
        HANLinkedList<String> list = new HANLinkedList<>();
        list.addFirst("e");
        list.addFirst("d");
        list.addFirst("c");
        list.addFirst("b");
        list.addFirst("a");

        return list;
    }
}
