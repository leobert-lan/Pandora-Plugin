package osp.leobert.android.plugin.pandora.util;

import com.google.common.base.CaseFormat;
import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.ide.fileTemplates.FileTemplateUtil;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.base.CaseFormat.LOWER_UNDERSCORE;


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

    public static String createLayoutFileName(PsiClass clazz) {
        try {
            String clzName = clazz.getName();
            Pattern pattern = Pattern.compile(".*VH2");
            Matcher matcher = pattern.matcher(clzName);

            return "app_vh_" + CaseFormat.UPPER_CAMEL.to(LOWER_UNDERSCORE, matcher.group());
        } catch (Exception e) {
            e.printStackTrace();
            return "app_vh_" + System.currentTimeMillis();
        }
    }

}
