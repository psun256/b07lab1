import java.io.*;
import java.util.*;

public class Polynomial {
    static double EPS = 1e-10;

    double [] c;
    int [] e;

    public Polynomial() {
        this.c = null;
        this.e = null;
    }

    public Polynomial(double [] c, int [] e) {
        if (c == null || c.length == 0) return;
        
        double [] c1 = c.clone();
        int [] e1 = e.clone();

        int z = removeZero(c1.length, c1, e1);
        this.c = Arrays.copyOfRange(c1, 0, c1.length-z);
        this.e = Arrays.copyOfRange(e1, 0, e1.length-z);
    }
    
    // s is valid string by contract
    public static String parse(String s) {
        // in order:
        // "adding negatives"; 5-x -> 5+-x
        // implicit coefficient; x -> 1x
        // implicit exponent;   5x -> 5x1
        // constants;            5 -> 5x0
        s = s.replaceAll("(.)(-)", "$1+-")
             .replaceAll("([+\\-]|^)x", "$11x")
             .replaceAll("x([+\\-]|$)", "x1$1")
             .replaceAll("(?<!x)([\\d.]+)([+\\-]|$)", "$1x0$2");
        return s;
    }
    
    private void strInit(String s) {
        String [] tokens = Polynomial.parse(s).split("\\+");
        int l = tokens.length;

        double [] c1 = new double[l];
        int [] e1 = new int[l];
        Arrays.fill(e1, -1); // impossible coefficient for counting
        int u = 0;

        for (int i = 0; i < l; i++) {
            String [] term = tokens[i].split("x");
            double cx = Double.parseDouble(term[0]);
            int ex = Integer.parseInt(term[1]);
            
            int ind = hasPower(ex, e1);
            if (ind == -1) ind = u++;

            c1[ind] += cx;
            e1[ind] = ex;
        }

        int z = removeZero(u, c1, e1);
        
        this.c = Arrays.copyOfRange(c1, 0, u-z);
        this.e = Arrays.copyOfRange(e1, 0, u-z);
    }

    
    public Polynomial(String s) {
        strInit(s);
    }
    
    public Polynomial(File f) throws IOException {
        Scanner sc = new Scanner(f);
        strInit(sc.nextLine());
        sc.close();
    }

    // nm lol ¯\_(ツ)_/¯
    public Polynomial add(Polynomial p) {
        if (this.len() == 0) return new Polynomial(p.c, p.e);
        if (p.len() == 0) return new Polynomial(this.c, this.e);

        int l = this.len() + p.len();
        double [] c1 = new double[l];
        int [] e1 = new int[l];
        Arrays.fill(e1, -1); // impossible exponent for counting purposes
        int u = 0;
      
        u = addArr(u, this.c, this.e, c1, e1);
        u = addArr(u, p.c, p.e, c1, e1);

        int z = removeZero(u, c1, e1);
        
        return new Polynomial(
                Arrays.copyOfRange(c1, 0, u-z),
                Arrays.copyOfRange(e1, 0, u-z));
    }

    // n^2m^2 lol ¯\_(ツ)_/¯
    public Polynomial multiply(Polynomial p) {
        if (this.len() == 0 || p.len() == 0) return new Polynomial();
        
        Polynomial res = new Polynomial();
        
        for (int i = 0; i < this.len(); i++) {
            double [] c1 = p.c.clone();
            int [] e1 = p.e.clone();

            for (int j = 0; j < p.len(); j++) {
                c1[j] *= this.c[i];
                e1[j] += this.e[i];
            }

            res = res.add(new Polynomial(c1, e1));
        }

        return res;
    }

    public double evaluate(double x) {
        if (this.len() == 0) return 0;
        
        double res = 0;
        for (int i = 0; i < this.len(); i++) {
            res += this.c[i] * Math.pow(x, this.e[i]);
        }
        return res;
    }

    public boolean hasRoot(double x) {
        return Math.abs(this.evaluate(x)) < 1e-9;
    }

    public void saveToFile(String name) throws IOException {
        FileWriter f = new FileWriter(name);
        f.write(this.toString());
        f.close();
    }

    public int len() {
        if (this.c == null) return 0;
        return this.c.length;
    }

    private static int hasPower(int x, int [] e) {
        for (int i = 0; i < e.length; i++)
            if (e[i] == x) return i;
        return -1;
    }

    private static int addArr(
                int unique,
                double [] c, int [] e,
                double [] c1, int [] e1) {
        for (int i = 0; i < c.length; i++) {
            double cx = c[i];
            int ex = e[i];

            int ind = hasPower(ex, e1);
            if (ind == -1) ind = unique++;

            c1[ind] += cx;
            e1[ind] = ex;
        }

        return unique;
    }

    private static int removeZero(int n, double [] c, int [] e) {
        int z = 0;
        for (int i = 0; i < n; i++) {
            if (Math.abs(c[i]) < EPS) z++;
            else {
                c[i-z] = c[i];
                e[i-z] = e[i];
            }
        }
        return z;
    }

    public String toString() {
        if (this.len() == 0) return "0";
        
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < this.len(); i++) {
            if (i > 0 && this.c[i] > 0.0)
                res.append("+");
            res.append(this.c[i]);
            
            if (this.e[i] > 0) {
                res.append("x");
                if (this.e[i] > 1)
                    res.append(this.e[i]);
            }
        }

        return res.toString();
    }
}
