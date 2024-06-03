import java.io.*;
import java.util.*;

public class Driver {
    public static void main(String [] args) throws IOException {
        int [] a = new int[5];
        int [] b = Arrays.copyOfRange(a, 0, 0);
        
        Polynomial p = new Polynomial(new double[]{1, 2, 3}, new int[]{1, 2, 3});
        Polynomial q = new Polynomial(new double[]{1, 2, 3}, new int[]{1, 2, 3});
        Polynomial r = p.multiply(q);
        Polynomial r1 = p.add(q);

        System.out.println(r);
        System.out.println(r1);
        System.out.println(Polynomial.parse("x"));
        System.out.println(Polynomial.parse("1"));
        System.out.println(Polynomial.parse("0"));
        System.out.println(Polynomial.parse(Polynomial.parse("5-3x2+7x8")));
        Polynomial adsf = new Polynomial("5-3x2+7x8");
        System.out.println(adsf);
        adsf.saveToFile("amogus.txt");
        File ff = new File("amogus.txt");
        
        Polynomial asdf = new Polynomial(ff);
        Polynomial amogus = new Polynomial("-5");
        asdf = asdf.add(amogus);
        System.out.println(asdf);
    }
}
