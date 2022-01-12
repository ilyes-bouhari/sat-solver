package utils;

public class ComboBoxItem {
    private String id;
    private String value;

    public ComboBoxItem(String id, String value) {
        this.id = id;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
