package br.com.bernardorufino.android.universitario;

import java.util.Arrays;
import java.util.List;

public class Test {

    public static class Foo {
        private String name;

        public Foo(String name) {
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Foo)) return false;

            Foo foo = (Foo) o;

            if (name != null ? !name.equals(foo.name) : foo.name != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return name != null ? name.hashCode() : 0;
        }
    }

    public static void main(String[] args) {
//        NumberFormat f = new DecimalFormat("#.##", new DecimalFormatSymbols(Locale.US));
//        double[] values = { 17, 18.5, 18.23, 24.2103, 90.00, 100.1, 100.3, 0.5, 1, 1.5, 1.25, 2.4, 0, 2, 4, 1.2345 };
//        for (double value : values) {
//            System.out.println(value + " = " + f.format(value));
//        }
        System.out.println(new Foo("a").equals(new Foo("a")));
        List<Foo> a = Arrays.asList(new Foo("a"), new Foo("b"));
        List<Foo> b = Arrays.asList(new Foo("a"), new Foo("b"));
        System.out.println(a.equals(b));
    }
}
