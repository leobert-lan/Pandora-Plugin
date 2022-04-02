package osp.leobert.android.plugin.pandora.action;

import com.google.common.base.CaseFormat;
import com.intellij.ide.IdeBundle;
import com.intellij.ide.actions.CreateFileFromTemplateDialog;
import com.intellij.ide.actions.JavaCreateTemplateInPackageAction;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.InputValidatorEx;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.JavaDirectoryService;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiNameHelper;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.util.IncorrectOperationException;
import com.intellij.util.PlatformIcons;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

import osp.leobert.android.plugin.pandora.util.Properties;
import osp.leobert.android.plugin.pandora.util.Utils;

import static com.google.common.base.CaseFormat.LOWER_CAMEL;
import static com.google.common.base.CaseFormat.LOWER_UNDERSCORE;
import static osp.leobert.android.plugin.pandora.util.Utils.CONF_BASE_VH_NAME;
import static osp.leobert.android.plugin.pandora.util.Utils.CONF_BASE_VH_PACKAGE;
import static osp.leobert.android.plugin.pandora.util.Utils.CONF_R_PACKAGE;

/**
 * <p><b>Package:</b> osp.leobert.android.plugin.pandora </p>
 * <p><b>Project:</b> Pandora-Plugin </p>
 * <p><b>Classname:</b> CreateVOFilesAction </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2018/10/29.
 */
public class CreateJavaVHVOFilesAction extends JavaCreateTemplateInPackageAction<PsiClass>
        implements DumbAware {

    private static final String VO_TEMPLATE_NAME = "VO";
    private static final String VIEW_HOLDER_TEMPLATE_NAME = "VH";

    private static final String VO_AND_VIEW_HOLDER = "VO & ViewHolder";
    private static final String ONLY_VIEW_HOLDER = "Only ViewHolder";

    public CreateJavaVHVOFilesAction() {
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

    private VirtualFile getModuleDir(PsiDirectory directory) {
        return Utils.getModuleDir(directory);
    }


    private Properties parseConfig(VirtualFile moduleDir) {
        return Utils.parseConfig(moduleDir);
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
        initConfig(dir);

        if (templateName.equals(VO_AND_VIEW_HOLDER)) {
            PsiClass voClass = createClass(dir, className, VO_TEMPLATE_NAME);
            injectConfigIntoTemplateClass(dir, className, voClass);
        }


        injectConfigIntoTemplateClass(dir, className, result);
        return result;
    }

    private void initConfig(final PsiDirectory dir) {
        try {
            VirtualFile projectFolder = getModuleDir(dir);
            if (projectFolder != null) {
                Properties configProp = parseConfig(projectFolder);
                if (configProp != null) {
                    rPackage = configProp.getProperty(CONF_R_PACKAGE,
                            "missing R_PACKAGE");

                    baseVhPackage = configProp.getProperty(CONF_BASE_VH_PACKAGE,
                            "missing BASE_VH_PACKAGE");

                    baseVhName = configProp.getProperty(CONF_BASE_VH_NAME,
                            "missing BASE_VH_NAME");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private String rPackage;
    private String baseVhPackage;
    private String baseVhName;

    private void injectConfigIntoTemplateClass(PsiDirectory dir, final String typeName, final PsiClass itemClass) {
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
                        .replace(CONF_R_PACKAGE, rPackage)
                        .replace(CONF_BASE_VH_PACKAGE, baseVhPackage)
                        .replace(CONF_BASE_VH_NAME, baseVhName)
                        .replace("VHVO_PREFIXCLASS", typeName) //useless
                        .replace("VHVO_PREFIXLOWER_NAME", CaseFormat.UPPER_CAMEL.to(LOWER_UNDERSCORE, typeName)) //useless
                        .replace("VHVO_PREFIXNAME", CaseFormat.UPPER_CAMEL.to(LOWER_CAMEL, typeName))); // useless
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
        return JavaDirectoryService.getInstance().
                createClass(dir, className, templateName);
    }
}
