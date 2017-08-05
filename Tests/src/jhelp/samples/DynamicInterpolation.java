package jhelp.samples;

import jhelp.util.gui.JHelpImage;
import jhelp.util.gui.JHelpSprite;
import jhelp.util.gui.UtilImage;
import jhelp.util.gui.dynamic.AnimationPosition;
import jhelp.util.gui.dynamic.BackgroundImage;
import jhelp.util.gui.dynamic.Interpolation;
import jhelp.util.gui.dynamic.JHelpDynamicImage;
import jhelp.util.gui.dynamic.Position;

/**
 * Created by jhelp on 14/07/17.
 */
public class DynamicInterpolation
{
    private JHelpDynamicImage dynamicImage;

    public DynamicInterpolation(int width, int height, Interpolation interpolation)
    {
        this.dynamicImage = new JHelpDynamicImage(width, height);
        JHelpImage image = UtilImage.computeInterpolationImage(interpolation,
                                                               width, height,
                                                               0xFF000000,
                                                               0xFF8888FF, 1,
                                                               0xFFFF0000, 1);
        BackgroundImage backgroundImage = new BackgroundImage(image);
        this.dynamicImage.setBackground(backgroundImage);

        image = this.dynamicImage.getImage();
        int         w       = width >> 4;
        int         h       = height >> 4;
        int         x       = (width - w) >> 1;
        int         yTop    = 0;
        int         yBottom = height - h;
        JHelpSprite sprite  = image.createSprite(x, yBottom, w, h);
        image = sprite.getImage();
        image.startDrawMode();
        image.clear(0xCAFEFACE);
        image.endDrawMode();
        sprite.setVisible(true);
        AnimationPosition<JHelpSprite> animationPosition = new AnimationPosition<>(sprite, Integer.MAX_VALUE,
                                                                                   interpolation);
        animationPosition.addFrame(0, new Position(x, yBottom));
        animationPosition.addFrame(100, new Position(x, yTop));
        animationPosition.addFrame(111, new Position(x, yTop));
        this.dynamicImage.playAnimation(animationPosition);
    }

    public JHelpImage image()
    {
        return this.dynamicImage.getImage();
    }
}
