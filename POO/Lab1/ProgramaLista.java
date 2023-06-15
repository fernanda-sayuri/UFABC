import java.util.Scanner;

public class ProgramaLista {
    
    public static boolean inserir(int numero, int[] v) {
        return false;
    }
    
    public static boolean remover(int numero, int[] v) {
        return false;
    }
    
    public static void main(String[] args) {
      Scanner leitor = new Scanner(System.in);

      int tipoTeste = leitor.nextInt();
      if(tipoTeste == 1){
        System.out.println("Teste:main");
      } else {
        System.out.println("Teste:metodo");
      }

      int numLista = leitor.nextInt();
      int[] vetor = new int[numLista];
    }
    
}