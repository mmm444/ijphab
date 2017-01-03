package mmm444.ijphab;

// GENERATED FILE - DO NOT EDIT
// generated on: 2017-01-01 22:36:42 UTC

import java.awt.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

class PhabricatorIconData {
  static final Map<String, String> ICONS = initIcons();
  static final Map<String, ColorFamily> COLORS = initColors();

  @SuppressWarnings("unused")
  static class ColorFamily {
    final Color lightBorder;
    final Color border;
    final Color icon;
    final Color text;
    final Color background;

    ColorFamily(Color lightBorder, Color border, Color icon, Color text, Color background) {
      this.lightBorder = lightBorder;
      this.border = border;
      this.icon = icon;
      this.text = text;
      this.background = background;
    }
  }

  private static Map<String, String> initIcons() {
    Map<String, String> m = new HashMap<>();
    m.put("account", "\uf09d");
    m.put("bugs", "\uf188");
    m.put("cleanup", "\uf014");
    m.put("communication", "\uf0e0");
    m.put("experimental", "\uf0c3");
    m.put("folder", "\uf07b");
    m.put("goal", "\uf11e");
    m.put("group", "\uf0c0");
    m.put("infrastructure", "\uf0c2");
    m.put("milestone", "\uf041");
    m.put("organization", "\uf1ad");
    m.put("policy", "\uf023");
    m.put("project", "\uf0b1");
    m.put("release", "\uf0d1");
    m.put("tag", "\uf02c");
    m.put("timeline", "\uf073");
    m.put("umbrella", "\uf0e9");
    return Collections.unmodifiableMap(m);
  }

  @SuppressWarnings({"UseJBColor", "InspectionUsingGrayColors"})
  private static Map<String, ColorFamily> initColors() {
    Map<String, ColorFamily> m = new HashMap<>();
    m.put("red", new ColorFamily(
      new Color(239, 207, 207),
      new Color(209, 171, 171),
      new Color(200, 90, 90),
      new Color(165, 55, 55),
      new Color(247, 230, 230)
    ));
    m.put("orange", new ColorFamily(
      new Color(248, 220, 195),
      new Color(219, 185, 158),
      new Color(231, 131, 49),
      new Color(186, 96, 22),
      new Color(251, 237, 225)
    ));
    m.put("yellow", new ColorFamily(
      new Color(233, 219, 205),
      new Color(201, 184, 168),
      new Color(155, 148, 110),
      new Color(114, 111, 86),
      new Color(253, 243, 218)
    ));
    m.put("green", new ColorFamily(
      new Color(198, 230, 199),
      new Color(160, 196, 161),
      new Color(76, 167, 78),
      new Color(50, 109, 52),
      new Color(221, 239, 221)
    ));
    m.put("blue", new ColorFamily(
      new Color(207, 219, 227),
      new Color(167, 181, 191),
      new Color(107, 116, 140),
      new Color(70, 76, 92),
      new Color(222, 231, 248)
    ));
    m.put("indigo", new ColorFamily(
      new Color(209, 201, 238),
      new Color(188, 180, 218),
      new Color(134, 114, 212),
      new Color(110, 92, 182),
      new Color(234, 230, 247)
    ));
    m.put("violet", new ColorFamily(
      new Color(224, 209, 231),
      new Color(188, 171, 197),
      new Color(146, 96, 173),
      new Color(105, 66, 127),
      new Color(239, 232, 243)
    ));
    m.put("pink", new ColorFamily(
      new Color(246, 213, 239),
      new Color(213, 174, 205),
      new Color(226, 111, 203),
      new Color(218, 73, 190),
      new Color(251, 234, 248)
    ));
    m.put("grey", new ColorFamily(
      new Color(227, 228, 232),
      new Color(178, 178, 178),
      new Color(117, 117, 117),
      new Color(85, 85, 85),
      new Color(237, 238, 242)
    ));
    m.put("disabled", new ColorFamily(
      new Color(229, 229, 229),
      new Color(203, 203, 203),
      new Color(186, 186, 186),
      new Color(166, 166, 166),
      new Color(243, 243, 243)
    ));
    return Collections.unmodifiableMap(m);
  }
}
