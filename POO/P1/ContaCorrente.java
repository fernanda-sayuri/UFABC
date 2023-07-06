public class ContaCorrente {
    private static int quantidadeContas = 0;
    private String titular;
    private double saldo;

    public ContaCorrente(String nome) {
        this.titular = nome;
        this.saldo = 0.0;
        quantidadeContas++;
    }

    public ContaCorrente(String nome, double saldo) {
        this.titular = nome;
        this.saldo = saldo;
        quantidadeContas++;
    }

    public double getSaldo() {
        return saldo;
    }

    public String getTitular() {
        return titular;
    }

    public boolean depositar(double valor) {
        if (valor > 0) {
            saldo += valor;
            return true;
        }
        return false;
    }

    public boolean sacar(double valor) {
        if (valor > 0 && valor <= saldo) {
            saldo -= valor;
            return true;
        }
        return false;
    }

    public static boolean transferir(ContaCorrente de, ContaCorrente para, double valor) {
        if (valor > 0 && valor <= de.saldo) {
            de.saldo -= valor;
            para.saldo += valor;
            return true;
        }
        return false;
    }

    public static int getQuantidadeContas() {
        return quantidadeContas;
    }
}