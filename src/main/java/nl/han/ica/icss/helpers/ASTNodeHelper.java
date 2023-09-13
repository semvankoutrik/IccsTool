package nl.han.ica.icss.helpers;

import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.ast.Declaration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class ASTNodeHelper {
    public static <T extends ASTNode> List<T> getOfType(List<ASTNode> astNodes, Class<T> tClass) {
        if (tClass == Declaration.class) {
            return (List<T>) astNodes.stream().filter(tClass::isInstance).collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    public static <T extends ASTNode> T getFirstOfType(List<ASTNode> astNodes, Class<T> tClass) {
        Optional<T> node = getOfType(astNodes, tClass).stream().findFirst();

        return node.orElse(null);
    }
}
