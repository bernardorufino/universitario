package br.com.bernardorufino.android.universitario.model;

import android.content.Context;
import br.com.bernardorufino.android.universitario.model.base.ModelManager;
import br.com.bernardorufino.android.universitario.model.database.DatabaseHelper;
import com.google.common.base.Throwables;
import com.google.common.collect.MutableClassToInstanceMap;

/* Ensures every ModelManager uses only one database connection */
public class ModelManagerFactory {

    private static DatabaseHelper sDatabaseHelper = null;
    private static MutableClassToInstanceMap<ModelManager<?>> sInstances = MutableClassToInstanceMap.create();

    public static synchronized <T extends ModelManager<?>> T getManager(Context context, Class<T> type) {
        context = context.getApplicationContext();
        T manager = sInstances.getInstance(type);
        if (manager == null) {
            if (sDatabaseHelper == null) sDatabaseHelper = new DatabaseHelper(context);
            manager = newManager(type);
        }
        return manager;
    }

    /* Do not use this method to retrieve model manager in the view/controller layer, just in the model layer */
    public static synchronized <T extends ModelManager<?>> T getManager(Class<T> type) {
        T manager = sInstances.getInstance(type);
        if (manager == null) {
            if (sDatabaseHelper == null) throw new IllegalStateException("Database helper wasn't initialized.");
            manager = newManager(type);
        }
        return manager;
    }

    private static <T extends ModelManager<?>> T newManager(Class<T> type) {
        try {
            T manager = type.newInstance();
            manager.setHelper(sDatabaseHelper);
            sInstances.putInstance(type, manager);
            return manager;
        } catch (InstantiationException | IllegalAccessException e) {
            throw Throwables.propagate(e);
        }
    }

    // Prevents instantiation
    private ModelManagerFactory() {
        throw new AssertionError("Cannot instantiate object from " + this.getClass());
    }
}
