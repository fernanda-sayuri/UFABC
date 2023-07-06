// package dispositivos;
import dispositivos.LampadaInteligente;

public class GerenciadorLampadas {
    //lampadas é um array do tipo LampadaInteligente
    private LampadaInteligente[] lampadas;
    private int quantidadeLampadas;

    public GerenciadorLampadas() {
        lampadas = new LampadaInteligente[10];
        quantidadeLampadas = 0;
    }

    public void adicionarLampada() {
        if (quantidadeLampadas < 10) {
            LampadaInteligente lampada = new LampadaInteligente();
            lampadas[quantidadeLampadas] = lampada;
            quantidadeLampadas++;
        }
    }

    public void ligarLampada(int indiceLampada) {
        if (indiceLampada >= 0 && indiceLampada < quantidadeLampadas) {
            LampadaInteligente lampada = lampadas[indiceLampada];
            if (!lampada.getEstado()) {
                lampada.ligar();
            }
        }
    }

    public void desligarLampada(int indiceLampada) {
        if (indiceLampada >= 0 && indiceLampada < quantidadeLampadas) {
            LampadaInteligente lampada = lampadas[indiceLampada];
            if (lampada.getEstado()) {
                lampada.desligar();
            }
        }
    }

    public void ligarTodasLampadas() {
        for (int i = 0; i < quantidadeLampadas; i++) {
            LampadaInteligente lampada = lampadas[i];
            if (!lampada.getEstado()) {
                lampada.ligar();
            }
        }
    }

    public void desligarTodasLampadas() {
        for (int i = 0; i < quantidadeLampadas; i++) {
            LampadaInteligente lampada = lampadas[i];
            if (lampada.getEstado()) {
                lampada.desligar();
            }
        }
    }
}
