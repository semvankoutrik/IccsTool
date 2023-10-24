package nl.han.ica.icss.transforms;

import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.PixelLiteral;
import nl.han.ica.icss.ast.selectors.TagSelector;

public class Fixtures {
    public static AST test() {
        Stylesheet stylesheet = new Stylesheet();

        stylesheet.addChild(
                (new Stylerule())
                        .addChild(new TagSelector("p"))
                        .addChild((new Declaration("width"))
                                .addChild(new PixelLiteral("200px"))
                        )

        );

        return new AST(stylesheet);
    }
}
