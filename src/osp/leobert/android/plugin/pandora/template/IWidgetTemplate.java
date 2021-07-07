package osp.leobert.android.plugin.pandora.template;

import osp.leobert.android.plugin.pandora.kt.Model;

import java.util.List;

/**
 * <p><b>Package:</b> osp.leobert.android.plugin.pandora.template </p>
 * <p><b>Project:</b> Pandora-Plugin2 </p>
 * <p><b>Classname:</b> IWidgetTemplate </p>
 * Created by leobert on 2021/7/7.
 */
public interface IWidgetTemplate {
    String generateFiles(String packageName, List<Model> models);
}
