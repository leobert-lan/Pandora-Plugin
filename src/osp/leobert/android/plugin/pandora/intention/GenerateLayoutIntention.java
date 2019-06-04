package osp.leobert.android.plugin.pandora.intention;

import com.intellij.codeInsight.hint.HintManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;

import org.jetbrains.annotations.NotNull;

import osp.leobert.android.plugin.pandora.service.EditorService;
import osp.leobert.android.plugin.pandora.template.LayoutFileTemplateDescriptorFactory;
import osp.leobert.android.plugin.pandora.ui.UiComponentFacade;
import osp.leobert.android.plugin.pandora.util.XmlLayoutUtils;

/**
 * <p><b>Package:</b> osp.leobert.android.plugin.pandora.intention </p>
 * <p><b>Project:</b> Pandora-Plugin </p>
 * <p><b>Classname:</b> GenerateLayoutIntention </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2018/10/30.
 */
public class GenerateLayoutIntention extends GenericIntention {

    public GenerateLayoutIntention() {
        super(GenerateLayoutChooser.INSTANCE);
    }

    @NotNull
    @Override
    public String getText() {
        return "[Pandora] Generate layout of ViewHolder";
    }

    @Override
    public boolean startInWriteAction() {
        return false;
    }

    @Override
    public void invoke(@NotNull final Project project, final Editor editor, PsiFile file) throws IncorrectOperationException {
        PsiElement element = file.findElementAt(editor.getCaretModel().getOffset());
        PsiClass clazz = PsiTreeUtil.getParentOfType(element, PsiClass.class);
        handleChooseNewFolder(project, editor, clazz, file);
    }

    private void handleChooseNewFolder(Project project, Editor editor, PsiClass clazz, PsiFile operatingFile) {
        UiComponentFacade uiComponentFacade = UiComponentFacade.getInstance(project);
        VirtualFile baseDir = project.getBaseDir();

        baseDir = findAndroidResDir(baseDir, operatingFile);

        VirtualFile vf = uiComponentFacade.showSingleFolderSelectionDialog("Select target folder", baseDir, baseDir);
        if (null != vf) {
            processGenerate(editor, clazz, PsiManager.getInstance(project).findDirectory(vf));
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

//    private String[] getPathTextForShown(Project project, List<String> paths, final Map<String, PsiDirectory> pathMap) {
//        Collections.sort(paths);
//        final String projectBasePath = project.getBasePath();
//        Collection<String> result = Lists.newArrayList(Collections2.transform(paths, new Function<String, String>() {
//            @Override
//            public String apply(String input) {
//                String relativePath = FileUtil.getRelativePath(projectBasePath, input, File.separatorChar);
//                Module module = ModuleUtil.findModuleForPsiElement(pathMap.get(input));
//                return null == module ? relativePath : ("[" + module.getName() + "] " + relativePath);
//            }
//        }));
//        return result.toArray(new String[result.size()]);
//    }
//
//    private Map<String, PsiDirectory> getPathMap(Collection<PsiDirectory> directories) {
//        Map<String, PsiDirectory> result = Maps.newHashMap();
//        for (PsiDirectory directory : directories) {
//            String presentableUrl = directory.getVirtualFile().getPresentableUrl();
//            if (presentableUrl != null) {
//                result.put(presentableUrl, directory);
//            }
//        }
//        return result;
//    }

    private void processGenerate(Editor editor, PsiClass clazz, PsiDirectory directory) {
        if (null == directory) {
            return;
        }
        if (!directory.isWritable()) {
            HintManager.getInstance().showErrorHint(editor, "Target directory is not writable");
            return;
        }
        try {
            HintManager.getInstance().showInformationHint(editor, "Info: className is: " + clazz.getName());
            String name = XmlLayoutUtils.createLayoutFileName(clazz);
            PsiElement psiFile = XmlLayoutUtils.createLayoutFileFromFileTemplate(LayoutFileTemplateDescriptorFactory.LAYOUT_XML_TEMPLATE,
                    name, directory, null);
            EditorService.getInstance(clazz.getProject()).scrollTo(psiFile, 0);
        } catch (Exception e) {
            HintManager.getInstance().showErrorHint(editor, "Failed: " + e.getCause());
        }
    }


}
