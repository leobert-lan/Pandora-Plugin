package osp.leobert.android.plugin.pandora.util;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PropertyUtil;
import com.intellij.psi.util.PsiTreeUtil;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public final class JavaUtils {

  private JavaUtils() {
    throw new UnsupportedOperationException();
  }

  public static boolean isModelClazz(@Nullable PsiClass clazz) {
    return null != clazz && !clazz.isAnnotationType() && !clazz.isInterface() && !clazz.isEnum() && clazz.isValid();
  }

  @NotNull
  public static Optional<PsiField> findSettablePsiField(@NotNull PsiClass clazz, @Nullable String propertyName) {
    PsiMethod propertySetter = PropertyUtil.findPropertySetter(clazz, propertyName, false, true);
    return null == propertySetter ? Optional.<PsiField>absent() : Optional.fromNullable(PropertyUtil.findPropertyFieldByMember(propertySetter));
  }

  @NotNull
  public static PsiField[] findSettablePsiFields(@NotNull PsiClass clazz) {
    PsiMethod[] methods = clazz.getAllMethods();
    List<PsiField> fields = Lists.newArrayList();
    for (PsiMethod method : methods) {
      if (PropertyUtil.isSimplePropertySetter(method)) {
        Optional<PsiField> psiField = findSettablePsiField(clazz, PropertyUtil.getPropertyName(method));
        if (psiField.isPresent()) {
          fields.add(psiField.get());
        }
      }
    }
    return fields.toArray(new PsiField[fields.size()]);
  }

  public static boolean isElementWithinInterface(@Nullable PsiElement element) {
    if (element instanceof PsiClass && ((PsiClass) element).isInterface()) {
      return true;
    }
    PsiClass type = PsiTreeUtil.getParentOfType(element, PsiClass.class);
    return Optional.fromNullable(type).isPresent() && type.isInterface();
  }

  @NotNull
  public static Optional<PsiClass> findClazz(@NotNull Project project, @NotNull String clazzName) {
    return Optional.fromNullable(JavaPsiFacade.getInstance(project).findClass(clazzName, GlobalSearchScope.allScope(project)));
  }

  @NotNull
  public static Optional<PsiMethod> findMethod(@NotNull Project project, @Nullable String clazzName, @Nullable String methodName) {
    if (StringUtils.isBlank(clazzName) && StringUtils.isBlank(methodName)) {
      return Optional.absent();
    }
    Optional<PsiClass> clazz = findClazz(project, clazzName);
    if (clazz.isPresent()) {
      PsiMethod[] methods = clazz.get().findMethodsByName(methodName, true);
      return ArrayUtils.isEmpty(methods) ? Optional.<PsiMethod>absent() : Optional.of(methods[0]);
    }
    return Optional.absent();
  }

}
