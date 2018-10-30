package osp.leobert.android.plugin.pandora.intention;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.intellij.codeInsight.hint.HintManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import osp.leobert.android.plugin.pandora.service.EditorService;
import osp.leobert.android.plugin.pandora.template.LayoutFileTemplateDescriptorFactory;
import osp.leobert.android.plugin.pandora.ui.ClickableListener;
import osp.leobert.android.plugin.pandora.ui.UiComponentFacade;
import osp.leobert.android.plugin.pandora.util.XmlLayoutUtils;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
        handleChooseNewFolder(project, editor, clazz);
    }

    private ClickableListener getChooseFolderListener(final Editor editor, final PsiClass clazz) {
        final Project project = clazz.getProject();
        return new ClickableListener() {
            @Override
            public void clicked() {
                handleChooseNewFolder(project, editor, clazz);
            }

            @Override
            public boolean isWriteAction() {
                return false;
            }
        };
    }

    private void handleChooseNewFolder(Project project, Editor editor, PsiClass clazz) {
        UiComponentFacade uiComponentFacade = UiComponentFacade.getInstance(project);
        VirtualFile baseDir = project.getBaseDir();
        VirtualFile vf = uiComponentFacade.showSingleFolderSelectionDialog("Select target folder", baseDir, baseDir);
        if (null != vf) {
            processGenerate(editor, clazz, PsiManager.getInstance(project).findDirectory(vf));
        }
    }

    private String[] getPathTextForShown(Project project, List<String> paths, final Map<String, PsiDirectory> pathMap) {
        Collections.sort(paths);
        final String projectBasePath = project.getBasePath();
        Collection<String> result = Lists.newArrayList(Collections2.transform(paths, new Function<String, String>() {
            @Override
            public String apply(String input) {
                String relativePath = FileUtil.getRelativePath(projectBasePath, input, File.separatorChar);
                Module module = ModuleUtil.findModuleForPsiElement(pathMap.get(input));
                return null == module ? relativePath : ("[" + module.getName() + "] " + relativePath);
            }
        }));
        return result.toArray(new String[result.size()]);
    }

    private Map<String, PsiDirectory> getPathMap(Collection<PsiDirectory> directories) {
        Map<String, PsiDirectory> result = Maps.newHashMap();
        for (PsiDirectory directory : directories) {
            String presentableUrl = directory.getVirtualFile().getPresentableUrl();
            if (presentableUrl != null) {
                result.put(presentableUrl, directory);
            }
        }
        return result;
    }

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
