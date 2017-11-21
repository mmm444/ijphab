package mmm444.ijphab.model;

import java.util.List;

@SuppressWarnings({"CanBeFinal", "unused"})
public class StatusesResponse extends MethodResponse {
  private Result result;

  public List<Status> getData() {
    return result.data;
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

    public String getName() {
      return name;
    }

    public String getValue() {
      return value;
    }

    public boolean isClosed() {
      return closed;
    }

    public String getSpecial() {
      return special;
    }
  }
}
