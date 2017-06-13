public class Test {

    public static void main(String[] args) throws ClassNotFoundException {
        A a = new A();
        a.setTest("Bite");

        B b = new B(a, true);
    }

    public static void print(Object o) {
        System.out.println(o);
    }

    public static class A {

        private String test;

        public String getTest() {
            return test;
        }

        public void setTest(String t) {
            test = t;
        }
    }

    public static class B extends A {

        public B(A a) {
            super();
            super.setTest(a.getTest());
        }

        public B(A a, boolean b) {
            getClass().cast(a);
        }
    }
}
