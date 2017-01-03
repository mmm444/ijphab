package mmm444.ijphab.model;

import com.google.gson.annotations.SerializedName;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.tasks.impl.RequestFailedException;

/**
 * Base class for Conduit responses.
 */
@SuppressWarnings({"CanBeFinal", "unused"})
public class MethodResponse {
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

    public void checkOk() {
        if (StringUtil.isNotEmpty(errorInfo)) {
            throw new RequestFailedException(errorInfo);
        }
    }
}
