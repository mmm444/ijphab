package mmm444.ijphab;

import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class PhabricatorIconBuilder {
  private static final Font ICON_FONT;
  private static final Map<String, BufferedImage> IMAGE_CACHE;

  static {
    ICON_FONT = loadIconFont();
    IMAGE_CACHE = new HashMap<>();
  }

  private static Font loadIconFont() {
    try (InputStream is = PhabricatorIconBuilder.class.getResourceAsStream("/fonts/fontawesome-webfont.ttf")) {
      return Font.createFont(Font.TRUETYPE_FONT, is);
    }
    catch (FontFormatException | IOException e) {
      return null;
    }
  }

  @SuppressWarnings({"SameParameterValue"})
  static Icon getIcon(@NotNull String icon, @NotNull String color, int size) {
    BufferedImage image = IMAGE_CACHE.computeIfAbsent(color + icon, k -> drawIcon(icon, color, size));
    return new ImageIcon(image);
  }

  @SuppressWarnings("UseJBColor")
  @NotNull
  private static BufferedImage drawIcon(@NotNull String icon, @NotNull String color, int size) {
    BufferedImage image = UIUtil.createImage(size, size, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2 = image.createGraphics();
    @SuppressWarnings("unchecked")
    RenderingHints hints =
      new RenderingHints((Map<RenderingHints.Key, ?>)Toolkit.getDefaultToolkit().getDesktopProperty("awt.font.desktophints"));
    hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    hints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    hints.put(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
    g2.setRenderingHints(hints);
    String iconChar = PhabricatorIconData.ICONS.get(icon);

    if (iconChar == null || ICON_FONT == null) {
      iconChar = "?";
    }
    else {
      Font font = ICON_FONT.deriveFont(Font.PLAIN, size - 4);
      g2.setFont(font);
    }

    PhabricatorIconData.ColorFamily c = PhabricatorIconData.COLORS.get(color);
    if (c == null) {
      c = PhabricatorIconData.COLORS.values().iterator().next();
    }

    int rr = 4;
    g2.setColor(new Color(0, 0, 0, 0));
    g2.fillRect(0, 0, size, size);
    g2.setColor(c.background);
    g2.fillRoundRect(0, 0, size - 1, size - 1, rr, rr);
    g2.setColor(c.lightBorder);
    g2.drawRoundRect(0, 0, size - 1, size - 1, rr, rr);

    FontMetrics fm = g2.getFontMetrics();
    Rectangle2D b = fm.getStringBounds(iconChar, g2);

    AffineTransform t = new AffineTransform();
    t.translate((size - b.getWidth()) / 2., size - ((size - b.getHeight() + fm.getDescent()) / 2.));
    g2.setTransform(t);
    g2.setColor(c.icon);
    g2.drawString(iconChar, 0, 0);
    return image;
  }
}

