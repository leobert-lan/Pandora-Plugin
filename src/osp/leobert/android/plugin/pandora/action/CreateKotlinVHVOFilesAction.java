package osp.leobert.android.plugin.pandora.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.pom.Navigatable;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.PsiManager;
import com.intellij.psi.impl.file.PsiDirectoryFactory;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import osp.leobert.android.plugin.pandora.CreateKtCodesDialog;
import osp.leobert.android.plugin.pandora.kt.FileSaver;
import osp.leobert.android.plugin.pandora.kt.IDEFileSaver;
import osp.leobert.android.plugin.pandora.kt.KotlinFileType;
import osp.leobert.android.plugin.pandora.kt.Model;
import osp.leobert.android.plugin.pandora.kt.SingleFileGenerator;
import osp.leobert.android.plugin.pandora.kt.SourceFilesGenerator;
import osp.leobert.android.plugin.pandora.util.Properties;
import osp.leobert.android.plugin.pandora.util.Utils;

import static osp.leobert.android.plugin.pandora.util.Utils.CONF_BASE_KT_VH_NAME;
import static osp.leobert.android.plugin.pandora.util.Utils.CONF_BASE_KT_VH_PACKAGE;
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
public class CreateKotlinVHVOFilesAction extends AnAction implements DumbAware, CreateKtCodesDialog.OnOKListener {

    public CreateKotlinVHVOFilesAction() {
        super("GenerateKotlinClassFile");
    }

    private CreateKtCodesDialog dialog;

    private PsiDirectory directory;

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {

        Project project = event.getProject();
        if (project == null) return;
        DataContext dataContext = event.getDataContext();
        final Module module = DataKeys.MODULE.getData(dataContext);
        if (module == null) return;

        final Navigatable navigatable = DataKeys.NAVIGATABLE.getData(dataContext);

        if (navigatable != null) {
            if (navigatable instanceof PsiDirectory) {
                directory = (PsiDirectory) navigatable;
            }
        }

        if (directory == null) {
            ModuleRootManager root = ModuleRootManager.getInstance(module);
            for (VirtualFile file : root.getSourceRoots()) {
                directory = PsiManager.getInstance(project).findDirectory(file);
            }
        }

        dialog = new CreateKtCodesDialog();
        dialog.setOnOKListener(this);
        dialog.setTitle("Generate Pandora VH and VO Codes");
        dialog.pack();
        dialog.setLocationRelativeTo(WindowManager.getInstance().getFrame(event.getProject()));
        dialog.setVisible(true);
        dialog.requestFocusInWindow();
    }


    private VirtualFile getModuleDir(PsiDirectory directory) {
        return Utils.getModuleDir(directory);
    }


    private Properties parseConfig(VirtualFile moduleDir) {
        return Utils.parseConfig(moduleDir);
    }


    private void initConfig(final PsiDirectory dir) {
        try {
            VirtualFile projectFolder = getModuleDir(dir);
            if (projectFolder != null) {
                Properties configProp = parseConfig(projectFolder);
                if (configProp != null) {
                    rPackage = configProp.getProperty(CONF_R_PACKAGE,
                            "com.jdd.motorfans");

                    baseVhPackage = configProp.getProperty(CONF_BASE_VH_PACKAGE,
                            "com.jdd.motorfans.common.base.adapter.vh2");

                    baseVhName = configProp.getProperty(CONF_BASE_VH_NAME,
                            "AbsViewHolder2");

                    baseKtVhPackage = configProp.getProperty(CONF_BASE_KT_VH_PACKAGE,
                            "com.jdd.motorfans.common.base.adapter.vh2");

                    baseKtVhName = configProp.getProperty(CONF_BASE_KT_VH_NAME,
                            "AbsViewHolder2");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private String rPackage;
    private String baseVhPackage;
    private String baseVhName;
    private String baseKtVhPackage;
    private String baseKtVhName;


    @Override
    public boolean isDumbAware() {
        return true;
    }

    @Override
    public void onOK(String text, boolean onlyVh, int type) {

        Project project = directory.getProject();
        PsiFileFactory factory = PsiFileFactory.getInstance(project);
        PsiDirectoryFactory directoryFactory = PsiDirectoryFactory.getInstance(directory.getProject());
        String packageName = directoryFactory.getQualifiedName(directory, true);

        FileSaver fileSaver = new IDEFileSaver(factory, directory, KotlinFileType.INSTANCE);
        initConfig(directory);

        fileSaver.setListener(fileName -> {
//            int ok = Messages.showOkCancelDialog(
////                    textResources.getReplaceDialogMessage(fileName),
////                    textResources.getReplaceDialogTitle(),
////                    UIUtil.getQuestionIcon());
////            return ok == 0;
            return true;
        });

        SourceFilesGenerator generator;

        //String rPackage, String baseVhPackage, String baseVhName
        Model model = new Model(text, onlyVh, rPackage, baseVhPackage, baseVhName, baseKtVhPackage, baseKtVhName);
        generator = new SingleFileGenerator(text + "Widget.kt", fileSaver);

        generator.setListener(filesCount -> {
                }
//                NotificationsHelper.showNotification(directory.getProject(),
//                        textResources.getGeneratedFilesMessage(filesCount))
        );

        generator.generateFiles(packageName, Arrays.asList(model));

    }

}
