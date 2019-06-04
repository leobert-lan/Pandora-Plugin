package osp.leobert.android.plugin.pandora.intention;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.util.PsiTreeUtil;

import org.jetbrains.annotations.NotNull;

public abstract class KtFileIntentionChooser implements IntentionChooser {

    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile file) {
//        if (!(file instanceof PsiJavaFile)) //暂时不知道ktfile是啥
//            return false;
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

    public boolean isPositionOfKtVHCreatorDeclaration(@NotNull PsiElement element) {
        boolean a = element instanceof LeafPsiElement;
        if (!a)
            return false;

        PsiFile ktFile = PsiTreeUtil.getParentOfType(element, PsiFile.class);
        if (ktFile != null) {
            String name = ktFile.getName();
            return name.contains("Widget");
        }
        return false;


//        LeafPsiElement psiFile = (LeafPsiElement) element;
//        String name = psiFile.getParent().getName();
//            throw new NullPointerException(name);
//        try {
////            return name.contains("VHCreator");
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
    }

    public boolean isTargetPresentInXml(@NotNull PsiElement element) {
        return false;
//    return JavaService.getInstance(element.getProject()).findWithFindFirstProcessor(element).isPresent();
    }

}
