package nl.han.ica.icss.transforms;

import nl.han.ica.datastructures.IHANLinkedList;
import nl.han.ica.icss.ast.Literal;
import nl.han.ica.icss.ast.Operation;
import nl.han.ica.icss.ast.literals.PixelLiteral;

import java.util.HashMap;

public class OperationEvaluator {
    public static Literal evaluate(Operation operation, IHANLinkedList<HashMap<String, Literal>> variables) {
        return new PixelLiteral("");
    }
}
