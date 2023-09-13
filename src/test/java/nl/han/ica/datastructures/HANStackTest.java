package nl.han.ica.datastructures;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HANStackTest {
    @Test
    public void pushPopPeekTest() {
        HANStack<String> stack = new HANStack<>();

        stack.push("a");
        stack.push("b");
        stack.push("c");
        stack.push("d");

        assertEquals("d", stack.pop());
        assertEquals("c", stack.peek());
        assertEquals("c", stack.peek());

        stack.push("e");
        assertEquals("e", stack.pop());
    }
}
