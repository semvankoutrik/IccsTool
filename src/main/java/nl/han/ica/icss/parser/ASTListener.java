package nl.han.ica.icss.parser;


import nl.han.ica.datastructures.HANStack;
import nl.han.ica.datastructures.IHANStack;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.*;
import nl.han.ica.icss.ast.operations.AddOperation;
import nl.han.ica.icss.ast.operations.MultiplyOperation;
import nl.han.ica.icss.ast.operations.SubtractOperation;
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
    public void enterOperationValue(ICSSParser.OperationValueContext ctx) {
        printEnter("OperationValue", ctx.getText());

        while(currentContainer.peek() instanceof MultiplyOperation && ((MultiplyOperation) currentContainer.peek()).rhs != null) {
            currentContainer.pop();
        }
    }

    @Override
    public void enterMultiply(ICSSParser.MultiplyContext ctx) {
        printEnter("Multiply", ctx.getText());

        var multiply = new MultiplyOperation();

        currentContainer.peek().addChild(multiply);
        currentContainer.push(multiply);
    }

    @Override
    public void exitMultiply(ICSSParser.MultiplyContext ctx) {
        printExit("Multiply", ctx.getText());
        if(currentContainer.peek() instanceof MultiplyOperation) currentContainer.pop();
    }

    @Override
    public void enterAdd(ICSSParser.AddContext ctx) {
        printEnter("Add", ctx.getText());

        var add = new AddOperation();

        if(currentContainer.peek() instanceof MultiplyOperation) {
            var multiplyList = new HANStack<MultiplyOperation>();

            var multiply = currentContainer.pop();
            multiplyList.push((MultiplyOperation) multiply);

            while(currentContainer.peek() instanceof MultiplyOperation) {
                multiply = currentContainer.pop();
                multiplyList.push((MultiplyOperation) multiply);
            }

            add.addChild(multiply);
            currentContainer
                    .peek()
                    .removeChild(multiply)
                    .addChild(add);
            currentContainer.push(add);

            while(multiplyList.peek() != null) {
                currentContainer.push(multiplyList.pop());
            }
        } else {
            currentContainer.peek().addChild(add);
            currentContainer.push(add);
        }
    }

    @Override
    public void exitAdd(ICSSParser.AddContext ctx) {
        printExit("Add", ctx.getText());
        if(currentContainer.peek() instanceof AddOperation) currentContainer.pop();
    }

    @Override
    public void enterSubtract(ICSSParser.SubtractContext ctx) {
        printEnter("Subtract", ctx.getText());

        var subtract = new SubtractOperation();

        if(currentContainer.peek() instanceof MultiplyOperation) {
            var multiplyList = new HANStack<MultiplyOperation>();

            var multiply = currentContainer.pop();
            multiplyList.push((MultiplyOperation) multiply);

            while(currentContainer.peek() instanceof MultiplyOperation) {
                multiply = currentContainer.pop();
                multiplyList.push((MultiplyOperation) multiply);
            }

            subtract.addChild(multiply);
            currentContainer
                    .peek()
                    .removeChild(multiply)
                    .addChild(subtract);
            currentContainer.push(subtract);

            while(multiplyList.peek() != null) {
                currentContainer.push(multiplyList.pop());
            }
        } else {
            currentContainer.peek().addChild(subtract);
            currentContainer.push(subtract);
        }
    }

    @Override
    public void exitSubtract(ICSSParser.SubtractContext ctx) {if(currentContainer.peek() instanceof SubtractOperation)currentContainer.pop();}

    @Override
    public void enterScalarValue(ICSSParser.ScalarValueContext ctx) {
        printEnter("ScalarValue", ctx.getText());

        currentContainer.peek().addChild(new ScalarLiteral(ctx.getText()));
    }

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
        printEnter("PercentageValue", ctx.getText());

        currentContainer.peek().addChild(new PercentageLiteral(ctx.getText()));
    }

    @Override
    public void enterBoolValue(ICSSParser.BoolValueContext ctx) {
        printEnter("BoolValue", ctx.getText());

        currentContainer.peek().addChild(new BoolLiteral(ctx.getText()));
    }

    @Override
    public void enterVariableReference(ICSSParser.VariableReferenceContext ctx) {
        printEnter("VariableReference", ctx.getText());

        var currentNode = currentContainer.peek();

        currentNode.addChild(new VariableReference(ctx.getText()));
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
        printEnter("Property", ctx.getText());
    }

    @Override
    public void enterPropertyName(ICSSParser.PropertyNameContext ctx) {
        String value = ctx.getText();

        printEnter("PropertyName", value);

        Declaration declaration = new Declaration(value);

        currentContainer.peek().addChild(declaration);

        currentContainer.push(declaration);
    }

    @Override
    public void exitProperty(ICSSParser.PropertyContext ctx) {
        printExit("Property", ctx.getText());
        currentContainer.pop();
    }

    @Override
    public void enterIfClause(ICSSParser.IfClauseContext ctx) {
        printEnter("IfClause", ctx.getText());

        var ifClause = new IfClause();

        currentContainer.peek().addChild(ifClause);
        currentContainer.push(ifClause);
    }

    @Override
    public void exitIfClause(ICSSParser.IfClauseContext ctx) {
        printExit("IfClause", ctx.getText());
        currentContainer.pop();
    }

    @Override
    public void enterElseClause(ICSSParser.ElseClauseContext ctx) {
        printEnter("ElseClause", ctx.getText());

        var elseClause = new ElseClause();

        currentContainer.peek().addChild(elseClause);
        currentContainer.push(elseClause);
    }

    @Override
    public void exitElseClause(ICSSParser.ElseClauseContext ctx) {
        printExit("ElseClause", ctx.getText());
        currentContainer.pop();
    }

    private Selector getSelector(String identifier) {
        Selector selector;

        // class
        String tagIdRegex = "[a-z][a-z0-9\\-]*";
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

    private void printEnter(String enter, @Nullable String value) {
//        System.out.println("ENTER " + enter);
//        if (value != null) System.out.println(value);
//        System.out.println();
    }

    private void printExit(String exit, @Nullable String value) {
//        System.out.println("EXIT " + exit);
//        if (value != null) System.out.println(value);
//        System.out.println();
    }
}