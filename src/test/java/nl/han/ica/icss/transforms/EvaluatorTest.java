package nl.han.ica.icss.transforms;

import nl.han.ica.icss.ast.AST;
import nl.han.ica.utils.ASTHelper;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EvaluatorTest {
    @Test
    public void testFile() throws IOException {
        AST actual = ASTHelper.parseTestFile("evaluatorFiles/test.icss");

        new Evaluator().apply(actual);

        AST expected = Fixtures.test();

        assertEquals(expected, actual);
    }

    @Test
    public void testLevel3() throws IOException {
        AST actual = ASTHelper.parseTestFile("level3.icss");

        new Evaluator().apply(actual);

        AST expected = Fixtures.transformedLevel3();

        assertEquals(expected, actual);
    }
}
