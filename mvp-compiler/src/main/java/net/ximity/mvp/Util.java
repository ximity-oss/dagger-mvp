package net.ximity.mvp;

import com.squareup.javapoet.JavaFile;

import net.ximity.annotation.MvpContract;

import java.io.IOException;
import java.util.Collection;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

/**
 * Annotation processing utility class methods
 *
 * @author by Emarc Magtanong on 2017/08/16.
 */
final class Util {

    private static Messager messager;
    private static Filer filer;
    private static Types typeUtil;

    public static void init(ProcessingEnvironment environment) {
        messager = environment.getMessager();
        filer = environment.getFiler();
        typeUtil = environment.getTypeUtils();
    }

    static void error(String message) {
        messager.printMessage(Diagnostic.Kind.ERROR, message);
    }

    static void warn(String message) {
        messager.printMessage(Diagnostic.Kind.WARNING, message);
    }

    static void note(String message) {
        messager.printMessage(Diagnostic.Kind.NOTE, message);
    }

    static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    static boolean isEmpty(String value) {
        return value == null || value.length() == 0;
    }

    static void writeJavaFile(JavaFile file, String generatedFile) {
        if (!isEmpty(generatedFile)) Util.note("Generating " + generatedFile + "...");
        try {
            file.writeTo(filer);
            if (!isEmpty(generatedFile)) Util.note("Generated " + generatedFile);
        } catch (IOException e) {
            if (!isEmpty(generatedFile)) Util.warn("Unable to generate file for " + generatedFile + "!");
        }
    }

    static TypeElement getView(TypeElement element) {
        try {
            element.getAnnotation(MvpContract.class).view();
        } catch (MirroredTypeException e) {
            return (TypeElement) typeUtil.asElement(e.getTypeMirror());
        }

        return null;
    }

    static TypeElement getPresenter(TypeElement element) {
        try {
            element.getAnnotation(MvpContract.class).presenter();
        } catch (MirroredTypeException e) {
            return (TypeElement) typeUtil.asElement(e.getTypeMirror());
        }

        return null;
    }

    private Util() {
    }
}
