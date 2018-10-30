package osp.leobert.android.plugin.pandora.util;

import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.ide.fileTemplates.FileTemplateUtil;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Properties;


public final class XmlLayoutUtils {

    private XmlLayoutUtils() {
        throw new UnsupportedOperationException();
    }


    public static PsiElement createLayoutFileFromFileTemplate(@NotNull String fileTemplateName,
                                                              @NotNull String fileName,
                                                              @NotNull PsiDirectory directory,
                                                              @Nullable Properties pops) throws Exception {
        FileTemplate fileTemplate = FileTemplateManager.getDefaultInstance().getInternalTemplate(fileTemplateName);
        return FileTemplateUtil.createFromTemplate(fileTemplate, fileName, pops, directory);
    }
}
