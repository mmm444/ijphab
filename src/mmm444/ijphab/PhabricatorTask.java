package mmm444.ijphab;

import com.intellij.tasks.Comment;
import com.intellij.tasks.Task;
import com.intellij.tasks.TaskRepository;
import com.intellij.tasks.TaskType;
import mmm444.ijphab.model.QueryResponse;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Date;

public class PhabricatorTask extends Task {
  private final PhabricatorRepository myRepository;
  private final QueryResponse.TaskData myData;

  public PhabricatorTask(QueryResponse.TaskData data, PhabricatorRepository repository) {
    this.myData = data;
    this.myRepository = repository;
  }

  @NotNull
  @Override
  public String getId() {
    return myData.getId();
  }

  @NotNull
  @Override
  public String getPresentableId() {
    return myData.getObjectName();
  }

  @NotNull
  @Override
  public String getSummary() {
    return myData.getTitle();
  }

  @Nullable
  @Override
  public String getDescription() {
    return myData.getDescription();
  }

  @NotNull
  @Override
  public Comment[] getComments() {
    return new Comment[0];
  }

  @NotNull
  @Override
  public Icon getIcon() {
    return myRepository.getTaskIcon(myData.getProjectPHIDs());
  }

  @NotNull
  @Override
  public TaskType getType() {
    return TaskType.OTHER;
  }

  @Nullable
  @Override
  public Date getUpdated() {
    return myData.getDateModified();
  }

  @Nullable
  @Override
  public Date getCreated() {
    return myData.getDateCreated();
  }

  @Override
  public boolean isClosed() {
    return myData.isClosed();
  }

  @Override
  public boolean isIssue() {
    return true;
  }

  @Nullable
  @Override
  public String getIssueUrl() {
    return myData.getUri();
  }

  @Nullable
  @Override
  public String getProject() {
    return null;
  }

  @Nullable
  @Override
  public TaskRepository getRepository() {
    return myRepository;
  }

  @NotNull
  String getStatusId() {
    return myData.getStatus();
  }
}
