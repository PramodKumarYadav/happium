package screens.search;

public interface SearchScreen {
    void setSearch(String contactName);
    String getFirstSearchResultText();
    void tapFirstSearchResult();
    String getPhoneNumber();
    String getDetailName();
    String getEmail();
    String getStreet1();
    String getStreet2();
}
