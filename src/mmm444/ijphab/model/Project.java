package mmm444.ijphab.model;

import org.jetbrains.annotations.NotNull;

@SuppressWarnings({"CanBeFinal", "unused"})
public class Project {
  private String phid;
  private Fields fields;

  @NotNull
  public String getName() {
    return fields.name;
  }

  @NotNull
  public String getPhid() {
    return phid;
  }

  public String getIcon() {
    return fields.icon.key;
  }

  public String getColor() {
    return fields.color.key;
  }

  @SuppressWarnings("WeakerAccess")
  public static class Fields {
    private String name;
    private Icon icon;
    private Color color;
  }

  @SuppressWarnings("WeakerAccess")
  public static class Icon {
    String key;
  }

  @SuppressWarnings("WeakerAccess")
  public static class Color {
    String key;
  }
}
