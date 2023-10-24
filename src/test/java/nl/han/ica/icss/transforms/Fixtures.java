package nl.han.ica.icss.transforms;

import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.ColorLiteral;
import nl.han.ica.icss.ast.literals.PixelLiteral;
import nl.han.ica.icss.ast.selectors.ClassSelector;
import nl.han.ica.icss.ast.selectors.IdSelector;
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

    public static AST transformedLevel3() {
        Stylesheet stylesheet = new Stylesheet();

        stylesheet.addChild(
                new Stylerule()
                        .addChild(new TagSelector("p"))
                        .addChild(new Declaration("background-color")
                                .addChild(new ColorLiteral("#ffffff"))
                        )
                        .addChild(new Declaration("width")
                                .addChild(new PixelLiteral("500px"))
                        )
                        .addChild(new Declaration("color")
                                .addChild(new ColorLiteral("#124532"))
                        )
                        .addChild(new Declaration("background-color")
                                .addChild(new ColorLiteral("#000000"))
                        )
                        .addChild(new Declaration("height")
                                .addChild(new PixelLiteral("20px"))
                        )
        ).addChild(
                new Stylerule()
                        .addChild(new TagSelector("a"))
                        .addChild(new Declaration("color")
                                .addChild(new ColorLiteral("#ff0000"))
                        )
        ).addChild(
                new Stylerule()
                        .addChild(new IdSelector("#menu"))
                        .addChild(new Declaration("width")
                                .addChild(new PixelLiteral("520px"))
                        )
        ).addChild(
                new Stylerule()
                        .addChild(new ClassSelector(".menu"))
                        .addChild(new Declaration("color")
                                .addChild(new ColorLiteral("#000000"))
                        )
                        .addChild(new Declaration("background-color")
                                .addChild(new ColorLiteral("#ff0000"))
                        )
        );

        return new AST(stylesheet);
    }
}
