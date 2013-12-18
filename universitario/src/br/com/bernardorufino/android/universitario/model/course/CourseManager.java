package br.com.bernardorufino.android.universitario.model.course;

public class CourseManager {

    private static class InstanceHolder {
        private static final CourseManager INSTANCE = new CourseManager();
    }

    public static CourseManager getInstance() {
        return InstanceHolder.INSTANCE;
    }

    // Prevents outside instantiation
    private CourseManager() {

    }


}
