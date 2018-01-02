package mmm444.ijphab.model;

import com.google.gson.annotations.SerializedName;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.tasks.impl.RequestFailedException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Base class for Conduit responses.
 */
@SuppressWarnings({"CanBeFinal", "unused"})
public abstract class MethodResponse {
    @SerializedName("error_code")
    private String errorCode;
    @SerializedName("error_info")
    private String errorInfo;

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    /**
     * Validate the data returned from the server. Return the list of found validation issues.
     */
    @Nonnull
    abstract List<String> validate();

    public void checkOk() {
        if (StringUtil.isNotEmpty(errorInfo)) {
            throw new RequestFailedException(errorInfo);
        }

        List<String> validationErrs = validate();
        if (!validationErrs.isEmpty()) {
            throw new RequestFailedException("Invalid data received: " + StringUtil.join(validationErrs, "\n"));
        }
    }
}
