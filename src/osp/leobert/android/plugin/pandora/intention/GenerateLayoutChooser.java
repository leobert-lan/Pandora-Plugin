package osp.leobert.android.plugin.pandora.intention;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;

/**
 * <p><b>Package:</b> osp.leobert.android.plugin.pandora.intention </p>
 * <p><b>Project:</b> Pandora-Plugin </p>
 * <p><b>Classname:</b> GenerateLayoutChooser </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2018/10/30.
 */
public class GenerateLayoutChooser extends JavaFileIntentionChooser {
    public static final JavaFileIntentionChooser INSTANCE = new GenerateLayoutChooser();

    @Override
    public boolean isAvailable(@NotNull PsiElement element) {
        if (isPositionOfInterfaceDeclaration(element)) {
            PsiClass clazz = PsiTreeUtil.getParentOfType(element, PsiClass.class);
            if (null != clazz) {
                return !isTargetPresentInXml(clazz);
            }
        }
        return false;
    }
}
