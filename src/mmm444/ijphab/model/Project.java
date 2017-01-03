package mmm444.ijphab.model;

import org.jetbrains.annotations.NotNull;

@SuppressWarnings({"CanBeFinal", "unused"})
public class Project {
  private String name;
  private String phid;
  private String icon;
  private String color;

  @NotNull
  public String getName() {
    return name;
  }

  @NotNull
  public String getPhid() {
    return phid;
  }

  public String getIcon() {
    return icon;
  }

  public String getColor() {
    return color;
  }
}
