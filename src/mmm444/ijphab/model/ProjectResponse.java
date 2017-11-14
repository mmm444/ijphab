package mmm444.ijphab.model;

import java.util.List;

/**
 * Response to the {@code project.search} method.
 */
@SuppressWarnings({"CanBeFinal", "unused"})
public class ProjectResponse extends MethodResponse {
  private Result result;

  public Result getResult() {
    return result;
  }

  @SuppressWarnings("CanBeFinal")
  public static class Result {
    private List<Project> data;
    private Cursor cursor;

    public List<Project> getData() {
      return data;
    }

    public Cursor getCursor() {
      return cursor;
    }
  }
}
