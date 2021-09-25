package screens.search;

public interface SearchScreen {
    void setSearch(String contactName);
    String getFirstSearchResultText();
    void navigateToFirstSearchResult();
}
