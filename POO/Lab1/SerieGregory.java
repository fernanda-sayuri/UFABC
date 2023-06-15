import java.util.Scanner;

public class SerieGregory {
    public static void main(String[] args) {
        Scanner leitor = new Scanner(System.in);
        int numeroIteracoes = leitor.nextInt();
        double pi = calcularPi(numeroIteracoes);
        System.out.println("\n" + String.format("%.5f", pi));
    }

    public static double calcularPi(int n) {
        double pi = 0;
        for (int i = 0; i < n; i++) {
            double termo = 1.0 / (2 * i + 1);
            if (i % 2 == 0) {
                pi += termo;
                if(i<50){
                    System.out.print(String.format("%.5f", termo));
                }
            } else {
                pi -= termo;
                if(i<50){
                    System.out.print(String.format("%.5f", termo*-1));
                }
            }
            System.out.print(" ");
        }
        pi *= 4;
        return pi;
    }
}