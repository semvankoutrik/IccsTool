package nl.han.ica.icss.parser;


import nl.han.ica.datastructures.HANStack;
import nl.han.ica.datastructures.IHANStack;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.ColorLiteral;
import nl.han.ica.icss.ast.literals.PercentageLiteral;
import nl.han.ica.icss.ast.literals.PixelLiteral;
import nl.han.ica.icss.ast.selectors.ClassSelector;
import nl.han.ica.icss.ast.selectors.IdSelector;
import nl.han.ica.icss.ast.selectors.TagSelector;
import nl.han.ica.icss.helpers.ASTNodeHelper;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

/**
 * This class extracts the ICSS Abstract Syntax Tree from the Antlr Parse tree.
 */
public class ASTListener extends ICSSBaseListener {

    // Accumulator attributes:
    private final AST ast;
    private final String tagIdRegex = "[a-z][a-z0-9\\-]*";
    private final String variableIdRegex = "[A-Z][a-zA-Z0-9_]*";

    // Use this to keep track of the parent nodes when recursively traversing the ast
    private final IHANStack<ASTNode> currentContainer;

    public ASTListener() {
        ast = new AST();
        currentContainer = new HANStack<>();
    }

    public AST getAST() {
        return ast;
    }

    @Override
    public void enterStylesheet(ICSSParser.StylesheetContext ctx) {
        System.out.println("ENTER stylesheet");
        System.out.println(ctx.getText());

        Stylesheet stylesheetNode = new Stylesheet();
        currentContainer.push(stylesheetNode);

        ast.setRoot(stylesheetNode);
    }

    @Override
    public void enterIdentifier(ICSSParser.IdentifierContext ctx) {
        System.out.println("ENTER Identifier");
        System.out.println(ctx.getText());

        String identifier = ctx.getChild(0).getText();

        if (isVariable(identifier)) {
            VariableAssignment variableAssignment = new VariableAssignment();
        } else {
            Selector selector = getSelector(identifier);

            Stylerule stylerule = new Stylerule(selector, new ArrayList<>());

            currentContainer.peek().addChild(stylerule);
            currentContainer.push(stylerule);
        }
    }

    @Override
    public void exitIdentifier(ICSSParser.IdentifierContext ctx) {
        System.out.println("EXIT Identifier");

        // Pop Selector
        currentContainer.pop();
    }

    @Override
    public void enterProperties(ICSSParser.PropertiesContext ctx) {
        System.out.println("ENTER properties");
        System.out.println(ctx.getText());
    }

    @Override
    public void enterProperty(ICSSParser.PropertyContext ctx) {
        System.out.println("ENTER property");
        System.out.println(ctx.getText());

        String propertyName = ctx.getText().split(":")[0];

        Declaration declaration = new Declaration(propertyName);

        // Add Declaration to Stylerule
        currentContainer.peek().addChild(declaration);

        // Push Declaration
        currentContainer.push(declaration);
    }

    @Override
    public void exitProperty(ICSSParser.PropertyContext ctx) {
        // Pop Declaration
        currentContainer.pop();
    }

    @Override
    public void enterColor(ICSSParser.ColorContext ctx) {
        System.out.println("ENTER Color");
        System.out.println(ctx.getText());

        String value = ctx.getText().split(":")[1];

        // Get value from "color:{value}"
        if (isVariable(value)) {
            addVariableReferenceChild(value);
        } else {
            ColorLiteral node = new ColorLiteral(ctx.getText().split(":")[1]);

            currentContainer.peek().addChild(node);
        }
    }

    @Override
    public void exitColor(ICSSParser.ColorContext ctx) {
        super.exitColor(ctx);
    }

    @Override
    public void enterBackgroundColor(ICSSParser.BackgroundColorContext ctx) {
        System.out.println("ENTER BackgroundColor");
        System.out.println(ctx.getText());

        // Get value from "background-color:{value}"
        String value = ctx.getText().split(":")[1];

        if (isVariable(value)) {
            addVariableReferenceChild(value);
        } else {
            ColorLiteral node = new ColorLiteral(value);

            currentContainer.peek().addChild(node);
        }
    }

    @Override
    public void exitBackgroundColor(ICSSParser.BackgroundColorContext ctx) {
        super.exitBackgroundColor(ctx);
    }

    @Override
    public void enterWidth(ICSSParser.WidthContext ctx) {
        System.out.println("ENTER Width");
        System.out.println(ctx.getText());

        // Get value from "width:{value}"
        String value = ctx.getText().split(":")[1];

        if (isVariable(value)) {
            addVariableReferenceChild(value);
        } else {
            ASTNode node;

            // Check unit of measurement.
            if (value.endsWith("px")) {
                node = new PixelLiteral(value);
            } else if (value.endsWith("%")) {
                node = new PercentageLiteral(value);
            } else {
                throw new RuntimeException("Unit of measurement not recognized for value \"" + value + "\"");
            }

            currentContainer.peek().addChild(node);
        }
    }

    private Selector getSelector(String identifier) {
        Selector selector;

        // class
        if (identifier.startsWith(".")) {
            selector = new ClassSelector(identifier);
        }
        // id
        else if (identifier.startsWith("#")) {
            selector = new IdSelector(identifier);
        }
        // tag
        else if (identifier.matches(tagIdRegex)) {
            selector = new TagSelector(identifier);
        }
        else {
            throw new RuntimeException("Unknown identifier \"" + identifier + "\".");
        }
        return selector;
    }

    private void addVariableReferenceChild(String name) {
        currentContainer.peek().addChild(new VariableReference(name));
    }

    private boolean isVariable(String value) {
        return value.matches(variableIdRegex);
    }
}