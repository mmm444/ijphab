package mmm444.ijphab.model;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
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

  @Nonnull
  @Override
  List<String> validate() {
    if (result == null) {
      return Collections.singletonList("null result in the response");
    }
    if (result.data == null) {
      return Collections.singletonList("null result.data in the response");
    }
    List<String> errs = new ArrayList<>();
    for (Project project : result.data) {
      project.validate(errs);
    }
    return errs;
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
