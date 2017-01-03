package mmm444.ijphab.model;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"CanBeFinal", "unused"})
public class QueryResponse extends MethodResponse {
  private Map<String, TaskData> result;

  public Map<String, TaskData> getResult() {
    return result;
  }

  @SuppressWarnings({"CanBeFinal", "unused"})
  public static class TaskData {
    private String id;
    private String phid;
    private String authorPHID;
    private String ownerPHID;
    private String status;
    private String statusName;
    private boolean isClosed;
    private String priority;
    private String priorityColor;
    private String title;
    private String description;
    private List<String> projectPHIDs = null;
    private String uri;
    private String objectName;
    private Date dateCreated;
    private Date dateModified;

    public String getId() {
      return id;
    }

    public String getPhid() {
      return phid;
    }

    public String getAuthorPHID() {
      return authorPHID;
    }

    public String getOwnerPHID() {
      return ownerPHID;
    }

    public String getStatus() {
      return status;
    }

    public String getStatusName() {
      return statusName;
    }

    public boolean isClosed() {
      return isClosed;
    }

    public String getPriority() {
      return priority;
    }

    public String getPriorityColor() {
      return priorityColor;
    }

    public String getTitle() {
      return title;
    }

    public String getDescription() {
      return description;
    }

    public List<String> getProjectPHIDs() {
      return projectPHIDs == null ? Collections.emptyList() : projectPHIDs;
    }

    public String getUri() {
      return uri;
    }

    public String getObjectName() {
      return objectName;
    }

    public Date getDateCreated() {
      return dateCreated;
    }

    public Date getDateModified() {
      return dateModified;
    }
  }
}
