package mmm444.ijphab.model;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"CanBeFinal", "unused"})
public class WhoamiResponse extends MethodResponse {
  private Map<String, Object> result;

  public Map<String, Object> getResult() {
    return result;
  }

  @Nonnull
  @Override
  List<String> validate() {
    // always valid since the result is not used outside of tests
    return Collections.emptyList();
  }
}
