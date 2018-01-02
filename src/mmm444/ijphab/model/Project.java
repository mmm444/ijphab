package mmm444.ijphab.model;

import org.jetbrains.annotations.NotNull;

import java.util.List;

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

  void validate(List<String> errs) {
    if (phid == null) {
      errs.add("null phid in a project");
    }
    if (fields == null) {
      errs.add("null fields for project " + phid);
    } else {
      if (fields.icon == null) {
        errs.add("null icon for project " + phid);
      }
      if (fields.color == null) {
        errs.add("null color for project " + phid);
      }
    }
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
