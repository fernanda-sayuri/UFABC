import java.util.Scanner;

public class TestesDiscosVoadores {

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    //System.out.print("Digite o número de linhas da matriz: ");
    int linhas = scanner.nextInt();

    //System.out.print("Digite o número de colunas da matriz: ");
    int colunas = scanner.nextInt();

    int[][] matriz = new int[linhas][colunas];

    //System.out.println("Digite os elementos da matriz:"); 
    String strVet = leitor.next();
      String[] textoSeparado = strVet.split(" ");
      for (int i = 0; i < textoSeparado.length; i++) {
        System.out.println(textoSeparado[i]);
      }
      
  }
    
}