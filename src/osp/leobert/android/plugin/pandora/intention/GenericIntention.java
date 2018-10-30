package osp.leobert.android.plugin.pandora.intention;


import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;

import org.jetbrains.annotations.NotNull;

/**
 * <p><b>Package:</b> osp.leobert.android.plugin.pandora.intention </p>
 * <p><b>Project:</b> Pandora-Plugin </p>
 * <p><b>Classname:</b> GenericIntention </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2018/10/30.
 */
public abstract class GenericIntention implements IntentionAction {

    protected IntentionChooser chooser;

    public GenericIntention(@NotNull IntentionChooser chooser) {
        this.chooser = chooser;
    }

    @NotNull
    @Override
    public String getFamilyName() {
        return getText();
    }

    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile file) {
        return chooser.isAvailable(project, editor, file);
    }

    @Override
    public boolean startInWriteAction() {
        return true;
    }

}
