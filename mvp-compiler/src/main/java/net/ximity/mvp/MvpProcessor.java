package net.ximity.mvp;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import net.ximity.annotation.MvpComponent;
import net.ximity.annotation.MvpContract;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

import static com.google.auto.common.MoreElements.getPackage;

@SupportedAnnotationTypes({
        "net.ximity.annotation.MvpComponent",
})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@AutoService(Processor.class)
public final class MvpProcessor extends AbstractProcessor {

    private final String VIEW_PACKAGE = "net.ximity.mvp.view";
    private boolean HALT = false;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        Util.init(processingEnv);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (!processAnnotations(roundEnv)) {
            return HALT;
        }

        if (roundEnv.processingOver()) {
            HALT = true;
        }

        return HALT;
    }

    private boolean processAnnotations(RoundEnvironment roundEnv) {
        return processMvpModules(roundEnv) &&
                processMvpSubcomponents(roundEnv) &&
                processMainComponent(roundEnv);
    }

    private boolean processMvpModules(RoundEnvironment roundEnv) {
        final Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(MvpContract.class);

        if (Util.isEmpty(elements)) {
            return true;
        }

        for (Element element : elements) {
            if (element.getKind() != ElementKind.INTERFACE) {
                Util.error(MvpContract.class.getSimpleName() + " can only be used for interfaces!");
                return false;
            }
        }

        return true;
    }

    private boolean processMvpSubcomponents(RoundEnvironment roundEnv) {
        final Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(MvpContract.class);

        if (Util.isEmpty(elements)) {
            return true;
        }

        for (Element element : elements) {
            if (element.getKind() != ElementKind.INTERFACE) {
                Util.error(MvpContract.class.getSimpleName() + " can only be used for interfaces!");
                return false;
            }
        }

        return true;
    }

    private boolean processMainComponent(RoundEnvironment roundEnv) {
        final Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(MvpComponent.class);

        if (Util.isEmpty(elements)) {
            return true;
        }

        if (elements.size() > 1) {
            Util.error("Only one component can be annotated with " + MvpComponent.class.getSimpleName() + "!");
        }

        for (Element element : elements) {
            if (element.getKind() != ElementKind.INTERFACE) {
                Util.error(MvpComponent.class.getSimpleName() + " can only be used for interfaces!");
                return false;
            }

            if (!generateBaseViews((TypeElement) element)) {
                return false;
            }

            if (!generateBaseComponent((TypeElement) element)) {
                return false;
            }
        }

        return true;
    }

    private boolean generateBaseViews(TypeElement element) {
        final String packageName = getPackage(element).toString();

        final ClassName activityView = ClassName.get(VIEW_PACKAGE, "BaseActivityView");
        final TypeSpec.Builder activityBuilder = TypeSpec.classBuilder("ActivityView")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .superclass(ParameterizedTypeName.get(activityView, ClassName.get(element)));

        Util.writeJavaFile(JavaFile.builder(packageName, activityBuilder.build())
                .build(), "ActivityView");

        final ClassName fragmentView = ClassName.get(VIEW_PACKAGE, "BaseFragmentView");
        final TypeSpec.Builder fragmentBuilder = TypeSpec.classBuilder("FragmentView")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .superclass(ParameterizedTypeName.get(fragmentView, ClassName.get(element)));

        Util.writeJavaFile(JavaFile.builder(packageName, fragmentBuilder.build())
                .build(), "FragmentView");

        final ClassName dialogView = ClassName.get(VIEW_PACKAGE, "BaseDialogView");
        final TypeSpec.Builder dialogBuilder = TypeSpec.classBuilder("DialogView")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .superclass(ParameterizedTypeName.get(dialogView, ClassName.get(element)));

        Util.writeJavaFile(JavaFile.builder(packageName, dialogBuilder.build())
                .build(), "DialogView");

        return true;
    }

    private boolean generateBaseComponent(TypeElement element) {
        final MvpComponent component = element.getAnnotation(MvpComponent.class);
        final String componentName = component.value();
        final TypeSpec mvpBindings = TypeSpec.interfaceBuilder(componentName)
                .addMethod(MethodSpec.methodBuilder("bind")
                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                        .addParameter(ClassName.get("android.app", "Activity"), "activity")
                        .build())
                .build();

        final String packageName = getPackage(element).getQualifiedName().toString();
        final JavaFile output = JavaFile.builder(packageName, mvpBindings)
                .build();

        Util.writeJavaFile(output, componentName);
        return true;
    }
}
