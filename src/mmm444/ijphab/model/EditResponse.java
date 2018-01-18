package mmm444.ijphab.model;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

/**
 * Response for the edit endpoints.
 * <p>
 * See <a href="https://secure.phabricator.com/book/phabricator/article/conduit_edit/">Phabricator documentation</a>.
 */
public class EditResponse extends MethodResponse {
  @Nonnull
  @Override
  List<String> validate() {
    return Collections.emptyList();
  }
}
