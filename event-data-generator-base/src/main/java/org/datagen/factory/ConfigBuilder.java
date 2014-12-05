package org.datagen.factory;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import org.datagen.utils.TypeBoxing;
import org.w3c.dom.Element;

public class ConfigBuilder<P extends BuilderParameter<?>> implements Config<P>,
		GenericBuilder<P, Config<P>, ConfigBuilder<P>> {

	private final Map<P, Optional<?>> parameters = new HashMap<>();

	public ConfigBuilder() {
	}

	@Override
	public Config<P> build() {
		return this;
	}

	@Override
	public <C extends Serializable> ConfigBuilder<P> set(@Nonnull P parameter, Optional<C> value) {
		parameters.put(parameter, value);

		return this;
	}

	@Override
	public <C extends Serializable> ConfigBuilder<P> set(@Nonnull P parameter, C value) {
		return set(parameter, Optional.<C> of(value));
	}

	@Override
	public <C extends Serializable> ConfigBuilder<P> unset(@Nonnull P parameter) {
		parameters.remove(parameter);

		return this;
	}

	@Override
	public <C extends Serializable> ConfigBuilder<P> enable(@Nonnull P parameter) {
		if (parameter.getType() != boolean.class) {
			throw new IllegalArgumentException();
		}

		return this.set(parameter, Optional.<Boolean> of(true));
	}

	@Override
	public <C extends Serializable> ConfigBuilder<P> disable(@Nonnull P parameter) {
		if (parameter.getType() != boolean.class) {
			throw new IllegalArgumentException();
		}

		return this.set(parameter, Optional.<Boolean> of(false));
	}

	@SuppressWarnings("unchecked")
	@Override
	public <C extends Serializable> Optional<C> get(@Nonnull P parameter) {
		return (parameters.containsKey(parameter)) ? (Optional<C>) parameters.get(parameter) : (Optional<C>) parameter
				.getDefaultValue();
	}

	@Override
	public Set<P> getKeys() {
		return parameters.keySet();
	}

	@Override
	public boolean isEnabled(@Nonnull P parameter) {
		if (parameter.getType() != boolean.class) {
			throw new IllegalArgumentException();
		}

		return this.<Boolean> get(parameter).orElseThrow(IllegalArgumentException::new);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <C extends Serializable> ConfigBuilder<P> setXml(@Nonnull Collection<Element> elements,
			@Nonnull Class<P> clazz) {
		if (!clazz.isEnum()) {
			throw new IllegalArgumentException(MessageFormat.format(
					"Class [ {0} ] is expected to be an enum to initialize configuration from XML properties",
					clazz.getName()));
		}

		elements.stream()
				.forEach(
						e -> {
							P parameter = (P) Enum.valueOf((Class<Enum>) clazz,
									e.getTagName().toUpperCase().replace("-", "_"));
							Serializable value = (parameter.getType().isPrimitive() ? TypeBoxing.box(
									parameter.getType(), e.getNodeValue()) : e.getNodeValue());
							set(parameter, Optional.of(value));
						});

		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String toString() {
		return this.parameters.entrySet().stream()
				.map(x -> x.getKey() + "=" + ((Optional<Object>) x.getValue()).orElse("<absent>"))
				.collect(Collectors.joining("; ", "{ ", " }"));
	}
}
