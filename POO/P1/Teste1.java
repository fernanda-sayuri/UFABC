import java.util.Scanner;

//classe deve ter o mesmo nome do arquivo
public class Teste1 {
  public static void main(String[] args) {
    Scanner leitor = new Scanner(System.in);
    int a = leitor.nextInt();
    System.out.println(a);

    //instanciando vetor
    int[] vet;
    vet = new int[8];

    double vetor[] = new double[5];
    for (int i=0; i<5; i++)
      //populando vetor a partir do teclado
      vetor[i] = leitor.nextDouble();
    
    double soma = 0;

    for (int i=0; i<5; i++)
      soma += vetor[i];

    System.out.println(soma);

    ////////////// MATRIZES
    int[][] matriz;
    matriz = new int[4][3];

    double[][] matriz2 = new double[4][3];

    public static void imprimirMatriz(int[][] matriz) {
          int nLinhas = matriz.length;
          int nColunas = matriz[0].length;

          for (int i = 0; i < nLinhas; i++) {
              for (int j = 0; j < nColunas; j++) {
                  System.out.print(matriz[i][j]);

                  if (j < nColunas - 1) {
                      System.out.print(" ");
                  }
              }
              System.out.println();
          }
      }
    }
  }
  