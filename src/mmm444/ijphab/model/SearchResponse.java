package mmm444.ijphab.model;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@SuppressWarnings({"CanBeFinal", "unused"})
public class SearchResponse extends MethodResponse {
  private Result result;

  public List<TaskData> getData() {
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
    for (TaskData taskData : result.data) {
      taskData.validate(errs);
    }
    return errs;
  }

  public static class Result {
    List<TaskData> data;
  }

  @SuppressWarnings({"CanBeFinal", "unused"})
  public static class TaskData {
    private String id;
    private String phid;
    private Fields fields;
    private Attachments attachments;

    public String getId() {
      return id;
    }

    public String getPhid() {
      return phid;
    }

    public String getAuthorPHID() {
      return fields.authorPHID;
    }

    public String getOwnerPHID() {
      return fields.ownerPHID;
    }

    public String getStatus() {
      return fields.status.value;
    }

    public String getStatusName() {
      return fields.status.name;
    }

    public String getPriority() {
      return fields.priority.name;
    }

    public String getPriorityColor() {
      return fields.priority.color;
    }

    public String getTitle() {
      return fields.name;
    }

    public String getDescription() {
      return fields.description == null ? "" : fields.description.raw;
    }

    public List<String> getProjectPHIDs() {

      return attachments.projects == null || attachments.projects.projectPHIDs == null
              ? Collections.emptyList() : attachments.projects.projectPHIDs;
    }

    public Date getDateCreated() {
      return fields.dateCreated;
    }

    public Date getDateModified() {
      return fields.dateModified;
    }

    private void validate(List<String> errs) {
      if (fields == null) {
        errs.add("null fields for task " + id);
      } else {
        if (fields.status == null) {
          errs.add("null status for task " + id);
        }
        if (fields.priority == null) {
          errs.add("null priority for task" + id);
        }
      }
      if (attachments == null) {
        errs.add("null attachments for task " + id);
      }
    }
  }

  public static class Fields {
    private String authorPHID;
    private String ownerPHID;

    private Status status;
    private Priority priority;

    private String name;
    private Description description;
    private Date dateCreated;
    private Date dateModified;
  }

  public static class Status {
    private String name;
    private String value;
  }

  public static class Priority {
    private String name;
    private String color;
  }

  public static class Description {
    private String raw;
  }

  public static class Attachments {
    private Projects projects;
  }

  public static class Projects {
    List<String> projectPHIDs;
  }
}
