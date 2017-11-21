package mmm444.ijphab;

import com.intellij.openapi.util.text.StringUtil;
import com.intellij.tasks.CustomTaskState;
import com.intellij.tasks.Task;
import com.intellij.tasks.TaskRepositoryType;
import com.intellij.tasks.impl.BaseRepository;
import com.intellij.tasks.impl.RequestFailedException;
import com.intellij.tasks.impl.httpclient.NewBaseRepositoryImpl;
import com.intellij.tasks.impl.httpclient.TaskResponseUtil;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.xmlb.annotations.Tag;
import mmm444.ijphab.model.*;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.message.BasicNameValuePair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Tag("Phabricator")
public class PhabricatorRepository extends NewBaseRepositoryImpl {
  private static final int MIN_PHAB_QUERY_LEN = 3;

  private static final Pattern PROJECT_NAME_SEP = Pattern.compile("\\s*,\\s*");
  private static final Pattern TASK_ID = Pattern.compile("T(\\d+)");

  private final List<String> myIconProjectPhids = new ArrayList<>();
  @SuppressWarnings("FieldAccessedSynchronizedAndUnsynchronized")
  private String myIconProjects = "";

  private transient Map<String, Project> myProjectCache = null;

  private transient StatusesResponse myStatusesResponse = null;

  @SuppressWarnings("unused")
  public PhabricatorRepository() {
  }

  public PhabricatorRepository(TaskRepositoryType type) {
    super(type);
  }

  public PhabricatorRepository(PhabricatorRepository other) {
    super(other);
    myIconProjects = other.myIconProjects;
  }

  @Override
  public boolean isConfigured() {
    return super.isConfigured() && StringUtil.isNotEmpty(myPassword);
  }

  public void setIconProjects(String iconProjects) {
    this.myIconProjects = iconProjects;
    updateIconProjectPhids();
  }

  @Tag("iconProjects")
  public String getIconProjects() {
    return myIconProjects;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;

    PhabricatorRepository that = (PhabricatorRepository)o;

    return myIconProjects.equals(that.myIconProjects);
  }

  @Nullable
  @Override
  public PhabricatorTask findTask(@NotNull String id) throws Exception {
    Params params = new Params()
      .add("constraints[ids][0]", id)
      .add("attachments[projects]", "1")
      .add("limit", "1");
    SearchResponse res = apiCall("maniphest.search", SearchResponse.class, params);
    List<SearchResponse.TaskData> data = res.getData();
    return !data.isEmpty() ? new PhabricatorTask(data.iterator().next(), this) : null;
  }

  @Override
  public PhabricatorTask[] getIssues(@Nullable String query, int offset, int limit, boolean withClosed) throws Exception {
    Params params = new Params()
      .add("queryKey", withClosed ? "all" : "open")
      .add("attachments[projects]", "1")
      .add("limit", String.valueOf(limit));
    // TODO:
    //  .add("offset", String.valueOf(offset));
    if (offset > 0) {
      return new PhabricatorTask[]{};
    }
    if (query != null) {
      Matcher m = TASK_ID.matcher(query);
      if (m.matches()) {
        params.add("constraints[ids][0]", m.group(1));
      } else if (query.length() >= MIN_PHAB_QUERY_LEN) {
        params.add("constraints[query]", query);
      } else {
        return new PhabricatorTask[]{};
      }
    }

    SearchResponse res = apiCall("maniphest.search", SearchResponse.class, params);
    return ContainerUtil
      .map2Array(res.getData(), PhabricatorTask.class, t -> new PhabricatorTask(t, this));
  }

  @NotNull
  @Override
  public BaseRepository clone() {
    return new PhabricatorRepository(this);
  }

  @NotNull
  @Override
  public String getRestApiPathPrefix() {
    return "/api";
  }

  @Override
  protected int getFeatures() {
    return NATIVE_SEARCH | STATE_UPDATING;
  }

  @Nullable
  @Override
  public CancellableConnection createCancellableConnection() {
    final HttpRequestBase request = apiRequest("user.whoami", Collections.emptyList());
    return new CancellableConnection() {
      @Override
      protected void doTest() throws Exception {
        try {
          HttpResponse response = getHttpClient().execute(request);
          WhoamiResponse res = deserializeApiResponse(response, WhoamiResponse.class);
          res.checkOk();
        }
        catch (IOException e) {
          if (!request.isAborted()) {
            throw e;
          }
        }
      }

      @Override
      public void cancel() {
        request.abort();
      }
    };
  }

  private synchronized StatusesResponse getStatusesResponse() throws Exception {
    if (myStatusesResponse == null) {
      myStatusesResponse = apiCall("maniphest.status.search", StatusesResponse.class);
    }
    return myStatusesResponse;
  }

  boolean isTaskClosed(String statusValue) {
    boolean closed = false;
    try {
      StatusesResponse res = getStatusesResponse();
      for (StatusesResponse.Status status : res.getData()) {
        if (statusValue != null && statusValue.equals(status.getValue())) {
          closed = status.isClosed();
          break;
        }
      }
    } catch (Exception ignored) {
    }
    return closed;
  }

