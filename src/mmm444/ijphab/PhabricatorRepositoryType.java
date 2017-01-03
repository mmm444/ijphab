package mmm444.ijphab;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.IconLoader;
import com.intellij.tasks.TaskRepository;
import com.intellij.tasks.config.TaskRepositoryEditor;
import com.intellij.tasks.impl.BaseRepositoryType;
import com.intellij.util.Consumer;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class PhabricatorRepositoryType extends BaseRepositoryType<PhabricatorRepository> {
  private static final Icon ICON = IconLoader.getIcon("/icons/phab.png", PhabricatorRepositoryType.class);

  @NotNull
  @Override
  public String getName() {
    return "Phabricator";
  }

  @NotNull
  @Override
  public Icon getIcon() {
    return ICON;
  }

  @NotNull
  @Override
  public TaskRepository createRepository() {
    return new PhabricatorRepository(this);
  }

  @NotNull
  @Override
  public TaskRepositoryEditor createEditor(PhabricatorRepository repository,
                                           Project project,
                                           Consumer<PhabricatorRepository> changeListener) {
    return new PhabricatorRepositoryEditor(project, repository, changeListener);
  }

  @Override
  public Class<PhabricatorRepository> getRepositoryClass() {
    return PhabricatorRepository.class;
  }
}
