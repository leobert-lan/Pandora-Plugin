package osp.leobert.android.plugin.pandora;

import com.google.common.base.CaseFormat;
import com.intellij.ide.IdeBundle;
import com.intellij.ide.actions.CreateFileFromTemplateDialog;
import com.intellij.ide.actions.JavaCreateTemplateInPackageAction;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.InputValidatorEx;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.util.IncorrectOperationException;
import com.intellij.util.PlatformIcons;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static com.google.common.base.CaseFormat.LOWER_CAMEL;
import static com.google.common.base.CaseFormat.LOWER_UNDERSCORE;

/**
 * <p><b>Package:</b> osp.leobert.android.plugin.pandora </p>
 * <p><b>Project:</b> Pandora-Plugin </p>
 * <p><b>Classname:</b> CreateVOFilesAction </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2018/10/29.
 */
public class CreateVHVOFilesAction extends JavaCreateTemplateInPackageAction<PsiClass>
        implements DumbAware {

    private static final String VO_TEMPLATE_NAME = "VO";
    private static final String VIEW_HOLDER_TEMPLATE_NAME = "VH";

    public static final String VO_AND_VIEW_HOLDER = "VO & ViewHolder";
    public static final String ONLY_VIEW_HOLDER = "Only ViewHolder";


    public CreateVHVOFilesAction() {
        super("", IdeBundle.message("action.create.new.class.description"), PlatformIcons.CLASS_ICON, true);
    }


    @Override
    protected void buildDialog(final Project project, PsiDirectory directory, CreateFileFromTemplateDialog.Builder builder) {
        builder.setTitle("Create VO and ViewHolder")
                .addKind(VO_AND_VIEW_HOLDER, PlatformIcons.CLASS_ICON, VO_AND_VIEW_HOLDER)
                .addKind(ONLY_VIEW_HOLDER, PlatformIcons.CLASS_ICON, ONLY_VIEW_HOLDER);

        builder.setValidator(new InputValidatorEx() {
            @Override
            public String getErrorText(String inputString) {
                if (inputString.length() > 0 &&
                        !PsiNameHelper.getInstance(project).isQualifiedName(inputString)) {
                    return "This is not a valid Java qualified name";
                }
                return null;
            }


            @Override
            public boolean checkInput(String inputString) {
                return true;
            }


            @Override
            public boolean canClose(String inputString) {
                return !StringUtil.isEmptyOrSpaces(inputString) &&
                        getErrorText(inputString) == null;
            }
        });
    }


    @Override
    protected String removeExtension(String templateName, String className) {
        return StringUtil.trimEnd(className, ".java");
    }


    @Override
    protected String getErrorTitle() {
        return IdeBundle.message("title.cannot.create.class");
    }


    @Override
    @SuppressWarnings("ConstantConditions")
    protected String getActionName(PsiDirectory directory, String newName, String templateName) {
        return IdeBundle.message("progress.creating.class",
                StringUtil.getQualifiedName(
                        JavaDirectoryService.getInstance().getPackage(directory).getQualifiedName(),
                        newName
                )
        );
    }


    @Override
    protected final PsiClass doCreate(PsiDirectory dir, String className, String templateName)
            throws IncorrectOperationException {
        PsiClass result = createClass(dir, className, VIEW_HOLDER_TEMPLATE_NAME);
        if (templateName.equals(VO_AND_VIEW_HOLDER)) {
            createClass(dir, className, VO_TEMPLATE_NAME);
        }

        onProcessItemViewBinder(dir, className, result);
        return result;
    }


    private void onProcessItemViewBinder(final PsiDirectory dir, final String typeName, final PsiClass itemClass) {
        PsiFile file = itemClass.getContainingFile();
        final PsiDocumentManager manager = PsiDocumentManager.getInstance(itemClass.getProject());
        final Document document = manager.getDocument(file);
        if (document == null) {
            return;
        }

        new WriteCommandAction.Simple(itemClass.getProject()) {
            @Override
            protected void run() throws Throwable {
                manager.doPostponedOperationsAndUnblockDocument(document);
                document.setText(document.getText()
                        .replace("VHVO_PREFIXCLASS", typeName)
                        .replace("VHVO_PREFIXLOWER_NAME", CaseFormat.UPPER_CAMEL.to(LOWER_UNDERSCORE, typeName))
                        .replace("VHVO_PREFIXNAME", CaseFormat.UPPER_CAMEL.to(LOWER_CAMEL, typeName)));
                CodeStyleManager.getInstance(itemClass.getProject()).reformat(itemClass);
            }
        }.execute();
    }


    @Override
    protected PsiElement getNavigationElement(@NotNull PsiClass createdElement) {
        return createdElement.getLBrace();
    }


    @Override
    protected void postProcess(PsiClass createdElement, String templateName, Map<String, String> customProperties) {
        super.postProcess(createdElement, templateName, customProperties);

        moveCaretAfterNameIdentifier(createdElement);
    }


    private PsiClass createClass(@NotNull PsiDirectory dir, @NotNull String className, @NotNull String templateName) {
        return JavaDirectoryService.getInstance().createClass(dir, className, templateName);
    }
}
