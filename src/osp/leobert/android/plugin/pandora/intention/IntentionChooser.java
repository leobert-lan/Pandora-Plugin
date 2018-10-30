package osp.leobert.android.plugin.pandora.intention;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;

import org.jetbrains.annotations.NotNull;

/**
 * <p><b>Package:</b> osp.leobert.android.plugin.pandora.intention </p>
 * <p><b>Project:</b> Pandora-Plugin </p>
 * <p><b>Classname:</b> IntentionChooser </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2018/10/30.
 */
public interface IntentionChooser {
    boolean isAvailable(@NotNull Project project, Editor editor, PsiFile file);

}
