package controller;

import java.util.Random;

public class DeadlockAlgorithm {
	
	private int totalDeProcessos;
	private int totalDeRecursos;	
	private int posicaoDoProcesso = 0;
	
	private int[][] recursosAlocados;
	private int[][] recursosNecessarios;
	private int[] vezesExecutadasDoProcesso;
	private int[] recursosDisponiveis;
	private int[] recursosExistentes;
	private int[] somatoriaRecursosAlocados;
	private boolean[] processoServido;
	private boolean[] impasse;
	
	public DeadlockAlgorithm(int totalDeProcessos, int totalDeRecursos, int[][] recursosAlocados, int[][] recursosNecessarios, int[] recursosExistentes) {
		
		this.totalDeProcessos = totalDeProcessos;
		this.totalDeRecursos = totalDeRecursos;
		this.recursosAlocados = recursosAlocados;
		this.recursosNecessarios = recursosNecessarios;
		this.recursosExistentes = recursosExistentes;
		this.vezesExecutadasDoProcesso = new int[totalDeProcessos];
		this.processoServido = new boolean[totalDeProcessos];
		this.impasse = new boolean[totalDeProcessos];
		
		this.recursosDisponiveis = new int[totalDeRecursos];
		this.somatoriaRecursosAlocados = new int[totalDeRecursos];
				
		populaDados();
	}

	private void populaDados() {
		//valor nos vetores
		for(int i = 0; i < totalDeProcessos; i++){
			vezesExecutadasDoProcesso[i] = 0;
			processoServido[i] = false;
			impasse[i] = false;
		}
		
		for(int i = 0; i < totalDeRecursos; i++){
			recursosDisponiveis[i] = 0;
			somatoriaRecursosAlocados[i] = 0;
		}
	}
	
	private int[] calcularRecursosEmUso(int[][] recursosAlocados){
		
		for(int processo = 0; processo < totalDeProcessos; processo++){
			for(int recurso = 0; recurso < totalDeRecursos; recurso++){
				somatoriaRecursosAlocados[recurso] += recursosAlocados[processo][recurso];
			}
		}
		return somatoriaRecursosAlocados;
	}
	
	private int[] calcularRecursosExistentes(int[][] recursosAlocados, int[] recursosDisponiveis){
		for(int processo = 0; processo < totalDeProcessos; processo++){
			for(int recurso = 0; recurso < totalDeRecursos; recurso++){
				recursosExistentes[recurso] += recursosAlocados[processo][recurso];
			}
		}
		
		for(int recurso = 0; recurso < totalDeRecursos; recurso++)
			recursosExistentes[recurso] += recursosDisponiveis[recurso];

		return recursosExistentes;
	}
	
	private boolean recursosSuficientes(){
		
		//se os recursos existentes nao satisfazerem
		//os recursos necessarios + recursos alocados
		for(int processo = 0; processo < totalDeProcessos; processo++)
			for(int recurso = 0; recurso < totalDeRecursos; recurso++)

				if(recursosExistentes[recurso] <
				  (recursosNecessarios[processo][recurso] + recursosAlocados[processo][recurso]))
					return false;
		
		return true;
		
	}
	
	private boolean deadlock(){
		//caso tenha analisado todos os processos e não achou recursos pra suprir
		for(int processo = 0; processo < totalDeProcessos; processo++)
			if(vezesExecutadasDoProcesso[processo] >= totalDeProcessos){
				System.out.println("\nDEADLOCK: Não existem recursos suficientes para ele executar os processos.");
				return true;
			}
				
		for(int processo = 0; processo < totalDeProcessos; processo++)
			if(!impasse[processo])
				return false;
		
		System.out.println("\nDEADLOCK: nenhum processo pode ser executado.");
		return true;
	}
	
	private int[] calcularRecursosDisponiveis(int[] recursosExistentes, int[] somatoriaRecursosAlocados){
		for(int recurso = 0; recurso < totalDeRecursos; recurso++)
			recursosDisponiveis[recurso] = recursosExistentes[recurso] - somatoriaRecursosAlocados[recurso];
	
		return recursosDisponiveis;
	}
	
	private int[][] criarCopiaDaMatriz(int[][] antigaMatriz){
		int[][] novaMatriz = new int[totalDeProcessos][totalDeRecursos];
		for(int i = 0; i < totalDeProcessos; i++){
			for(int j = 0; j < totalDeRecursos; j++){
				novaMatriz[i][j] = antigaMatriz[i][j];
			}
		}
		return novaMatriz;
	}
	
	private void resetarImpasses(){
		for(int processo = 0; processo < totalDeProcessos; processo++)
			impasse[processo] = false;
	}
	
	private boolean processosServidos(){
		for(int processo = 0; processo < totalDeProcessos; processo++)
			if(!processoServido[processo])
				return false;
		
		return true;
	}
	
