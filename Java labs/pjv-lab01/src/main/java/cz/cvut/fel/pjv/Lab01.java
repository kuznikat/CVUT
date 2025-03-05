package cz.cvut.fel.pjv;

import java.util.Scanner;

public class Lab01 {
   public void homework() {

      //public static void main(String[] args){
         Scanner scan = new Scanner(System.in);
         System.out.println("Vyber operaci (1-soucet, 2-rozdil, 3-soucin, 4-podil):");
         int operation = scan.nextInt();
         switch (operation){
            case 1 :
               System.out.println("Zadej scitanec: ");
               double x = scan.nextDouble();
               System.out.println("Zadej scitanec: ");
               double y = scan.nextDouble();
               System.out.println("Zadej pocet desetinnych mist: ");
               int length1 = scan.nextInt();
               if (length1 < 0) {
                  System.out.println("Chyba - musi byt zadane kladne cislo!");
                  break;
               }
               double result1 = x + y;
               System.out.printf("%." + length1 + "f + %." + length1 + "f = %." + length1 + "f\n", x, y, result1);
               break;
            case 2:
               System.out.println("Zadej mensenec: ");
               double a = scan.nextDouble();
               System.out.println("Zadej mensitel: ");
               double b = scan.nextDouble();
               System.out.println("Zadej pocet desetinnych mist: ");
               int length2 = scan.nextInt();
               if (length2 < 0) {
                  System.out.println("Chyba - musi byt zadane kladne cislo!");
                  break;
               }
               double result2 = a - b;
               System.out.printf("%." + length2 + "f - %." + length2 + "f = %." + length2 + "f\n", a, b, result2);
               break;
            case 3:
               System.out.println("Zadej cinitel: ");
               double d = scan.nextDouble();
               System.out.println("Zadej cinitel: ");
               double e = scan.nextDouble();
               System.out.println("Zadej pocet desetinnych mist: ");
               int length3 = scan.nextInt();
               if (length3 < 0) {
                  System.out.println("Chyba - musi byt zadane kladne cislo!");
                  break;
               }
               double result3 = d * e;
               System.out.printf("%." + length3 + "f * %." + length3 + "f = %." + length3 + "f\n",d, e, result3);
               break;
            case 4:
               System.out.println("Zadej delenec: ");
               double g = scan.nextDouble();
               System.out.println("Zadej delitel: ");
               double h = scan.nextDouble();
               if ( h == 0) {
                  System.out.println("Pokus o deleni nulou!");
                  break;
               }
               System.out.println("Zadej pocet desetinnych mist: ");
               int length4 = scan.nextInt();
               if (length4 < 0) {
                  System.out.println("Chyba - musi byt zadane kladne cislo!");
                  break;
               }
               double result4 = g / h;
               System.out.printf("%." + length4 + "f / %." + length4 + "f = %." + length4 + "f\n",g, h, result4);
               break;
            default:
               System.out.println("Chybna volba!");
         }

      }
}