  @NotNull
  @Override
  public Set<CustomTaskState> getAvailableTaskStates(@NotNull Task task) throws Exception {
    StatusesResponse res = getStatusesResponse();
    String currentState;
    if (task instanceof PhabricatorTask) {
      currentState = ((PhabricatorTask)task).getStatusId();
    }
    else {
      PhabricatorTask phTask = findTask(task.getId());
      currentState = phTask != null ? phTask.getStatusId() : "";
    }
    List<CustomTaskState> states = new ArrayList<>();
    boolean nextToTop = false;
    // skip current state and keep the one after the skipped the first
    for (StatusesResponse.Status status : res.getData()) {
      if (!status.getValue().equals(currentState)) {
        CustomTaskState state = new CustomTaskState(status.getValue(), status.getName());
        if (nextToTop) {
          states.add(0, state);
          nextToTop = false;
        }
        else {
          states.add(state);
        }
      }
      else {
        nextToTop = true;
      }
    }
    return new LinkedHashSet<>(states);
  }

  @Override
  public void setTaskState(@NotNull Task task, @NotNull CustomTaskState state) throws Exception {
    Params params = new Params()
      .add("objectIdentifier", task.getId())
      .add("transactions[0][type]", "status")
      .add("transactions[0][value]", state.getId());
    apiCall("maniphest.edit", MethodResponse.class, params);
  }

  synchronized Icon getTaskIcon(@NotNull List<String> taskProjectPhids) {
    initProjectCache();

    String iconProjectPhid = null;
    for (String priorityPhid : myIconProjectPhids) {
      if (taskProjectPhids.contains(priorityPhid)) {
        iconProjectPhid = priorityPhid;
      }
    }
    if (iconProjectPhid == null && !taskProjectPhids.isEmpty()) {
      iconProjectPhid = taskProjectPhids.get(0);
    }

    String icon = "";
    String color = "grey";
    if (iconProjectPhid != null) {
      Project iconProject = myProjectCache.get(iconProjectPhid);
      if (iconProject != null && iconProject.getIcon() != null) {
        icon = iconProject.getIcon();
        color = iconProject.getColor();
      }
    }

    return PhabricatorIconBuilder.getIcon(icon, color, 16);
  }

  private synchronized void initProjectCache() {
    if (myProjectCache != null || !isConfigured()) {
      return;
    }
    try {
      myProjectCache = new HashMap<>();
      ProjectResponse res;
      String after = null;
      do {
        Params params = new Params();
        if (after != null) {
          params.add("after", after);
        }
        res = apiCall("project.search", ProjectResponse.class, params);
        for (Project project : res.getResult().getData()) {
          myProjectCache.put(project.getPhid(), project);
        }
        after = res.getResult().getCursor().getAfter();
      } while (after != null);
    }
    catch (IOException | RequestFailedException ignored) {
    }
  }

  private synchronized void updateIconProjectPhids() {
    myIconProjectPhids.clear();
    initProjectCache();
    if (myProjectCache != null) {
      Map<String, String> nameToPhid = new HashMap<>();
      for (Project project : myProjectCache.values()) {
        nameToPhid.put(project.getName(), project.getPhid());
      }
      for (String name : PROJECT_NAME_SEP.split(myIconProjects)) {
        String phid = nameToPhid.get(name);
        if (phid != null) {
          myIconProjectPhids.add(phid);
        }
      }
    }
  }

  private HttpRequestBase apiRequest(String apiMethod, List<NameValuePair> params) {
    HttpPost post = new HttpPost(getRestApiUrl(apiMethod));
    post.setConfig(RequestConfig.custom().setCookieSpec(CookieSpecs.IGNORE_COOKIES).build());

    List<NameValuePair> formFields = new ArrayList<>();
    formFields.add(new BasicNameValuePair("api.token", myPassword));
    formFields.addAll(params);

    try {
      post.setEntity(new UrlEncodedFormEntity(formFields, "UTF-8"));
    }
    catch (UnsupportedEncodingException ignored) {
      // cannot happen
    }
    return post;
  }

  private static <T extends MethodResponse> T deserializeApiResponse(HttpResponse res, Class<T> cls) throws IOException {
    TaskResponseUtil.GsonSingleObjectDeserializer<T> responseHandler =
      new TaskResponseUtil.GsonSingleObjectDeserializer<>(PhabricatorUtil.GSON, cls);
    return responseHandler.handleResponse(res);
  }

  private <T extends MethodResponse> T apiCall(String apiMethod, Class<T> cls, Params params) throws IOException {
    HttpResponse response = getHttpClient().execute(apiRequest(apiMethod, params.getList()));
    T res = deserializeApiResponse(response, cls);
    res.checkOk();
    return res;
  }

  private <T extends MethodResponse> T apiCall(String apiMethod, Class<T> cls) throws IOException {
    return apiCall(apiMethod, cls, new Params());
  }

  public synchronized List<String> getProjectNames() {
    initProjectCache();
    List<String> res;
    if (myProjectCache != null) {
      res = new ArrayList<>(myProjectCache.size());
      for (Project project : myProjectCache.values()) {
        res.add(project.getName());
      }
      Collections.sort(res);
    }
    else {
      res = Collections.emptyList();
    }
    return res;
  }

  private static class Params {
    private final List<NameValuePair> list = new ArrayList<>();

    Params add(String name, String value) {
      list.add(new BasicNameValuePair(name, value));
      return this;
    }

    public List<NameValuePair> getList() {
      return list;
    }
  }
}
