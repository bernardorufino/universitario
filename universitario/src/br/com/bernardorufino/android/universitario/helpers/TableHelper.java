package br.com.bernardorufino.android.universitario.helpers;

public class TableHelper {
    
    public static String columnInQuery(String table, String column) {
        return table + "." + column + " as " + columnAlias(table, column);
    }
    
    public static String columnAlias(String table, String column) {
        return table + column;
    }

    // Prevents instantiation
    private TableHelper() {
        throw new AssertionError("Cannot instantiate object from " + this.getClass());
    }
}
