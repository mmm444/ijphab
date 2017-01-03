package mmm444.ijphab.model;

import java.util.Map;

/**
 * Response to the {@code project.query} method.
 */
@SuppressWarnings({"CanBeFinal", "unused"})
public class ProjectResponse extends MethodResponse {
  private Result result;

  public Result getResult() {
    return result;
  }

  @SuppressWarnings("CanBeFinal")
  public static class Result {
    private Map<String, Project> data;

    public Map<String, Project> getData() {
      return data;
    }
  }
}
