package br.com.bernardorufino.android.universitario.model.util;

import br.com.bernardorufino.android.universitario.model.base.Model;

import java.util.*;

import static com.google.common.base.Preconditions.checkState;

public class ModelUtils {

    public static <M extends Model> ModelsDelta<M> delta(Collection<M> oldModels, Collection<M> freshModels) {
        Map<Integer, M> idMapBefore = getIdMap(oldModels);
        Map<Integer, M> idMapAfter = getIdMap(freshModels);
        ModelsDelta<M> delta = new ModelsDelta<>(oldModels, freshModels);
        for (M model : oldModels) {
            int id = model.getId();
            if (idMapAfter.containsKey(id)) {
                M fresh = idMapAfter.get(id);
                (model.equals(fresh) ? delta.unchanged : delta.updated).add(fresh);
            } else {
                delta.deleted.add(model);
            }
        }
        for (M model : freshModels) {
            int id = model.getId();
            if (!idMapBefore.containsKey(id)) {
                delta.created.add(model);
            }
        }
        return delta;
    }

    public static <M extends Model> Map<Integer, M> getIdMap(Collection<M> models) {
        Map<Integer, M> map = new HashMap<>();
        for (M model : models) {
            map.put(model.getId(), model);
        }
        return map;
    }

    public static class ModelsDelta<M extends Model> {

        public final Collection<M> oldModels;
        public final Collection<M> freshModels;
        public final Collection<M> created = new HashSet<>();
        public final Collection<M> deleted = new HashSet<>();
        public final Collection<M> updated = new HashSet<>();
        public final Collection<M> unchanged = new HashSet<>();

        public ModelsDelta(Collection<M> oldModels, Collection<M> freshModels) {
            this.oldModels = oldModels;
            this.freshModels = freshModels;
        }

        public boolean hasChanges() {
            boolean hasChanges = created.size() > 0 || deleted.size() > 0 || updated.size() > 0;
            if (hasChanges) {
                checkState(oldModels.size() + created.size() - deleted.size() == freshModels.size());
            }
            return hasChanges;
        }
    }
}
