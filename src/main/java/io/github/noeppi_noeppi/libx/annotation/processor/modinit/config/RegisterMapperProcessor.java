package io.github.noeppi_noeppi.libx.annotation.processor.modinit.config;

import io.github.noeppi_noeppi.libx.annotation.processor.modinit.ModEnv;
import io.github.noeppi_noeppi.libx.annotation.processor.modinit.ModInit;
import io.github.noeppi_noeppi.libx.config.GenericValueMapper;
import io.github.noeppi_noeppi.libx.config.ValueMapper;

import javax.lang.model.element.*;
import javax.tools.Diagnostic;

public class RegisterMapperProcessor {
    
    public static void processRegisterMapper(Element element, ModEnv env) {
        if (!(element instanceof TypeElement typeElem)) {
            env.messager().printMessage(Diagnostic.Kind.ERROR, "Failed to get qualified name for element annotated with @RegisterMapper", element);
            return;
        }
        if (element.getKind() != ElementKind.PACKAGE || !(element.getEnclosingElement() instanceof PackageElement parent)) {
            env.messager().printMessage(Diagnostic.Kind.ERROR, "Parent of element annotated with @RegisterMapper is not a package", element);
            return;
        }
        boolean simple = env.subTypeErasure(element.asType(), env.forClass(ValueMapper.class));
        boolean generic = env.subTypeErasure(element.asType(), env.forClass(GenericValueMapper.class));
        if (simple && generic) {
            env.messager().printMessage(Diagnostic.Kind.ERROR, "Can't register a value mapper that is both simple and generic.", element);
            return;
        } else if (!simple && !generic) {
            env.messager().printMessage(Diagnostic.Kind.ERROR, "Can't use @RegisterMapper on class that is no value mapper.", element);
            return;
        }
        ModInit mod = env.getMod(element);
        mod.addConfigMapper(parent.getQualifiedName() + "." + element.getSimpleName(), !typeElem.getTypeParameters().isEmpty());
    }
}
