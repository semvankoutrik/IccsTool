package nl.han.ica.icss.generator;


import nl.han.ica.icss.ast.AST;
import nl.han.ica.icss.ast.Declaration;
import nl.han.ica.icss.ast.Stylerule;

public class Generator {
    public String generate(AST ast) {
        StringBuilder builder = new StringBuilder();

        ast.root.getChildren().forEach(node -> {
            var stylerule = (Stylerule) node;
            var selectorsBuilder = new StringBuilder();

            stylerule.selectors.forEach(selector -> selectorsBuilder.append(selector).append(", "));

            builder.append(selectorsBuilder.substring(0, selectorsBuilder.length() - 2)).append(" {\n");

            stylerule.body.forEach(declarationNode -> {
                var declaration = (Declaration) declarationNode;

                builder.append("  ").append(declaration.property.name).append(": ").append(declaration.expression.toString()).append(";\n");
            });

            builder.append("}\n\n");
        });

        return builder.toString();
    }
}
