package osp.leobert.android.plugin.pandora.template;

import com.intellij.ide.fileTemplates.FileTemplateDescriptor;
import com.intellij.ide.fileTemplates.FileTemplateGroupDescriptor;
import com.intellij.ide.fileTemplates.FileTemplateGroupDescriptorFactory;
import osp.leobert.android.plugin.pandora.util.Icons;


public class LayoutFileTemplateDescriptorFactory implements FileTemplateGroupDescriptorFactory {

  public static final String LAYOUT_XML_TEMPLATE = "vh_layout.xml";
  public static final String DATA_BINDING_LAYOUT_XML_TEMPLATE = "data_binding_vh_layout.xml";

  @Override
  public FileTemplateGroupDescriptor getFileTemplatesDescriptor() {
    FileTemplateGroupDescriptor group = new FileTemplateGroupDescriptor("Pandora", Icons.PANDORA_LOGO);
    group.addTemplate(new FileTemplateDescriptor(LAYOUT_XML_TEMPLATE, Icons.PANDORA_LOGO));
    return group;
  }

}
