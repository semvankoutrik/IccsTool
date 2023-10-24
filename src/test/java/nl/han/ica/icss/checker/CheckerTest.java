package nl.han.ica.icss.checker;

import nl.han.ica.icss.ast.AST;
import nl.han.ica.icss.checker.errors.*;
import nl.han.ica.utils.ASTHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class CheckerTest {
    private Checker sut;

    @BeforeEach
    public void setup() {
        this.sut = new Checker();
    }

    @Test
    public void nonExistentVariableHasError() throws IOException {
        AST ast = ASTHelper.parseTestFile("checkerFiles/invalid_variable.icss");
        sut.check(ast);

        var errors = ast.getErrors();

        assertTrue(errors
                .stream()
                .anyMatch(error -> Objects.equals(error.description, VariableNotFound.create("NONEXISTENT").getError()))
        );
    }

    @Test
    public void throwIfInvalidOperands() throws IOException {
        AST ast = ASTHelper.parseTestFile("checkerFiles/invalid_operation.icss");
        sut.check(ast);

        var errors = ast.getErrors();

        assertTrue(errors
                .stream()
                .anyMatch(error -> Objects.equals(
                        error.description,
                        OperandsNotCompatible.create("20", "20px").getError()
                ))
        );
    }

    @Test
    public void throwIfNeitherOperandIsScalar() throws IOException {
        AST ast = ASTHelper.parseTestFile("checkerFiles/invalid_operation.icss");
        sut.check(ast);

        var errors = ast.getErrors();

        assertTrue(errors
                .stream()
                .anyMatch(error -> Objects.equals(
                        error.description,
                        MissingScalarOperandInMultiplication.create("20%", "30%").getError()
                ))
        );
    }

    @Test
    public void noErrorsOnValidOperations() throws IOException {
        AST ast = ASTHelper.parseTestFile("checkerFiles/valid_operation.icss");
        sut.check(ast);

        var errors = ast.getErrors();

        assertEquals(0, errors.size());
    }

    @Test
    public void throwErrorOnInvalidColorDeclarations() throws IOException {
        AST ast = ASTHelper.parseTestFile("checkerFiles/invalid_declarations.icss");
        sut.check(ast);

        var errors = ast.getErrors();

        assertTrue(errors
                .stream()
                .anyMatch(error -> Objects.equals(
                        error.description,
                        InvalidDeclarationValue.create("color", "2px").getError()
                ))
        );

        assertTrue(errors
                .stream()
                .anyMatch(error -> Objects.equals(
                        error.description,
                        InvalidDeclarationValue.create("background-color", "40%").getError()
                ))
        );
    }

    @Test
    public void throwErrorOnInvalidSizeDeclarations() throws IOException {
        AST ast = ASTHelper.parseTestFile("checkerFiles/invalid_declarations.icss");
        sut.check(ast);

        var errors = ast.getErrors();

        assertTrue(errors
                .stream()
                .anyMatch(error -> Objects.equals(
                        error.description,
                        InvalidDeclarationValue.create("width", "#ffffff").getError()
                ))
        );

        assertTrue(errors
                .stream()
                .anyMatch(error -> Objects.equals(
                        error.description,
                        InvalidDeclarationValue.create("height", "TRUE").getError()
                ))
        );

        assertTrue(errors
                .stream()
                .anyMatch(error -> Objects.equals(
                        error.description,
                        InvalidDeclarationValue.create("width", "#000000").getError()
                ))
        );

        assertTrue(errors
                .stream()
                .anyMatch(error -> Objects.equals(
                        error.description,
                        InvalidDeclarationValue.create("height", "FALSE").getError()
                ))
        );
    }

    @Test
    public void errorWhenVariableDeclaredAfterReference() throws IOException {
        AST ast = ASTHelper.parseTestFile("checkerFiles/variable_after_declaration.icss");
        sut.check(ast);

        var errors = ast.getErrors();

        assertTrue(errors
                .stream()
                .anyMatch(error -> Objects.equals(error.description, VariableNotFound.create("Color").getError()))
        );
    }

    @Test
    public void noErrorVariableDeclaredInStylerule() throws IOException {
        AST ast = ASTHelper.parseTestFile("checkerFiles/variable_inside_stylerule.icss");
        sut.check(ast);

        var errors = ast.getErrors();

        assertEquals(0, errors.size());
    }

    @Test
    public void noErrorValidFile() throws IOException {
        AST ast = ASTHelper.parseTestFile("checkerFiles/valid.icss");
        sut.check(ast);

        var errors = ast.getErrors();

        assertEquals(0, errors.size());
    }

    @Test
    public void ifClauseNoError() throws IOException {
        AST ast = ASTHelper.parseTestFile("checkerFiles/valid_ifclause.icss");
        sut.check(ast);

        var errors = ast.getErrors();

        assertEquals(0, errors.size());
    }

    @Test
    public void invalidConditionalExpression() throws IOException {
        AST ast = ASTHelper.parseTestFile("checkerFiles/invalid_ifclause.icss");
        sut.check(ast);

        var errors = ast.getErrors();

        assertTrue(errors
                .stream()
                .anyMatch(error -> Objects.equals(error.description, InvalidConditionalExpression.create("500px").getError()))
        );
    }

    @Test
    public void errorVariableOfIfUsedInElse() throws IOException {
        AST ast = ASTHelper.parseTestFile("checkerFiles/invalid_ifclause.icss");
        sut.check(ast);

        var errors = ast.getErrors();

        assertTrue(errors
                .stream()
                .anyMatch(error -> Objects.equals(error.description, VariableNotFound.create("Color").getError()))
        );
    }
}
