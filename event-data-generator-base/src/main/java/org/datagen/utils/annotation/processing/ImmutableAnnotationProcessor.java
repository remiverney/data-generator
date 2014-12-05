package org.datagen.utils.annotation.processing;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import javax.tools.Diagnostic.Kind;

@SupportedAnnotationTypes({ ImmutableAnnotationProcessor.IMMUTABLE_ANNOTATION_CLASSNAME })
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class ImmutableAnnotationProcessor extends AbstractProcessor {

	private static final Diagnostic.Kind MESSAGE_KIND = Kind.ERROR;
	protected static final String IMMUTABLE_ANNOTATION_CLASSNAME = "org.datagen.utils.annotation.Immutable";

	public ImmutableAnnotationProcessor() {
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		Messager messager = processingEnv.getMessager();
		Elements utils = processingEnv.getElementUtils();

		TypeElement catalogElement = utils.getTypeElement(IMMUTABLE_ANNOTATION_CLASSNAME);

		Set<? extends Element> catalogs = roundEnv.getElementsAnnotatedWith(catalogElement);

		for (Element catalog : catalogs) {
			if (!(catalog instanceof TypeElement)) {
				continue;
			}

			checkClass(messager, (TypeElement) catalog);
		}

		return true;
	}

	private static void checkClass(Messager messager, TypeElement type) {
		for (Element enclosed : type.getEnclosedElements()) {
			if (enclosed.getKind() == ElementKind.FIELD) {
				Set<Modifier> modifiers = enclosed.getModifiers();

				if (modifiers.contains(Modifier.STATIC)) {
					continue;
				}

				if ((!modifiers.contains(Modifier.FINAL))
						|| ((!modifiers.contains(Modifier.PRIVATE)) && (!modifiers.contains(Modifier.PROTECTED)))) {
					messager.printMessage(MESSAGE_KIND,
							"Type annotated as immutable, but found a non-static, non-private/protected or non-final field: "
									+ enclosed.getSimpleName(), type);
				}
			}
		}
	}
}
