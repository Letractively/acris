package sk.seges.acris.generator.client.context.api;

import java.util.Collection;
import java.util.Iterator;

import sk.seges.acris.generator.shared.domain.GeneratorToken;

public interface TokensCache extends Iterator<GeneratorToken> {

	int getWaitingTokensCount();

	int getTokensCount();

	void addTokens(Collection<String> tokens);

	GeneratorToken getCurrentToken();

	void setDefaultToken(GeneratorToken generatorToken);

	GeneratorToken getDefaultToken();

	void setDefaultLocale(String locale);
}