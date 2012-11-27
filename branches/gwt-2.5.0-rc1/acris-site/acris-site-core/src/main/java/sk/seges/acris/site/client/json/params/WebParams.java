package sk.seges.acris.site.client.json.params;

import sk.seges.acris.domain.params.ContentParameters;

public interface WebParams extends ContentParameters {

	public static final String OFFLINE_POST_PROCESSOR_INACTIVE = "offlinePostProcessorInactive";
	public static final String OFFLINE_INDEX_PROCESSOR_INACTIVE = "offlineIndexProcessorInactive";
	public static final String OFFLINE_AUTODETECT_MODE = "offlineAutodetectMode";
	public static final String PUBLISH_ON_SAVE_ENABLED = "publishOnSaveEnabled";
	public static final String PRODUCT_CATEGORY_SINGLE_SELECT = "productCategorySingleSelect";
	public static final String PRODUCT_LIST_FILTERS_ENABLED = "productListFilterEnabled";
	public static final String PRODUCT_LIST_SORT_ENABLED = "productListSortEnabled";
	public static final String SEARCH_MODE = "searchMode";
	public static final String SEARCH_LOCALE_PREFIX = "searchLocalePrefix";
	public static final String BLUEWAVE_URL = "bluewaveUrl";
	public static final String BLUEWAVE_USERNAME = "bluewaveUsername";
	public static final String BLUEWAVE_PASSWORD = "bluewavePassword";

	String[] getOfflinePostProcessorInactive();

	void setOfflinePostProcessorInactive(String[] processors);

	String[] getOfflineIndexProcessorInactive();

	void setOfflineIndexProcessorInactive(String[] processors);

	Boolean isOfflineAutodetectMode();

	void setOfflineAutodetectMode(boolean mode);

	Boolean isPublishOnSaveEnabled();

	void setPublishOnSaveEnabled(boolean publishOnSaveEnabled);

	Boolean isProductCategorySingleSelect();

	void setProductCategorySingleSelect(boolean productCategorySingleSelect);

	Boolean isFiltersEnabled();

	void setFiltersEnabled(boolean filtersEnabled);

	Boolean isSortEnabled();

	void setSortEnabled(boolean sortEnabled);

	String getSearchMode();

	void setSearchMode(String mode);

	Boolean isSearchLocalePrefix();

	void setSearchLocalePrefix(boolean prefix);

	String getBluewaveUrl();

	void setBluewaveUrl(String bluewaveUrl);

	String getBluewaveUsername();

	void setBluewaveUsername(String bluewaveUsername);

	String getBluewavePassword();

	void setBluewavePassword(String bluewavePassword);
}