	private void usarRecurso(int processo){
		System.out.println("Processo[" + processo + "] usando recurso.");
		int[][] recursosAlocadosAntesDaSoma = criarCopiaDaMatriz(recursosAlocados);
		
		for(int recurso = 0; recurso < totalDeRecursos; recurso++){
			
			//se tem recursos pra emprestar
			if(recursosDisponiveis[recurso] > 0){
				
				//emprestar recursos disponiveis
				recursosAlocados[processo][recurso] += recursosDisponiveis[recurso];
				
				//tirar recursos que foram emprestados
				recursosDisponiveis[recurso] -= recursosAlocadosAntesDaSoma[processo][recurso];
			}
		}
	}
	
	private void devolverRecurso(int processo){
		for(int recurso = 0; recurso < totalDeRecursos; recurso++){
			
			//devolvendo recursos usados como disponiveis
			recursosDisponiveis[recurso] = recursosAlocados[processo][recurso];

			recursosAlocados[processo][recurso] = 0;
			recursosNecessarios[processo][recurso] = 0;
			
		}
		processoServido[processo] = true;
		System.out.println("Processo[" + processo + "] devolveu recurso");
		posicaoDoProcesso++;
		
		System.out.println("\n--------------------------------------------------------------");
		System.out.println("Show recursos disponiveis apos o processo devolver recurso:");
		mostrarVetorDe("Recursos disponiveis", recursosDisponiveis);
		
		resetarImpasses();
	}
	
	private void compararRecursos(int[][] recursosNecessarios, int[] recursosDisponiveis){
		
		for(int processo = 0; processo < totalDeProcessos; processo++){
			
			//se ja foi servido, va servir o proximo processo
			if(processoServido[processo])
				continue;
			
			//passou pelo processo
			vezesExecutadasDoProcesso[processo]++;
			
			mostrarVezesExecutadas(processo);
			
			for(int recurso = 0; recurso < totalDeRecursos; recurso++){
				
				mostrarAndamentoDaComparacao(processo, recurso);
				
				if(recursosDisponiveis[recurso] < recursosNecessarios[processo][recurso]){
					impasse[processo] = true;
					System.out.println("Sem recursos suficientes pra suprir. Processo[" + processo + "] esta inseguro.");
					break;
				}
				
				//chegou ate o fim dos recursos e nao houve impasses
				else if((recurso >= (totalDeRecursos-1)) && (!impasse[processo])){
					
					System.out.println("\nTudo ok pra emprestar todos os recursos!!! O processo " + processo + " esta seguro!");
					usarRecurso(processo);
					devolverRecurso(processo);
					
					//forçando a voltar a comparar desde o primeiro
					processo = -1;
					
					mostrarMatrizDe("Recursos necessarios:", recursosNecessarios);
					mostrarMatrizDe("Recursos alocados:", recursosAlocados);
					mostrarVetorDe("Recursos. disponiveis:", recursosDisponiveis);
					mostrarVetorDe("Recursos totais:", recursosExistentes);
				}
				else {
					System.out.println("Tem recurso disponivel pra suprir!");
				}
			}
		}
	}
	
	private void mostrarAndamentoDaComparacao(int processo, int recurso){
		System.out.println();
		System.out.println("..................................");
		System.out.println("Analisando o processo " + processo + ", recurso " + recurso);
		System.out.println("Analisando se recurso disponivel[" + recurso + "] < recurso necessario["+ recurso + "]...");
	}
	
	private void mostrarVezesExecutadas(int processo){
		System.out.println("\n*********************************************");
		System.out.println("*Processo[" + processo + "] esta sendo executado pela " + vezesExecutadasDoProcesso[processo] + "x...*");
		System.out.println("*********************************************\n");
	}
	
	public void mostrarMatrizDe(String mensagem, int[][] matriz){
		System.out.println(".....................");
		System.out.println(mensagem);
		for(int processo = 0; processo < matriz.length; processo++){
			for(int recurso = 0; recurso < matriz[0].length; recurso++){
				System.out.print(matriz[processo][recurso] + " ");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	public void mostrarVetorDe(String mensagem, int[] vetor){
		System.out.println("================");
		System.out.println(mensagem);
		int tamanhoVetor = vetor.length;
		for(int i = 0; i < tamanhoVetor; i++){
			System.out.print(vetor[i] + " ");
		}
		System.out.println();
	}
	
	public void realizarAnalise(){
		
		somatoriaRecursosAlocados = calcularRecursosEmUso(recursosAlocados);
		recursosDisponiveis = calcularRecursosDisponiveis(recursosExistentes, somatoriaRecursosAlocados);
		
		mostrarMatrizDe("Recursos necessarios:", recursosNecessarios);
		mostrarMatrizDe("Recursos alocados:", recursosAlocados);
		mostrarVetorDe("Recursos disponiveis:", recursosDisponiveis);
		mostrarVetorDe("Recursos totais:", recursosExistentes);
		
		if(!recursosSuficientes()){
			System.out.println("Nao ha recursos existentes suficientes para suprir o (total de recursos alocados + total de recursos necessários), impossivel prosseguir.");
			return;
		}
		
		while(!processosServidos() && !deadlock()){
			compararRecursos(recursosNecessarios, recursosDisponiveis);
		}
		
		if(!deadlock())
			System.out.println("\nNao houve deadlock.");
	}
}
