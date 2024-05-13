import java.lang.Math;

public class Polynomial {
    double [] c;
    public Polynomial() {
        this.c = new double[0];
    }
    public Polynomial(double [] c) {
        this.c = c.clone();
    }
    public Polynomial add(Polynomial p) {
        double [] c1 = p.c;
        double [] c2 = new double[Math.max(this.c.length, c1.length)];
        for (int i = 0; i < c2.length; i++) {
            if (i < this.c.length) c2[i] += this.c[i];
            if (i < c1.length) c2[i] += c1[i];
        }
        return new Polynomial(c2);
    }
    public double evaluate(double x) {
        double pox = 1;
        double acc = 0;
        for (int i = 0; i < this.c.length; i++) {
            acc += pox * this.c[i];
            pox *= x;
        }
        return acc;
    }
    public boolean hasRoot(double x) {
        return Math.abs(evaluate(x)) < 1e-9;
    }
}
