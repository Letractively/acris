package sk.seges.acris.generator.server.rewriterules;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import sk.seges.acris.generator.rpc.domain.GeneratorToken;
import sk.seges.acris.generator.server.processor.ContentInfoProvider;

public abstract class AbstractNiceURLGenerator implements INiceUrlGenerator {

	protected static final String NEW_LINE = System.getProperty("line.separator");

	protected static final Logger log = Logger.getLogger(AbstractNiceURLGenerator.class);

	@Autowired
	@Qualifier("url.redirect.single.file")
	protected Boolean redirectSingleFile;

	@Autowired
	@Qualifier("url.redirect.condition")
	protected Boolean redirectCondition;
	
	@Autowired
	@Qualifier("url.redirect.file.location")
	private String redirectFilePath;

	@Autowired
	@Qualifier("legacy.url.redirect.single.file")
	private Boolean legacyRedirectSingleFile;

	@Autowired
	@Qualifier("legacy.url.redirect.file.location")
	private String legacyRedirectFilePath;

	private ContentInfoProvider contentInfoProvider;
	
//	public void setContentProvidersFactory(ListFactoryBean contentProvidersFactory) {
//	    this.contentProvidersFactory = contentProvidersFactory;
//	    try {
//	        this.contentProviders = (List<ContentProvider>) contentProvidersFactory.getObject();    
//	    } catch (Exception e) {
//	        throw new RuntimeException(e);
//	    }
//	    
//	}

	protected abstract String getDefaultRewriteRule();
	protected abstract String getRewriteRule(String fromURL, String toURL);
	protected abstract String getFinalRewriteRule();

	protected String getRedirectRewriteRule(String fromURL, String toURL) {
		return getRewriteRule(fromURL, toURL);
	}

	protected String getForwardRewriteRule(String fromURL, String toURL) {
		return getRewriteRule(fromURL, toURL);
	}

	protected String getPermanentRedirectRewriteRule(String fromURL, String toURL) {
		return getRewriteRule(fromURL, toURL);
	}

	private boolean generate(GeneratorToken token, Writer writer) {
		
		if (!contentInfoProvider.exists(token)) {
			log.error("Content for niceurl '" + token.getNiceUrl() + "' does not exists.");
			return false;
		}
		
	    String webUrl = "http://" + token.getWebId() + "/";

	    try {
	        writer.write(getRewriteRule(webUrl + "#" + token.getNiceUrl(), webUrl + token.getNiceUrl()));
	    } catch (IOException e) {
	        log.error("Unable to write rewrite rule for niceurl '" + webUrl + "#" + token.getNiceUrl() + "'. Target URL is '" + webUrl + token.getNiceUrl() + "'");
	        return false;
	    }
		/**/
		return true;
	}

	protected BufferedWriter openOrCreateFile(String fileName) {
		File outputFile = new File(fileName);

		if (!outputFile.exists()) {
			try {
				if (!outputFile.createNewFile()) {
					log.error("Unable to create new empty file " + fileName);
					return null;
				}
			} catch (IOException ioe) {
				log.error("Exception occured while creating new empty file " + fileName, ioe);
				return null;
			}
		}

		FileOutputStream outputStream;
		try {
			outputStream = new FileOutputStream(outputFile, redirectSingleFile);
		} catch (FileNotFoundException e) {
			log.error("Exception occured while creating fileOutputStream for file " + fileName, e);
			return null;
		}

		return new BufferedWriter(new OutputStreamWriter(outputStream));
	}

	public void clearRewriteFile(String lang) {
		File file = null;
		
		if (redirectSingleFile) {
			file = new File(redirectFilePath);
			if (file.exists()) {
				file.delete();
			}
		} else {
			file = new File(redirectFilePath + "_" + lang);
			if (file.exists()) {
				file.delete();
			}
		}
	}
	
