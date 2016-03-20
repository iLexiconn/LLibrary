package net.ilexiconn.llibrary.server.util;

public class Tuple3<A, B, C> {
    private A a;
    private B b;
    private C c;

    public Tuple3() {
        this(null, null, null);
    }

    public Tuple3(A a, B b, C c) {
        set(a, b, c);
    }

    public A getA() {
        return a;
    }

    public void setA(A a) {
        this.a = a;
    }

    public B getB() {
        return b;
    }

    public void setB(B b) {
        this.b = b;
    }

    public C getC() {
        return c;
    }

    public void setC(C c) {
        this.c = c;
    }

    public void set(A a, B b, C c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }
}

