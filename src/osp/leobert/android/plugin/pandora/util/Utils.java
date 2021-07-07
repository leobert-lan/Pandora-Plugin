package osp.leobert.android.plugin.pandora.util;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiManager;

import java.io.File;

/**
 * <p><b>Package:</b> osp.leobert.android.plugin.pandora.util </p>
 * <p><b>Project:</b> Pandora-Plugin </p>
 * <p><b>Classname:</b> Utils </p>
 * Created by leobert on 2019/4/29.
 */
public class Utils {

    public static final String CONF_R_PACKAGE = "R_PACKAGE";
    public static final String CONF_BASE_VH_PACKAGE = "BASE_VH_PACKAGE";
    public static final String CONF_BASE_VH_NAME = "BASE_VH_NAME";
    public static final String CONF_BASE_KT_VH_PACKAGE = "BASE_KT_VH_PACKAGE";
    public static final String CONF_BASE_KT_VH_NAME = "BASE_KT_VH_NAME";

    public static final String TEMPLATE_VH_IMPORT = "TEMPLATE_VH_IMPORT";
    public static final String TEMPLATE_VH_CREATOR = "TEMPLATE_VH_CREATOR";
    public static final String TEMPLATE_REACTIVE_VH_CREATOR = "TEMPLATE_REACTIVE_VH_CREATOR";

    public static final String KT_TEMPLATE = "KT_TEMPLATE";
    public static final String KT_TEMPLATE_REACTIVE = "KT_TEMPLATE_REACTIVE";

    public static VirtualFile getModuleDir(PsiDirectory directory) {
        if (directory == null) return null;
        PsiDirectory parent = directory.getParent();
        if (parent == null) return null;
        if ("src".equals(directory.getName())) {
            return parent.getVirtualFile();
        } else {
            return getModuleDir(parent);
        }
    }

    public static VirtualFile getModuleDir(VirtualFile directory) {
        if (directory == null) return null;
        VirtualFile parent = directory.getParent();
        if (parent == null) return null;
        if ("src".equals(directory.getName())) {
            return parent;
        } else {
            return getModuleDir(parent);
        }
    }


    public static Properties parseConfig(VirtualFile moduleDir) {
        if (moduleDir == null) return null;

        VirtualFile pandoraFile = moduleDir.findChild("pandora");
        if (pandoraFile == null) return null;

        VirtualFile config = pandoraFile.findChild("config.properties");
        if (config == null) return null;

        String path = config.getPath();
        try {
            return Properties.newInstance(new File(path));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static PsiDirectory getDefaultLayoutRes(VirtualFile moduleDir, Project project) {
        if (moduleDir == null) return null;

        VirtualFile srcFile = moduleDir.findChild("src");
        if (srcFile == null) return null;

        VirtualFile resFile = moduleDir.findChild("res");
        if (resFile == null) return null;

        VirtualFile layoutFile = moduleDir.findChild("layout");
        if (layoutFile == null) return null;

        return PsiManager.getInstance(project).findDirectory(layoutFile);
    }

}
