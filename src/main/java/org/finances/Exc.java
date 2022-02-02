package org.finances;

public class Exc {

    static double ACCYRACY_LIMIT = 0.0000000000001;


    public static double getPi() {  
        double a = 1.0;
        double b = 1.0 / Math.sqrt(2.0); 
        double t = 1.0 / 4;
        double p = 1.0;
        System.out.println("a = " + a + ", b= " +b + ", t= " +t);

        while (Math.abs(b - a) > ACCYRACY_LIMIT) {

            System.out.println("a = " + a + ", b= " +b + ", t= " +t);
            double na = (a + b)/2.0;
            double nb = Math.pow(a*b, 1.0/2);
            double nt = t - p * Math.pow((a - na), 2.0);
            double np = 2.0 *p;
            System.out.println("na = " + a + ", nb= " +b + ", nt= " +t);

            a = na;
            b = nb;
            t = nt;
            p = np;
            System.out.println(Math.pow(a + b, 2.0)/ (4.0 * t));

        }
        return Math.pow(a + b, 2.0)/ (4.0 * t);
    }
}
