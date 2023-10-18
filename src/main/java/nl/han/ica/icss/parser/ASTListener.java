package nl.han.ica.icss.parser;


import nl.han.ica.datastructures.HANStack;
import nl.han.ica.datastructures.IHANStack;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.BoolLiteral;
import nl.han.ica.icss.ast.literals.ColorLiteral;
import nl.han.ica.icss.ast.literals.PercentageLiteral;
import nl.han.ica.icss.ast.literals.PixelLiteral;
import nl.han.ica.icss.ast.selectors.ClassSelector;
import nl.han.ica.icss.ast.selectors.IdSelector;
import nl.han.ica.icss.ast.selectors.TagSelector;

import javax.annotation.Nullable;
import java.util.ArrayList;

/**
 * This class extracts the ICSS Abstract Syntax Tree from the Antlr Parse tree.
 */
public class ASTListener extends ICSSBaseListener {

    // Accumulator attributes
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
        printEnter("Stylesheet", null);

        Stylesheet stylesheetNode = new Stylesheet();
        currentContainer.push(stylesheetNode);

        ast.setRoot(stylesheetNode);
    }

    @Override
    public void enterStylerule(ICSSParser.StyleruleContext ctx) {
        printEnter("Identifier", ctx.getText());

        String identifier = ctx.getChild(0).getText();

        Selector selector = getSelector(identifier);

        Stylerule stylerule = new Stylerule(selector, new ArrayList<>());

        currentContainer.peek().addChild(stylerule);
        currentContainer.push(stylerule);
    }

    @Override
    public void exitStylerule(ICSSParser.StyleruleContext ctx) {currentContainer.pop();}

    @Override
    public void enterColorValue(ICSSParser.ColorValueContext ctx) {
        printEnter("ColorValue", ctx.getText());

        currentContainer.peek().addChild(new ColorLiteral(ctx.getText()));
    }

    @Override
    public void enterPixelValue(ICSSParser.PixelValueContext ctx) {
        printEnter("PixelValue", ctx.getText());

        currentContainer.peek().addChild(new PixelLiteral(ctx.getText()));
    }

    @Override
    public void enterPercentageValue(ICSSParser.PercentageValueContext ctx) {
        printEnter("PixelValue", ctx.getText());

        currentContainer.peek().addChild(new PercentageLiteral(ctx.getText()));
    }

    @Override
    public void enterBoolValue(ICSSParser.BoolValueContext ctx) {
        printEnter("PixelValue", ctx.getText());

        currentContainer.peek().addChild(new BoolLiteral(ctx.getText()));
    }

    @Override
    public void enterVariableReference(ICSSParser.VariableReferenceContext ctx) {
        printEnter("VariableReference", ctx.getText());

        var currentNode = currentContainer.peek();

        currentNode.addChild(new VariableReference(ctx.getText()));
    }

    @Override
    public void enterVariableValue(ICSSParser.VariableValueContext ctx) {
        printEnter("VariableValue", ctx.getText());
    }

    @Override
    public void enterVariableAssignment(ICSSParser.VariableAssignmentContext ctx) {
        printEnter("VariableAssignment", ctx.getText());

        VariableAssignment assignment = new VariableAssignment();

        currentContainer.peek().addChild(assignment);
        currentContainer.push(assignment);
    }

    @Override
    public void exitVariableAssignment(ICSSParser.VariableAssignmentContext ctx) {
        currentContainer.pop();
    }

    @Override
    public void enterProperties(ICSSParser.PropertiesContext ctx) {
        printEnter("Properties", ctx.getText());
    }

    @Override
    public void enterProperty(ICSSParser.PropertyContext ctx) {
        String value = ctx.getText();

        printEnter("Property", value);

        Declaration declaration = new Declaration(value);

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
        printEnter("Color", ctx.getText());

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
    public void enterBackgroundColor(ICSSParser.BackgroundColorContext ctx) {
        printEnter("BackgroundColor", ctx.getText());

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
        printEnter("Width", ctx.getText());

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
        } else {
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

    private void printEnter(String enter, @Nullable String value) {
        System.out.println("ENTER " + enter);
        if (value != null) System.out.println(value);
        System.out.println();
    }
}