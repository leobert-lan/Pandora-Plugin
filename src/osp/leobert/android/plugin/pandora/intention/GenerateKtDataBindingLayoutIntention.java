package osp.leobert.android.plugin.pandora.intention;

import com.google.common.base.CaseFormat;
import com.intellij.codeInsight.hint.HintManager;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.impl.file.PsiDirectoryFactory;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;

import org.jetbrains.annotations.NotNull;

import osp.leobert.android.plugin.pandora.service.EditorService;
import osp.leobert.android.plugin.pandora.template.LayoutFileTemplateDescriptorFactory;
import osp.leobert.android.plugin.pandora.ui.UiComponentFacade;
import osp.leobert.android.plugin.pandora.util.XmlLayoutUtils;

import java.util.Locale;

import static com.google.common.base.CaseFormat.LOWER_UNDERSCORE;
import static osp.leobert.android.plugin.pandora.util.Utils.CONF_BASE_KT_VH_NAME;
import static osp.leobert.android.plugin.pandora.util.Utils.CONF_BASE_KT_VH_PACKAGE;
import static osp.leobert.android.plugin.pandora.util.Utils.CONF_R_PACKAGE;
import static osp.leobert.android.plugin.pandora.util.Utils.getModuleDir;
import static osp.leobert.android.plugin.pandora.util.Utils.parseConfig;

/**
 * <p><b>Package:</b> osp.leobert.android.plugin.pandora.intention </p>
 * <p><b>Project:</b> Pandora-Plugin </p>
 * <p><b>Classname:</b> GenerateLayoutIntention </p>
 * Created by leobert on 2018/10/30.
 */
public class GenerateKtDataBindingLayoutIntention extends GenericIntention {

    public GenerateKtDataBindingLayoutIntention() {
        super(GenerateKtDataBindingLayoutChooser.INSTANCE);
    }

    @NotNull
    @Override
    public String getText() {
        return "[Pandora] Generate DataBinding layout of ViewHolder";
    }

    @Override
    public boolean startInWriteAction() {
        return false;
    }

    private String packageName;

    @Override
    public void invoke(@NotNull final Project project, final Editor editor, PsiFile file) throws IncorrectOperationException {
        PsiElement element = file.findElementAt(editor.getCaretModel().getOffset());
        PsiFile ktFile = PsiTreeUtil.getParentOfType(element, PsiFile.class);
        if (ktFile == null) return;

        try {
            packageName = PsiDirectoryFactory.getInstance(project)
                    .getQualifiedName(ktFile.getContainingDirectory(), true);
        } catch (Exception e) {

        }


        String name = ktFile.getName();
        if (name.contains("Widget"))
            name = name.replace("Widget.kt", "");
        handleChooseNewFolder(project, editor, name, file);
    }

    private void handleChooseNewFolder(Project project, Editor editor, String foo, PsiFile operatingFile) {
        UiComponentFacade uiComponentFacade = UiComponentFacade.getInstance(project);
        VirtualFile baseDir = project.getBaseDir();

        baseDir = findAndroidResDir(baseDir, operatingFile);

        initConfig(PsiManager.getInstance(project).findDirectory(baseDir));

        VirtualFile vf = uiComponentFacade.showSingleFolderSelectionDialog("Select target folder", baseDir, baseDir);
        if (null != vf) {
            processGenerate(editor, foo, PsiManager.getInstance(project).findDirectory(vf), project);
        }
    }

    private VirtualFile findAndroidResDir(VirtualFile projectBaseDir, PsiFile operatingFile) {
        VirtualFile tmp = operatingFile.getVirtualFile();
        final String DIR_MAIN = "main";
        final String DIR_BASE = projectBaseDir.getName();
        boolean findMainDir = false;

        while (tmp.getParent() != null) {
            tmp = tmp.getParent();
            if ("/".equals(tmp.getName()) || DIR_BASE.equals(tmp.getName())) {
                findMainDir = false;
                break;
            }

            if (DIR_MAIN.equals(tmp.getName())) {
                findMainDir = true;
                break;
            }
        }
        if (findMainDir) {
            VirtualFile res = tmp.findChild("res");
            if (res != null)
                return res;
        }

        return projectBaseDir;
    }

