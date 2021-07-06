package osp.leobert.android.plugin.pandora.persist;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Logger;

/**
 * <p><b>Package:</b> osp.leobert.android.plugin.pandora.persist </p>
 * <p><b>Project:</b> Pandora-Plugin2 </p>
 * <p><b>Classname:</b> PandoraPersist </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2021/7/6.
 */
@State(name = "PandoraPersist", storages = {@Storage(value = "Pandora.xml")})
public class PandoraPersist implements PersistentStateComponent<PandoraPersist.State> {

    public static PandoraPersist getInstance(Project project) {
        return project.getComponent(PandoraPersist.class);
    }

    public static PandoraPersist getInstance() {
        return ApplicationManager.getApplication().getComponent(PandoraPersist.class);
    }

    private State state;

    @Override
    public @Nullable PandoraPersist.State getState() {
        return state;
    }

    @Override
    public void loadState(@NotNull State state) {
        this.state = state;
        Logger.getGlobal().fine("loadState:" + state);
    }

    public static class State {

        public State() {
        }

        public String rPackage;

        public String getrPackage() {
            return rPackage;
        }

        public void setrPackage(String rPackage) {
            this.rPackage = rPackage;
        }

        @Override
        public String toString() {
            return "State{" +
                    "rPackage='" + rPackage + '\'' +
                    '}';
        }
    }


}
