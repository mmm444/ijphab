package mmm444.ijphab.model;

import java.util.Map;

@SuppressWarnings({"CanBeFinal", "unused"})
public class StatusesResponse extends MethodResponse {
  private Result result;

  public Result getResult() {
    return result;
  }

  @SuppressWarnings({"CanBeFinal", "unused"})
  public static class Result {
    private Map<String, String> statusMap;

    public Map<String, String> getStatusMap() {
      return statusMap;
    }
  }
}