	public boolean generate(String webId, String niceUrl, String lang) {
		GeneratorToken token = new GeneratorToken();
		token.setWebId(webId);
		token.setNiceUrl(niceUrl);
		token.setLanguage(lang);

		List<String> niceurls = new ArrayList<String>();

	    List<String> availableNiceurls = contentInfoProvider.getAvailableNiceurls(token.getWebId(), token.getLanguage());

	    for(String niceurl : availableNiceurls) {
	    	niceurls.add(niceurl);
	    }

		if (niceurls == null || niceurls.size() == 0) {
			if (log.isInfoEnabled()) {
				log.info("There are no niceurls available in the database");
			}
			return false;
		}

		BufferedWriter writer = null;
		
		boolean newFile = !(new File(redirectFilePath).exists());
		
		if (redirectSingleFile) {
			writer = openOrCreateFile(redirectFilePath);
		} else {
			writer = openOrCreateFile(redirectFilePath + "_" + token.getLanguage());
		}
		
		if (writer == null) {
			if (log.isInfoEnabled()) {
				if (redirectSingleFile) {
					log.info("Unable to create buffered writer (from file '" + redirectFilePath  + "') for saving output");
				} else {
					log.info("Unable to create buffered writer (from file '" + redirectFilePath + "_" + token.getLanguage() + "') for saving output");
				}
			}
			return false;
		}

		if (newFile) {
			final String defaultRule = getDefaultRewriteRule(); 
			
			if (defaultRule != null && defaultRule.length() > 0) {
				try {
					writer.write(defaultRule);
				} catch (IOException ioe) {
					log.error("Unable to write default rewrite rule '" + defaultRule + "'");
					return false;
				}
			}
		
			final BaseURLGenerator baseURLGenerator = new BaseURLGenerator(this);
	
			try {
				writer.write(baseURLGenerator.getBaseURLs(token.getWebId()));
			} catch (IOException e) {
				log.error("Unable to write base rewrite rules");
				return false;
			}
		}
		
		for (final String niceurl : niceurls) {
			if (!generate(token, writer)) {
				if (redirectSingleFile) {
					log.error("Unable to generate and save nice URL for niceurl " + niceurl + ". Output file: "
						+ redirectFilePath + ". Continue with generating nice URL for the next token");
				} else {
					log.error("Unable to generate and save nice URL for niceurl " + niceurl + ". Output file: "
							+ redirectFilePath + "_" + token.getLanguage() + ". Continue with generating nice URL for the next token");
				}
			}
		}

		final LegacyURLGenerator legacyURLGenerator = new LegacyURLGenerator(this, legacyRedirectFilePath, legacyRedirectSingleFile);

		try {
			writer.write(legacyURLGenerator.getLegacyURLs(token.getLanguage()));
		} catch (IOException e) {
			log.error("Unable to write rewrite legacy rules");
			return false;
		}

		//TODO
		final String finalRule = getFinalRewriteRule(); 
		
		if (finalRule != null && finalRule.length() > 0) {
			try {
				writer.write(finalRule);
			} catch (IOException ioe) {
				log.error("Unable to write final rewrite rule '" + finalRule + "'");
				return false;
			}
		}
		
		try {
			writer.flush();
			writer.close();
		} catch (IOException e) {
			if (redirectSingleFile) {
				log.error("Unable to close output writer for file " + redirectFilePath, e);
			} else {
				log.error("Unable to close output writer for file " + redirectFilePath + "_" + token.getLanguage(), e);
			}
		}
		
		return true;
	}
	
	public Boolean getRedirectCondition() {
		return redirectCondition;
	}

	public void setRedirectCondition(Boolean redirectCondition) {
		this.redirectCondition = redirectCondition;
	}
	public ContentInfoProvider getContentInfoProvider() {
		return contentInfoProvider;
	}
	public void setContentInfoProvider(ContentInfoProvider contentInfoProvider) {
		this.contentInfoProvider = contentInfoProvider;
	}
}