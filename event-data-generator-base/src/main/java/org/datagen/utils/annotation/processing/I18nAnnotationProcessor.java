package org.datagen.utils.annotation.processing;

import java.io.IOException;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import javax.tools.Diagnostic.Kind;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

import org.datagen.config.I18n;
import org.datagen.config.I18nCatalog;

@SupportedAnnotationTypes({ I18nAnnotationProcessor.I18N_CATALOG_ANNOTATION_CLASSNAME })
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class I18nAnnotationProcessor extends AbstractProcessor {

	private static final Diagnostic.Kind MESSAGE_KIND = Kind.MANDATORY_WARNING;
	protected static final String I18N_CATALOG_ANNOTATION_CLASSNAME = "org.datagen.config.I18nCatalog";

	private static final String I18N_PROPERTY_LINE_PATTERN = "{0} = {1}\n";
	private static final String I18N_PROPERTY_LINE_PLURAL_PATTERN = "{0}__PLURAL = {1}\n";

	public I18nAnnotationProcessor() {
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		Messager messager = processingEnv.getMessager();
		Filer filer = processingEnv.getFiler();
		Elements utils = processingEnv.getElementUtils();

		messager.printMessage(MESSAGE_KIND, "Processing i18n catalogs...");

		TypeElement catalogElement = utils.getTypeElement(I18N_CATALOG_ANNOTATION_CLASSNAME);

		Set<? extends Element> catalogs = roundEnv.getElementsAnnotatedWith(catalogElement);

		for (Element catalog : catalogs) {
			if (!(catalog instanceof TypeElement)) {
				continue;
			}

			try {
				createI18nProperties(filer, messager, (TypeElement) catalog);
			} catch (IOException e) {
				messager.printMessage(Kind.ERROR,
						"Could not create i18n catalog file " + catalog.getAnnotation(I18nCatalog.class).catalog()
								+ ": " + e, catalog);
			}
		}

		return true;
	}

	private static void createI18nProperties(Filer filer, Messager messager, TypeElement type) throws IOException {
		I18nCatalog annotation = type.getAnnotation(I18nCatalog.class);
		messager.printMessage(MESSAGE_KIND, "Processing i18n catalog " + annotation.catalog(), type);

		String fqn = type.getQualifiedName().toString();
		int separator = fqn.lastIndexOf('.');
		FileObject file = filer.createResource(StandardLocation.CLASS_OUTPUT,
				separator == -1 ? "" : fqn.substring(0, separator), annotation.catalog() + ".properties", type);

		try (OutputStream os = file.openOutputStream()) {
			for (Element enclosed : type.getEnclosedElements()) {
				I18n i18n = enclosed.getAnnotation(I18n.class);
				if (enclosed.getKind() == ElementKind.ENUM_CONSTANT) {
					String line = MessageFormat.format(I18N_PROPERTY_LINE_PATTERN, i18n.key().isEmpty() ? enclosed
							.getSimpleName().toString() : i18n.key(), i18n.value());
					os.write(line.getBytes());

					if (!i18n.plural().isEmpty()) {
						String plural = MessageFormat.format(I18N_PROPERTY_LINE_PLURAL_PATTERN,
								i18n.key().isEmpty() ? enclosed.getSimpleName().toString() : i18n.key(), i18n.plural());
						os.write(plural.getBytes());
					}
				}
			}

			os.close();
		}
	}
}
