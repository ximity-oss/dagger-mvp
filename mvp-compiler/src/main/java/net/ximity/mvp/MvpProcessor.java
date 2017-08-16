package net.ximity.mvp;

import com.google.auto.common.MoreElements;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import net.ximity.annotation.MvpComponent;

import java.io.IOException;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
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
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

@SupportedAnnotationTypes({
        "net.ximity.annotation.MvpComponent",
})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@AutoService(Processor.class)
public final class MvpProcessor extends AbstractProcessor {

    private Elements elementUtils;
    private Types typeUtils;
    private Filer filer;

    private boolean HALT = false;
    private int round = -1;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils = processingEnv.getElementUtils();
        typeUtils = processingEnv.getTypeUtils();
        filer = processingEnv.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        round++;

        if (round == 0) {
            Util.init(processingEnv);
        }

        if (!processAnnotations(roundEnv)) {
            return HALT;
        }

        if (roundEnv.processingOver()) {
            HALT = true;
        }

        return HALT;
    }

    private boolean processAnnotations(RoundEnvironment roundEnv) {
        return processMainComponent(roundEnv);
    }

    private boolean processMainComponent(RoundEnvironment roundEnv) {
        final Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(MvpComponent.class);

        if (Util.isEmpty(elements)) {
            return true;
        }

        if (elements.size() > 1) {
            Util.error("Only one component can be annotated with @MvpMainComponent");
        }

        for (Element element : elements) {
            if (element.getKind() != ElementKind.INTERFACE) {
                Util.error("@MvpMainComponent can only be used for interfaces!");
                return false;
            }

            if (!generateBaseComponent((TypeElement) element)) {
                return false;
            }
        }

        return true;
    }

    private boolean generateBaseComponent(TypeElement element) {
        final MvpComponent component = element.getAnnotation(MvpComponent.class);
        final String componentName = Util.isEmpty(component.value()) ? "MvpBindings" : component.value();
        Util.note("Generating " + componentName + "...");
        final TypeSpec.Builder builder = TypeSpec.interfaceBuilder(Util.isEmpty(componentName) ? "MvpBindings" : componentName)
                .addMethod(MethodSpec.methodBuilder("bind")
                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                        .addParameter(ClassName.get("android.app", "Activity"), "activity")
                        .build());

        final TypeSpec newClass = builder.build();
        final String packageName = MoreElements.getPackage(element).getQualifiedName().toString();
        final JavaFile javaFile = JavaFile.builder(packageName, newClass)
                .build();

        try {
            javaFile.writeTo(filer);
        } catch (IOException e) {
            Util.warn("Unable to generate file for");
        }
        return true;
    }
}
