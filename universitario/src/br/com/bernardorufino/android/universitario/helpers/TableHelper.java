package br.com.bernardorufino.android.universitario.helpers;

public class TableHelper {
    
    public static String columnInQuery(String table, String column) {
        return table + "." + column + " as " + columnAlias(table, column);
    }

    public static String columnsInQuery(String table, String... columns) {
        StringBuilder query = new StringBuilder();
        int i = 0, n = columns.length;
        for (String column : columns) {
            query.append(columnInQuery(table, column));
            if (++i < n) query.append(", ");
        }
        return query.toString();
    }
    
    public static String columnAlias(String table, String column) {
        return table + column;
    }

    // Prevents instantiation
    private TableHelper() {
        throw new AssertionError("Cannot instantiate object from " + this.getClass());
    }
}