    private void initConfig(final PsiDirectory dir) {
        try {
            VirtualFile projectFolder = getModuleDir(dir);
            if (projectFolder != null) {
                osp.leobert.android.plugin.pandora.util.Properties configProp = parseConfig(projectFolder);
                if (configProp != null) {

                    baseVhPackage = configProp.getProperty(CONF_BASE_KT_VH_PACKAGE,
                            "missing missing CONF_BASE_KT_VH_PACKAGE");

                    baseVhName = configProp.getProperty(CONF_BASE_KT_VH_NAME,
                            "AbsViewHolder2");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private String baseVhPackage = "missing CONF_BASE_KT_VH_PACKAGE";
    private String baseVhName = "AbsViewHolder2";


    private void processGenerate(Editor editor, String foo, PsiDirectory directory, Project project) {
        if (null == directory) {
            return;
        }
        if (!directory.isWritable()) {
            HintManager.getInstance().showErrorHint(editor, "Target directory is not writable");
            throw new IllegalStateException("Target directory is not writable");
//            return;
        }
        String name = "app_vh_" + CaseFormat.UPPER_CAMEL.to(LOWER_UNDERSCORE, foo);
        PsiElement psiFile;
        //CustomFileTemplate

//        document.setText(document.getText()
//                .replace("PREFIX", foo)
//                .replace("BASE_KT_VH_PACKAGE", baseVhPackage)
//                .replace("BASE_KT_VH_NAME", baseVhName)
//                .replace("PACKAGE", packageName));

//        try {
//
//            psiFile = XmlLayoutUtils.createLayoutFileFromText(
//                    template.replace("PREFIX", foo)
//                            .replace("BASE_KT_VH_PACKAGE", baseVhPackage)
//                            .replace("BASE_KT_VH_NAME", baseVhName)
//                            .replace("PACKAGE", packageName),
//                    name, directory, null);
//        } catch (Exception e) {
//            throw new IllegalArgumentException(e.getMessage());
//        }

        try {

            psiFile = XmlLayoutUtils.createLayoutFileFromFileTemplate(LayoutFileTemplateDescriptorFactory.DATA_BINDING_LAYOUT_XML_TEMPLATE,
                    name, directory, null);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }


        try {
            if (psiFile != null)
                EditorService.getInstance(project).scrollTo(psiFile, 0);
        } catch (Exception e) {
            HintManager.getInstance().showErrorHint(editor, "Failed: " + e.getCause());
        }

        fixTemp(project, (PsiFile) psiFile, foo, packageName, baseVhPackage, baseVhName);
    }

    private void fixTemp(Project project, PsiFile file, String foo, String packageName,
                         String baseVhPackage, String baseVhName) {

        final PsiDocumentManager manager = PsiDocumentManager.getInstance(project);
        final Document document = manager.getDocument(file);
        if (document == null) {
            return;
        }


        new WriteCommandAction.Simple(project) {
            @Override
            protected void run() {
                manager.doPostponedOperationsAndUnblockDocument(document);
                //properties.setProperty("PREFIX", foo);
                ////        //PACKAGE
                ////        properties.setProperty("PACKAGE", packageName);
                ////
                ////        //BASE_KT_VH_PACKAGE
                ////        properties.setProperty("BASE_KT_VH_PACKAGE", baseVhPackage);
                ////        //BASE_KT_VH_NAME
                ////        properties.setProperty("BASE_KT_VH_NAME", baseVhName);
                document.setText(document.getText()
                        .replace("PREFIX", foo)
                        .replace("BASE_KT_VH_PACKAGE", baseVhPackage)
                        .replace("BASE_KT_VH_NAME", baseVhName)
                        .replace("PACKAGE", packageName));
            }
        }.execute();
    }

    private static final String template = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
            "<layout>\n" +
            "    <data>\n" +
            "        <variable name=\"vo\"\n" +
            "            type=\"PACKAGE.PREFIXVO2\" />\n" +
            "\n" +
            "        <variable name=\"itemInteract\"\n" +
            "            type=\"PACKAGE.PREFIXItemInteract\" />\n" +
            "\n" +
            "        <variable name=\"vh\"\n" +
            "            type=\"BASE_KT_VH_PACKAGE.BASE_KT_VH_NAME\" />\n" +
            "    </data>\n" +
            "\n" +
            "    <FrameLayout xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
            "        xmlns:app=\"http://schemas.android.com/apk/res-auto\"\n" +
            "        android:layout_width=\"match_parent\"\n" +
            "        android:layout_height=\"wrap_content\">\n" +
            "\n" +
            "        <!-- android:onClick=\"@{(view)->itemInteract.changeData(vh.getAdapterPosition(),item)}\"-->\n" +
            "        <TextView android:layout_width=\"wrap_content\" android:layout_height=\"wrap_content\"\n" +
            "            android:text=\"PREFIX\" />\n" +
            "    </FrameLayout>\n" +
            "</layout>";


}
