/**
 * 
 */
package sk.seges.corpis.ie.server.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sk.seges.corpis.ie.server.domain.CsvEntry;
import sk.seges.corpis.ie.server.domain.RowBasedHandlerContext;
import sk.seges.corpis.ie.shared.domain.ImportExportViolation;
import sk.seges.corpis.ie.shared.domain.ViolationConstants;
import au.com.bytecode.opencsv.bean.CsvToBean;
import au.com.bytecode.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;

/**
 * @author ladislav.gazo
 */
public abstract class CSVImportExportService {
	private static final String ENTRY_MAPPING_METHOD = "getMapping";
	private static final Logger log = LoggerFactory.getLogger(CSVImportExportService.class);
	
	private static final String CUSTOM_SUFFIX = "_CUSTOM";

	protected Map<String, CSVHandler<?, ?>> handlerMapping;
	private final CsvEntryMappingLoader mappingLoader;

	protected abstract String detectFormat();

	protected abstract String getDestination(RowBasedHandlerContext contextTemplate);

	protected abstract RowBasedHandlerContext instantiateContext();

	public CSVImportExportService(Map<String, CSVHandler<?, ?>> handlerMapping, CsvEntryMappingLoader mappingLoader) {
		super();
		this.handlerMapping = handlerMapping;
		this.mappingLoader = mappingLoader;
	}

	@SuppressWarnings("unchecked")
	public List<ImportExportViolation> importCSV(RowBasedHandlerContext contextTemplate, Set<String> fieldNames) {
		List<ImportExportViolation> violations = new ArrayList<ImportExportViolation>();

		
		String format = detectFormat();
		
		CSVHandler handler = handlerMapping.get(format);

		Class handledCsvEntryClass = handler.getHandledCsvEntryClass();

		Map<String, String> fieldToColumnMapping = mappingLoader.loadFieldToColumnMapping(handledCsvEntryClass);
		handler.setFieldToColumnMapping(fieldToColumnMapping);
		
		String file = getDestination(contextTemplate);
		
		
		List<CsvEntry> entries  = null;
		
		if (format.toUpperCase().endsWith(CUSTOM_SUFFIX)) {
			entries = readCustomEntries(file, violations);
		} else {
			entries = readCsvEntries(file, handledCsvEntryClass, violations);
		}
		
		if (!violations.isEmpty()) {
			return violations;
		}
		
		// one for header and one for not starting at 0
		int i = 2;
		for (CsvEntry entry : entries) {
			RowBasedHandlerContext newContext = instantiateContext();
			contextTemplate.injectInto(newContext);
			newContext.setRow(i);
			violations.addAll(handler.handle(newContext, entry, fieldNames));
			i++;
		}

		return violations;
	}
	
	private List<CsvEntry> readCsvEntries(String file, Class handledCsvEntryClass, List<ImportExportViolation> violations) {
		List<CsvEntry> entries = null;
		
		CsvToBean csv = new CsvToBean();                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     
		HeaderColumnNameTranslateMappingStrategy strat = new HeaderColumnNameTranslateMappingStrategy();
		strat.setType(handledCsvEntryClass);
		strat.setColumnMapping(mappingLoader.loadMapping(handledCsvEntryClass));
		
		try {
//			Reader in = new BufferedReader(new FileReader(file));
			Reader in = new InputStreamReader(new FileInputStream(file), "UTF-8");
			entries = csv.parse(strat, in, ',');
		} catch (FileNotFoundException e) {
			log.warn("CSV File not found = " + file, e);
			violations.add(new ImportExportViolation(ViolationConstants.FILE_NOT_FOUND, file));
		} catch (UnsupportedEncodingException e) {
			log.warn("Unsupported encoding = " + file, e);
			violations.add(new ImportExportViolation(ViolationConstants.UNSUPPORTED_ENCODING, file));
		}
		return entries;
	}
	
	
	/**
	 * Dummy method, should be overridden for custom import 
	 * 
	 * @param file
	 * @param violations
	 * @return
	 */
	protected List<CsvEntry> readCustomEntries(String file, List<ImportExportViolation> violations) {
		return null;
	}
	
	
	
	
}