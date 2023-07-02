public class DiscoVoador {
  //atributos privados
  private String cor;
  private int posicaoAtual = 0;

  public void darPartida() {
   System.out.println("Motor ligado!");
  }

  public void irParaFrente(int metros) {
    posicaoAtual += metros;
  }

  public void setCor(String cor) {
    this.cor = cor;
  }

  public String getCor() {
    return this.cor;
  }
}
