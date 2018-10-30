package osp.leobert.android.plugin.pandora.intention;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;
import osp.leobert.android.plugin.pandora.util.JavaUtils;

public abstract class JavaFileIntentionChooser implements IntentionChooser {

    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile file) {
        if (!(file instanceof PsiJavaFile))
            return false;
        PsiElement element = file.findElementAt(editor.getCaretModel().getOffset());
        return null != element && isAvailable(element);
    }

    public abstract boolean isAvailable(@NotNull PsiElement element);

    public boolean isPositionOfParameterDeclaration(@NotNull PsiElement element) {
        return element.getParent() instanceof PsiParameter;
    }

    public boolean isPositionOfMethodDeclaration(@NotNull PsiElement element) {
        return element.getParent() instanceof PsiMethod;
    }

    public boolean isPositionOfInterfaceDeclaration(@NotNull PsiElement element) {
        return element.getParent() instanceof PsiClass;
    }

    public boolean isPositionOfViewHolderDeclaration(@NotNull PsiElement element) {
        boolean a = element.getParent() instanceof PsiClass;
        if (!a)
            return false;
        PsiClass psiClass = (PsiClass) element.getParent();
//        if (psiClass.isInterface() || psiClass.isAnnotationType() || psiClass.isEnum())
//            return false;
        String name = psiClass.getName();
        if (name != null) {
            return name.endsWith("VH2");
        }
        return false;
    }

    public boolean isTargetPresentInXml(@NotNull PsiElement element) {
        return false;
//    return JavaService.getInstance(element.getProject()).findWithFindFirstProcessor(element).isPresent();
    }

}
