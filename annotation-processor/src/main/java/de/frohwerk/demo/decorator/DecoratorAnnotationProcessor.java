package de.frohwerk.demo.decorator;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

import static de.frohwerk.demo.decorator.Stuff.modelInterfaces;

@AutoService(Processor.class)
public final class DecoratorAnnotationProcessor extends AbstractProcessor {

    private static final Set<String> SUPPORTED_ANNOTATIONS = Set.of(DecoratorBase.class.getName());

    private static final Logger logger = LogManager.getLogger(DecoratorAnnotationProcessor.class);

    private Elements elements;
    private Messager messager;
    private Types types;
    private Filer filer;

    @Override
    public synchronized void init(final ProcessingEnvironment processingEnv) {
        logger.debug("Initializing de.frohwerk.demo.decorator.DecoratorAnnotationProcessor");
        elements = processingEnv.getElementUtils();
        messager = processingEnv.getMessager();
        types = processingEnv.getTypeUtils();
        filer = processingEnv.getFiler();
        super.init(processingEnv);
    }

    @Override
    public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv) {
        logger.debug("Running de.frohwerk.demo.decorator.DecoratorAnnotationProcessor");
        logger.debug("Annotations: {}", annotations);
        logger.debug("RoundEnvironment: {}", roundEnv);

        for (final Element element : roundEnv.getElementsAnnotatedWith(DecoratorBase.class)) {
            logger.debug("{} is annotated with {}", element, annotations);
            logger.debug("Language model types for element: {}", logger.isInfoEnabled() ? modelInterfaces(element) : "");
            final var typeElement = (TypeElement) element;
            logger.debug("{} implements {}", element, typeElement.getInterfaces());

            final var prefix = typeElement.getSimpleName().toString();
            final var packageName = elements.getPackageOf(element).getQualifiedName().toString();

            final var targetType = (DeclaredType) getTargetType(element);
            final var targetElement = (TypeElement) types.asElement(targetType);
            logger.debug("Target type for decorator base class is {}", targetType);

            final var baseClass = TypeSpec
                    .classBuilder(prefix + "Base")
                    .addModifiers(Modifier.PUBLIC)
                    .addSuperinterface(targetType)
                    .addField(delegateField(targetType))
                    .addMethod(constructorSpec(targetType));

            // Implement methods of decorated interface
            addDelegateMethods(baseClass, targetType);

            elements.getAllMembers(targetElement).stream()
                    .filter(member -> member.getKind() == ElementKind.METHOD)
                    .filter(member -> member.getModifiers().contains(Modifier.ABSTRACT))
                    .map(ExecutableElement.class::cast)
                    .forEach(member -> logger.info("Member: {}", member));

            try {
                JavaFile.builder(packageName, baseClass.build()).build().writeTo(filer);
            } catch (final IOException ex) {
                error(ex.getMessage());
            }
        }

        return true;
    }

    private void addDelegateMethods(final TypeSpec.Builder baseClass, final DeclaredType declaredType) {
        logger.info("Adding delegate methods for type {}", declaredType);
        final var targetElement = (TypeElement) declaredType.asElement();

        targetElement.getEnclosedElements().stream()
                .filter(member -> member.getKind() == ElementKind.METHOD)
                .filter(member -> member.getModifiers().contains(Modifier.ABSTRACT))
                .forEach(member -> baseClass.addMethod(delegateMethod(declaredType, (ExecutableElement) member)));

        targetElement.getInterfaces().forEach(t -> addDelegateMethods(baseClass, (DeclaredType) t));
    }

    private static AnnotationMirror getAnnotationMirror(final Element element, final Class<?> annotationClass) {
        for (final AnnotationMirror annotation : element.getAnnotationMirrors()) {
            final var annotationElement = (TypeElement) annotation.getAnnotationType().asElement();
            logger.info("AnnotationMirror name: {}", annotationElement.getQualifiedName());
            if (annotationElement.getQualifiedName().contentEquals(annotationClass.getName())) {
                return annotation;
            }
        }
        return null;
    }

    private static TypeMirror getTargetType(final Element element) {
        final var annotation = getAnnotationMirror(element, DecoratorBase.class);
        if (annotation == null) throw new MissingAnnotationException(DecoratorBase.class);
        if (annotation.getElementValues() == null) return element.asType();
        for (final var attribute : annotation.getElementValues().entrySet()) {
            if (attribute.getKey().getSimpleName().contentEquals("value")) {
                AnnotationValue value = attribute.getValue();
                Object annotationValueValue = value.getValue();
                if (annotationValueValue instanceof TypeMirror) {
                    return (TypeMirror) annotationValueValue;
                }
            }
        }
        return element.asType();
    }

    private MethodSpec constructorSpec(final TypeMirror delegateType) {
        return MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PROTECTED)
                .addParameter(TypeName.get(delegateType), "delegate", Modifier.FINAL)
                .addStatement("this.delegate = delegate")
                .build();
    }

    private FieldSpec delegateField(final TypeMirror type) {
        return FieldSpec.builder(TypeName.get(type), "delegate", Modifier.PRIVATE, Modifier.FINAL).build();
    }

    private MethodSpec delegateMethod(final DeclaredType declaredType, final ExecutableElement element) {
        logger.info("Implementing method: {}", element);
        final var spec = MethodSpec.overriding(element, declaredType, types);
        final var args = element.getParameters().stream()
                .map(VariableElement::getSimpleName)
                .collect(Collectors.joining(", "));
        return element.getReturnType().getKind() == TypeKind.VOID
                ? spec.addStatement("this.delegate.$N($N)", element.getSimpleName(), args).build()
                : spec.addStatement("return this.delegate.$N($N)", element.getSimpleName(), args).build();
    }

    @SuppressWarnings("SameParameterValue")
    private void error(final String msg) {
        messager.printMessage(Diagnostic.Kind.ERROR, msg);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return SUPPORTED_ANNOTATIONS;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

}
