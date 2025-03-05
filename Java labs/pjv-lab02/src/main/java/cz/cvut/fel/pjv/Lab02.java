/*
 * File name: Lab06.java
 * Date:      2014/08/26 21:39
 * Author:    @author
 */

package cz.cvut.fel.pjv;

public class Lab02 {
   public static void homework() {
      TextIO textIO = new TextIO();
      int numberOfLine = 1; // to store a current line number
      int numbersCounter = 0; //count numbers
      double numbersSum = 0.0; //sum of all numbers for prumer
      double sumOfSquares = 0; //sum of squares odhylek for counting sm.odch.

      while (true) {
         String line = textIO.getLine();
         if (line.isEmpty()) {
            break; // it will stop for this current line, but will continue to read soubor
         }
         if (TextIO.isDouble(line)) { //checks if the string line can be parsed into double
            double lineValue = Double.parseDouble(line); // here i convert line into double
            numbersCounter++; //add new value => increase counter
            numbersSum += lineValue; // add value to sum
            sumOfSquares += lineValue * lineValue; // add squares of value to the whole sum of squares
         } else {
            System.err.println("A number has not been parsed from line " + numberOfLine);
         }
         numberOfLine++; //increase line number
         if (numbersCounter == 10) {
            printResult(numbersCounter, numbersSum, sumOfSquares);
            numbersCounter = 0;
            numbersSum = 0.0;
            sumOfSquares = 0.0;
         }

      }
      if (numbersCounter > 1) {
         printResult(numbersCounter, numbersSum, sumOfSquares);
      }
      System.err.println("End of input detected!");
   }

      public static void printResult (int numbersCounter, double numbersSum, double sumOfSquares) {
         double averageValue = numbersSum / numbersCounter; //counting average
         double standardDeviation = Math.sqrt(sumOfSquares / numbersCounter - (averageValue * averageValue));
         System.out.printf("%2d %.3f %.3f\n", numbersCounter, averageValue, standardDeviation);
      }
}
