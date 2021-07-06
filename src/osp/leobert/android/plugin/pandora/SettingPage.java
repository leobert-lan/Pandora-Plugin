package osp.leobert.android.plugin.pandora;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.util.NlsContexts;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * <p><b>Package:</b> osp.leobert.android.plugin.pandora </p>
 * <p><b>Project:</b> Pandora-Plugin2 </p>
 * <p><b>Classname:</b> SettingPage </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2021/7/6.
 */
public class SettingPage implements Configurable {
    @Override
    public @NlsContexts.ConfigurableName String getDisplayName() {
        return "Pandora";
    }

    @Override
    public @Nullable JComponent createComponent() {
        return null;
    }

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    public void apply() throws ConfigurationException {

    }
}
