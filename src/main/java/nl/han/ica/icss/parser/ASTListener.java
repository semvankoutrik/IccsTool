package nl.han.ica.icss.parser;


import nl.han.ica.datastructures.HANStack;
import nl.han.ica.datastructures.IHANStack;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.selectors.ClassSelector;
import nl.han.ica.icss.ast.selectors.IdSelector;
import nl.han.ica.icss.ast.selectors.TagSelector;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

/**
 * This class extracts the ICSS Abstract Syntax Tree from the Antlr Parse tree.
 */
public class ASTListener extends ICSSBaseListener {

    // Accumulator attributes:
    private AST ast;

    // Use this to keep track of the parent nodes when recursively traversing the ast
    private IHANStack<ASTNode> currentContainer;

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
    public void exitStylesheet(ICSSParser.StylesheetContext ctx) {
    }

    @Override
    public void enterIdentifier(ICSSParser.IdentifierContext ctx) {
        System.out.println("ENTER Identifier");
        System.out.println(ctx.getText());

        String identifier = ctx.getChild(0).getText();

        ASTNode node;

        // class
        if (identifier.startsWith(".")) {
            node = new ClassSelector(identifier.substring(1));
        }
        // id
        else if (identifier.startsWith("#")) {
            node = new IdSelector(identifier.substring(1));
        }
        // tag
        else {
            node = new TagSelector(identifier);
        }

        currentContainer.peek().addChild(node);
        currentContainer.push(node);
    }

    @Override
    public void exitIdentifier(ICSSParser.IdentifierContext ctx) {
        System.out.println("EXIT Identifier");
        currentContainer.pop();
    }

    @Override
    public void enterProperties(ICSSParser.PropertiesContext ctx) {
        System.out.println("ENTER properties");
        System.out.println(ctx.getText());
    }

    @Override
    public void exitProperties(ICSSParser.PropertiesContext ctx) {
        super.exitProperties(ctx);
    }

    @Override
    public void enterProperty(ICSSParser.PropertyContext ctx) {
        System.out.println("ENTER property");
        System.out.println(ctx.getText());
    }

    @Override
    public void exitProperty(ICSSParser.PropertyContext ctx) {
        super.exitProperty(ctx);
    }

    @Override
    public void enterColor(ICSSParser.ColorContext ctx) {
        System.out.println("ENTER Color");
        System.out.println(ctx.getText());
    }

    @Override
    public void exitColor(ICSSParser.ColorContext ctx) {
        super.exitColor(ctx);
    }

    @Override
    public void enterBackgroundColor(ICSSParser.BackgroundColorContext ctx) {
        System.out.println("ENTER BackgroundColor");
        System.out.println(ctx.getText());
    }

    @Override
    public void exitBackgroundColor(ICSSParser.BackgroundColorContext ctx) {
        super.exitBackgroundColor(ctx);
    }

    @Override
    public void enterWidth(ICSSParser.WidthContext ctx) {
        System.out.println("ENTER Width");
        System.out.println(ctx.getText());
    }

    @Override
    public void exitWidth(ICSSParser.WidthContext ctx) {
        super.exitWidth(ctx);
    }

    @Override
    public void enterEveryRule(ParserRuleContext ctx) {
    }

    @Override
    public void exitEveryRule(ParserRuleContext ctx) {
        super.exitEveryRule(ctx);
    }

    @Override
    public void visitTerminal(TerminalNode node) {
        super.visitTerminal(node);
    }

    @Override
    public void visitErrorNode(ErrorNode node) {
        super.visitErrorNode(node);
    }
}