package nl.han.ica.icss.helpers;

import nl.han.ica.datastructures.IHANLinkedList;
import nl.han.ica.icss.ast.Expression;

import java.util.HashMap;

public class HANLinkedListHelper {
    public static HashMap<String, Expression> scopeVariablesListToHashMap(IHANLinkedList<HashMap<String, Expression>> allVariables)
    {
        var map = new HashMap<String, Expression>();

        for(int i = 0; i < allVariables.getSize(); i++) {
            map.putAll(allVariables.get(i));
        }

        return map;
    }
}
