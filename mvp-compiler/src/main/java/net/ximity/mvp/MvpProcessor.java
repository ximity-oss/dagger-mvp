package net.ximity.mvp;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import net.ximity.annotation.MainComponent;
import net.ximity.annotation.MvpContract;
import net.ximity.annotation.PerView;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.inject.Inject;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import static com.google.auto.common.MoreElements.getPackage;
import static net.ximity.mvp.Util.asElement;
import static net.ximity.mvp.Util.error;
import static net.ximity.mvp.Util.isSubTypePresenter;
import static net.ximity.mvp.Util.writeJavaFile;

@SupportedAnnotationTypes({
        "net.ximity.annotation.MainComponent",
        "net.ximity.annotation.MvpContract",
})
@AutoService(Processor.class)
public final class MvpProcessor extends AbstractProcessor {

    private final String TEMPLATE_PACKAGE = "net.ximity.mvp.template";
    private final String CONTRACT_PACKAGE = "net.ximity.mvp.contract";
    private boolean HALT = false;
    private final List<Binding> bindings = new ArrayList<>();

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

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
                error(MvpContract.class.getSimpleName() + " can only be used for interfaces!");
                return false;
            }

            if (!generateMvpModule((TypeElement) element)) {
                return false;
            }
        }

        return true;
    }

    @SuppressWarnings({"ConstantConditions", "unchecked"})
    private boolean generateMvpModule(TypeElement element) {
        final String packageName = getPackage(element).toString();
        final MvpContract contract = element.getAnnotation(MvpContract.class);

        TypeElement view = Util.getView(element);
        List<TypeMirror> viewImplements = (List<TypeMirror>) view.getInterfaces();
        TypeElement presenter = Util.getPresenter(element);
        List<TypeMirror> presenterImplements = (List<TypeMirror>) presenter.getInterfaces();

        Annotation presenterAnnotation = presenter.getAnnotation(PerView.class);
        if (presenterAnnotation == null) {
            error(presenter.getSimpleName().toString() + " does not have a @PerView scope!!!");
        }

        String moduleClassName = Util.isEmpty(contract.moduleName()) ?
                element.getSimpleName().toString() + "Module" :
                contract.moduleName();

        if (viewImplements.isEmpty()) {
            error(view.getSimpleName().toString() + " does not implement "
                    + element.getSimpleName() + " view contract! (Does not implement any interfaces)");
            return false;
        }

        if (presenterImplements.isEmpty()) {
            error(presenter.getSimpleName().toString() + " does not implement "
                    + element.getSimpleName() + " presenter contract!");
            return false;
        }

        TypeElement viewInterface = null;
        for (TypeMirror typeMirror : viewImplements) {
            TypeElement currentInterface = asElement(typeMirror);
            if (element.getEnclosedElements().contains(currentInterface)) {
                viewInterface = currentInterface;
            }
        }

        if (viewInterface == null) {
            error(view.getSimpleName().toString() + " does not implement "
                    + element.getSimpleName() + " view contract!");
            return false;
        }

        TypeElement presenterInterface = null;
        boolean isViewPresenter = false;
        for (TypeMirror typeMirror : presenterImplements) {
            TypeElement currentInterface = asElement(typeMirror);
            if (element.getEnclosedElements().contains(currentInterface)) {
                presenterInterface = currentInterface;
            }

            for (TypeMirror innerType : currentInterface.getInterfaces()) {
                isViewPresenter = isSubTypePresenter(innerType);
            }
        }

        if (presenterInterface == null) {
            error(presenter.getSimpleName().toString() + " does not implement "
                    + element.getSimpleName() + " presenter contract!");
            return false;
        }

        final TypeSpec.Builder moduleBuilder = TypeSpec.classBuilder(moduleClassName)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addAnnotation(ClassName.get("dagger", "Module"))
                .addField(FieldSpec.builder(ClassName.get(viewInterface), "view")
                        .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                        .build())
                .addMethod(MethodSpec.constructorBuilder()
                        .addParameter(ParameterSpec.builder(ClassName.get(viewInterface), "view")
                                .addAnnotation(ClassName.get("android.support.annotation", "NonNull"))
                                .build())
                        .addModifiers(Modifier.PUBLIC)
                        .addStatement("this.$N = $N", "view", "view")
                        .build())
                .addMethod(MethodSpec.methodBuilder("providesView")
                        .addAnnotation(PerView.class)
                        .addAnnotation(ClassName.get("dagger", "Provides"))
                        .returns(ClassName.get(viewInterface))
                        .addStatement("return this.$N", "view")
                        .build())
                .addMethod(MethodSpec.methodBuilder("providesPresenter")
                        .addAnnotation(PerView.class)
                        .addAnnotation(ClassName.get("dagger", "Provides"))
                        .returns(ClassName.get(presenterInterface))
                        .addParameter(ClassName.get(presenter), "impl")
                        .addStatement("return $N", "impl")
                        .build());
        if (isViewPresenter) {
            moduleBuilder.addMethod(MethodSpec.methodBuilder("providesViewPresenter")
                    .addAnnotation(PerView.class)
                    .addAnnotation(ClassName.get("dagger", "Provides"))
                    .returns(ClassName.get(CONTRACT_PACKAGE, "ViewPresenter"))
                    .addParameter(ClassName.get(presenter), "impl")
                    .addStatement("return $N", "impl")
                    .build());
        }

        writeJavaFile(JavaFile.builder(packageName, moduleBuilder.build())
                .build(), moduleClassName);

        return true;
    }

    private boolean processMvpSubcomponents(RoundEnvironment roundEnv) {
        final Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(MvpContract.class);

        if (Util.isEmpty(elements)) {
            return true;
        }

        for (Element element : elements) {
            if (element.getKind() != ElementKind.INTERFACE) {
                error(MvpContract.class.getSimpleName() + " can only be used for interfaces!");
                return false;
            }

            if (!generateMvpComponent((TypeElement) element)) {
                return false;
            }
        }

        return true;
    }

    @SuppressWarnings("ConstantConditions")
    private boolean generateMvpComponent(TypeElement element) {
        final String packageName = getPackage(element).toString();
        final MvpContract contract = element.getAnnotation(MvpContract.class);

        String componentName = Util.isEmpty(contract.subcomponentName()) ?
                element.getSimpleName().toString() + "Component" :
                contract.subcomponentName();
        String moduleName = Util.isEmpty(contract.moduleName()) ?
                element.getSimpleName().toString() + "Module" :
                contract.moduleName();

        TypeElement view = Util.getView(element);

        final TypeSpec mvpBindings = TypeSpec.interfaceBuilder(componentName)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(PerView.class)
                .addAnnotation(AnnotationSpec.builder(ClassName.get("dagger", "Subcomponent"))
                        .addMember("modules", "$N.class", moduleName)
                        .build())
                .addMethod(MethodSpec.methodBuilder("bind")
                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                        .addParameter(ClassName.get(view), view.getSimpleName().toString())
                        .build())
                .build();

        final JavaFile output = JavaFile.builder(packageName, mvpBindings)
                .build();

        writeJavaFile(output, componentName);
        bindings.add(new Binding(packageName, componentName, moduleName));
        return true;
    }

    private boolean processMainComponent(RoundEnvironment roundEnv) {
        final Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(MainComponent.class);

        if (Util.isEmpty(elements)) {
            return true;
        }

        if (elements.size() > 1) {
            error("Only one component can be annotated with " + MainComponent.class.getSimpleName() + "!");
        }

        for (Element element : elements) {
            if (element.getKind() != ElementKind.INTERFACE) {
                error(MainComponent.class.getSimpleName() + " can only be used for interfaces!");
                return false;
            }

            if (!generateBaseComponents((TypeElement) element)) {
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

    private boolean generateBaseComponents(TypeElement element) {
        final ClassName application = ClassName.get(TEMPLATE_PACKAGE, "DaggerApplication");
        final TypeSpec.Builder applicationBuilder = TypeSpec.classBuilder("BaseApplication")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .superclass(ParameterizedTypeName.get(application, ClassName.get(element)));

        writeJavaFile(JavaFile.builder(TEMPLATE_PACKAGE, applicationBuilder.build())
                .build(), "BaseApplication");

        final ClassName activity = ClassName.get(TEMPLATE_PACKAGE, "DaggerActivity");
        final TypeSpec.Builder activityBuilder = TypeSpec.classBuilder("BaseActivity")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .superclass(ParameterizedTypeName.get(activity, ClassName.get(element)));

        writeJavaFile(JavaFile.builder(TEMPLATE_PACKAGE, activityBuilder.build())
                .build(), "BaseActivity");

        final ClassName fragment = ClassName.get(TEMPLATE_PACKAGE, "DaggerFragment");
        final TypeSpec.Builder fragmentBuilder = TypeSpec.classBuilder("BaseFragment")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .superclass(ParameterizedTypeName.get(fragment, ClassName.get(element)));

        writeJavaFile(JavaFile.builder(TEMPLATE_PACKAGE, fragmentBuilder.build())
                .build(), "BaseFragment");

        final ClassName dialog = ClassName.get(TEMPLATE_PACKAGE, "DaggerDialog");
        final TypeSpec.Builder dialogBuilder = TypeSpec.classBuilder("BaseDialog")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .superclass(ParameterizedTypeName.get(dialog, ClassName.get(element)));

        writeJavaFile(JavaFile.builder(TEMPLATE_PACKAGE, dialogBuilder.build())
                .build(), "BaseDialog");

        final ClassName receiver = ClassName.get(TEMPLATE_PACKAGE, "DaggerBroadcastReceiver");
        final TypeSpec.Builder receiverBuilder = TypeSpec.classBuilder("BaseReceiver")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .superclass(ParameterizedTypeName.get(receiver, ClassName.get(element)));

        writeJavaFile(JavaFile.builder(TEMPLATE_PACKAGE, receiverBuilder.build())
                .build(), "BaseReceiver");

        final ClassName service = ClassName.get(TEMPLATE_PACKAGE, "DaggerService");
        final TypeSpec.Builder serviceBuilder = TypeSpec.classBuilder("BaseService")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .superclass(ParameterizedTypeName.get(service, ClassName.get(element)));

        writeJavaFile(JavaFile.builder(TEMPLATE_PACKAGE, serviceBuilder.build())
                .build(), "BaseService");

        return true;
    }

    private boolean generateBaseViews(TypeElement element) {
        List<MethodSpec> activityMethods = new ArrayList<>();

        activityMethods.add(MethodSpec.methodBuilder("onCreate")
                .addModifiers(Modifier.PROTECTED)
                .addAnnotation(Override.class)
                .addParameter(ParameterSpec.builder(ClassName.get("android.os", "Bundle"), "saveInstanceState")
                        .addAnnotation(ClassName.get("android.support.annotation", "Nullable"))
                        .build())
                .addStatement("super.onCreate(saveInstanceState)")
                .addStatement("viewPresenter.create()")
                .build());

        activityMethods.add(MethodSpec.methodBuilder("onStart")
                .addModifiers(Modifier.PROTECTED)
                .addAnnotation(Override.class)
                .addStatement("super.onStart()")
                .addStatement("viewPresenter.start()")
                .build());

        activityMethods.add(MethodSpec.methodBuilder("onStop")
                .addModifiers(Modifier.PROTECTED)
                .addAnnotation(Override.class)
                .addStatement("super.onStop()")
                .addStatement("viewPresenter.stop()")
                .build());

        activityMethods.add(MethodSpec.methodBuilder("onDestroy")
                .addModifiers(Modifier.PROTECTED)
                .addAnnotation(Override.class)
                .addStatement("super.onDestroy()")
                .addStatement("viewPresenter.destroy()")
                .build());

        activityMethods.add(MethodSpec.methodBuilder("injectViewPresenter")
                .addAnnotation(Inject.class)
                .addModifiers(Modifier.PROTECTED)
                .addParameter(ClassName.get(CONTRACT_PACKAGE, "ViewPresenter"), "viewPresenter")
                .addStatement("this.viewPresenter = viewPresenter")
                .build());

        List<MethodSpec> fragmentMethods = new ArrayList<>();

        fragmentMethods.add(MethodSpec.methodBuilder("onCreate")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(ParameterSpec.builder(ClassName.get("android.os", "Bundle"), "saveInstanceState")
                        .addAnnotation(ClassName.get("android.support.annotation", "Nullable"))
                        .build())
                .addStatement("super.onCreate(saveInstanceState)")
                .addStatement("viewPresenter.create()")
                .build());

        fragmentMethods.add(MethodSpec.methodBuilder("onStart")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addStatement("super.onStart()")
                .addStatement("viewPresenter.start()")
                .build());

        fragmentMethods.add(MethodSpec.methodBuilder("onStop")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addStatement("super.onStop()")
                .addStatement("viewPresenter.stop()")
                .build());

        fragmentMethods.add(MethodSpec.methodBuilder("onDestroy")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addStatement("super.onDestroy()")
                .addStatement("viewPresenter.destroy()")
                .build());

        fragmentMethods.add(MethodSpec.methodBuilder("injectViewPresenter")
                .addAnnotation(Inject.class)
                .addModifiers(Modifier.PROTECTED)
                .addParameter(ClassName.get(CONTRACT_PACKAGE, "ViewPresenter"), "viewPresenter")
                .addStatement("this.viewPresenter = viewPresenter")
                .build());

        final ClassName activityView = ClassName.get(TEMPLATE_PACKAGE, "DaggerActivity");
        final TypeSpec.Builder activityBuilder = TypeSpec.classBuilder("ActivityView")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .superclass(ParameterizedTypeName.get(activityView, ClassName.get(element)))
                .addField(FieldSpec.builder(ClassName.get(CONTRACT_PACKAGE, "ViewPresenter"), "viewPresenter")
                        .addModifiers(Modifier.PRIVATE)
                        .build())
                .addMethods(activityMethods);

        writeJavaFile(JavaFile.builder(TEMPLATE_PACKAGE, activityBuilder.build())
                .build(), "ActivityView");

        final ClassName fragmentView = ClassName.get(TEMPLATE_PACKAGE, "DaggerFragment");
        final TypeSpec.Builder fragmentBuilder = TypeSpec.classBuilder("FragmentView")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .superclass(ParameterizedTypeName.get(fragmentView, ClassName.get(element)))
                .addField(FieldSpec.builder(ClassName.get(CONTRACT_PACKAGE, "ViewPresenter"), "viewPresenter")
                        .addModifiers(Modifier.PRIVATE)
                        .build())
                .addMethods(fragmentMethods);

        writeJavaFile(JavaFile.builder(TEMPLATE_PACKAGE, fragmentBuilder.build())
                .build(), "FragmentView");

        final ClassName dialogView = ClassName.get(TEMPLATE_PACKAGE, "DaggerDialog");
        final TypeSpec.Builder dialogBuilder = TypeSpec.classBuilder("DialogView")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .superclass(ParameterizedTypeName.get(dialogView, ClassName.get(element)))
                .addField(FieldSpec.builder(ClassName.get(CONTRACT_PACKAGE, "ViewPresenter"), "viewPresenter")
                        .addModifiers(Modifier.PRIVATE)
                        .build())
                .addMethods(fragmentMethods);

        writeJavaFile(JavaFile.builder(TEMPLATE_PACKAGE, dialogBuilder.build())
                .build(), "DialogView");

        return true;
    }

    private boolean generateBaseComponent(TypeElement element) {
        final MainComponent component = element.getAnnotation(MainComponent.class);
        final String componentName = component.value();
        final TypeSpec.Builder mvpBindingsBuilder = TypeSpec.interfaceBuilder(componentName)
                .addModifiers(Modifier.PUBLIC);

        for (Binding binding : bindings) {
            mvpBindingsBuilder.addMethod(MethodSpec.methodBuilder("add")
                    .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                    .addParameter(ClassName.get(binding.getPackageName(), binding.getModuleName()), "module")
                    .returns(ClassName.get(binding.getPackageName(), binding.getComponentName()))
                    .build());
        }

        final TypeSpec mvpBindings = mvpBindingsBuilder.build();

        final String packageName = getPackage(element).getQualifiedName().toString();
        final JavaFile output = JavaFile.builder(packageName, mvpBindings)
                .build();

        writeJavaFile(output, componentName);
        return true;
    }
}
