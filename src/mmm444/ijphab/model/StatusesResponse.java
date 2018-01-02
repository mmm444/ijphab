package mmm444.ijphab.model;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"CanBeFinal", "unused"})
public class StatusesResponse extends MethodResponse {
  private Result result;

  public List<Status> getData() {
    return result.data;
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
    for (Status status : result.data) {
      status.validate(errs);
    }
    return errs;
  }

  @SuppressWarnings({"CanBeFinal", "unused"})
  public static class Result {
    private List<Status> data;
  }

  public static class Status {
    private String name;
    private String value;
    private boolean closed;
    private String special;

    @Nonnull
    public String getName() {
      return name;
    }

    @Nonnull
    public String getValue() {
      return value;
    }

    public boolean isClosed() {
      return closed;
    }

    public String getSpecial() {
      return special;
    }

    public void validate(List<String> errs) {
      if (name == null) {
        errs.add("null name for a status");
      }
      if (value == null) {
        errs.add("null value for status " + name);
      }
    }
  }
}
