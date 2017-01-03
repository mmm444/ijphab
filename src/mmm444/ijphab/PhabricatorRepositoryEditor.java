package mmm444.ijphab;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.tasks.config.BaseRepositoryEditor;
import com.intellij.ui.TextFieldWithAutoCompletion;
import com.intellij.ui.components.JBLabel;
import com.intellij.util.Consumer;
import com.intellij.util.ui.FormBuilder;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Collections;

public class PhabricatorRepositoryEditor extends BaseRepositoryEditor<PhabricatorRepository> {
  private JBLabel myIconProjectsLabel;
  private TextFieldWithAutoCompletion<String> myIconProjects;

  public PhabricatorRepositoryEditor(Project project,
                                     PhabricatorRepository repository,
                                     Consumer<PhabricatorRepository> changeListener) {
    super(project, repository, changeListener);
    myUsernameLabel.setVisible(false);
    myUserNameText.setVisible(false);
    myPasswordLabel.setText("Token:");

    installListener(myIconProjects);

    myTestButton.setEnabled(myRepository.isConfigured());
    if (myRepository.isConfigured()) {
      ApplicationManager.getApplication().executeOnPooledThread(this::installProjectCompletion);
    }
  }

  @Override
  public void apply() {
    super.apply();
    myRepository.setIconProjects(myIconProjects.getText());
    myTestButton.setEnabled(myRepository.isConfigured());
  }

  @Nullable
  @Override
  protected JComponent createCustomPanel() {
    myIconProjectsLabel = new JBLabel("Icon Projects:", SwingConstants.RIGHT);
    myIconProjects = TextFieldWithAutoCompletion.create(
      myProject,
      Collections.emptyList(),
      true,
      myRepository.getIconProjects()
    );
    JBLabel descLabel = new JBLabel();
    descLabel.setCopyable(true);
    descLabel.setText("Only one icon is shown for each task. " +
                      "This icon is extracted from the projects the task belongs to.<br>" +
                      "You can specify the projects whose icons will be used first. " +
                      "Separate multiple projects with commas.");
    descLabel.setComponentStyle(UIUtil.ComponentStyle.SMALL);
    return FormBuilder.createFormBuilder()
      .addLabeledComponent(myIconProjectsLabel, myIconProjects)
      .addComponentToRightColumn(descLabel)
      .getPanel();
  }

  @Override
  protected void afterTestConnection(boolean connectionSuccessful) {
    super.afterTestConnection(connectionSuccessful);

    if (connectionSuccessful) {
      installProjectCompletion();
    }
  }

  private void installProjectCompletion() {
    myIconProjects.setVariants(myRepository.getProjectNames());
  }

  @Override
  public void setAnchor(@Nullable JComponent anchor) {
    super.setAnchor(anchor);
    myIconProjectsLabel.setAnchor(anchor);
  }
}
