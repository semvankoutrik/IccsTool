package nl.han.ica.icss.checker;

import nl.han.ica.icss.ast.AST;
import nl.han.ica.icss.checker.errors.MissingScalarOperandInMultiplication;
import nl.han.ica.icss.checker.errors.OperandsNotCompatible;
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
    public void checkVariableAssigment() throws IOException {
        AST ast = ASTHelper.parseTestFile("level3.icss");
        sut.check(ast);

        var variables = sut.getVariables();
        assertNotNull(variables.get("LinkColor"));
        assertNotNull(variables.get("ParWidth"));
        assertNotNull(variables.get("AdjustColor"));
        assertNotNull(variables.get("UseLinkColor"));
        assertNull(variables.get("NONEXISTENTVARIABLE"));
    }

    @Test
    public void nonExistentVariableHasError() throws IOException {
        AST ast = ASTHelper.parseTestFile("checkerFiles/invalid_variable.icss");
        sut.check(ast);

        var errors = ast.getErrors();

        assertTrue(errors
                .stream()
                .anyMatch(error -> Objects.equals(error.description, "Variable NONEXISTENT not found"))
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
}
