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
		//definindo valor padrao pros vetores responsaveis pelos processos
		for(int i = 0; i < totalDeProcessos; i++){
			vezesExecutadasDoProcesso[i] = 0;
			processoServido[i] = false;
			impasse[i] = false;
		}
		
		//definindo valor padrao pros vetores responsaveis pelos recursos
		for(int i = 0; i < totalDeRecursos; i++){
			recursosDisponiveis[i] = 0;
			somatoriaRecursosAlocados[i] = 0;
		}
	}
	
	public static void setTimeout(Runnable runnable, int delay){
	    new Thread(() -> {
	        try {
	            Thread.sleep(delay);
	            runnable.run();
	        }
	        catch (Exception e){
	            System.err.println(e);
	        }
	    }).start();
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
		
		//se processou mais do que o total de processos e ainda
		//assim nao conseguiu finalizar, deu deadlock
		for(int processo = 0; processo < totalDeProcessos; processo++)
			if(vezesExecutadasDoProcesso[processo] >= totalDeProcessos){
				System.out.println("\nDEADLOCK, motivo: algum processo rodou mais do que o necessário e ainda não conseguiu\nser executado, ou seja, jamais haverão recursos suficientes para ele executar.");
				return true;
			}
				
		
		//conferindo se conseguiu rodar algum processo
		for(int processo = 0; processo < totalDeProcessos; processo++)
			if(!impasse[processo])
				return false;
		
		System.out.println("\nDEADLOCK, motivo: nenhum processo pode ser executado.");
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
		System.out.println("Processo[" + processo + "] ests usando recurso.");
		
		int[][] recursosAlocadosAntesDaSoma = criarCopiaDaMatriz(recursosAlocados);
		
		for(int recurso = 0; recurso < totalDeRecursos; recurso++){
			
			//se tem recursos pra emprestar
			if(recursosDisponiveis[recurso] > 0){
				
				//recursos disponiveis estao sendo emprestados aos processos
				recursosAlocados[processo][recurso] += recursosDisponiveis[recurso];
				
				//recursos disponiveis diminuiram
				recursosDisponiveis[recurso] -= recursosAlocadosAntesDaSoma[processo][recurso];
			}
			
		}
	}
	
	private void devolverRecurso(int processo){
		for(int recurso = 0; recurso < totalDeRecursos; recurso++){
			
			//recursos antes usados agora serao
			//devolvidos para os recursos disponiveis
			recursosDisponiveis[recurso] = recursosAlocados[processo][recurso];

			//zerando a quantidade de recursos
			//que aquele processo precisa
			recursosAlocados[processo][recurso] = 0;
			recursosNecessarios[processo][recurso] = 0;
			
		}
		processoServido[processo] = true;
		System.out.println("Processo[" + processo + "] devolveu o recurso. Ficou em " + (posicaoDoProcesso+1) + "o lugar.");
		posicaoDoProcesso++;
		
		System.out.println("\n--------------------------------------------------------------");
		System.out.println("Exibindo recursos disponiveis apas o processo devolver recurso:");
		mostrarVetorDe("Rec. disponiveis", recursosDisponiveis);
		
		//serviu o processo, esque�a os antigos
		//impasses e tente novamente, talvez tenha
		//conseguido novos recursos.
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
					System.out.println("Nao tem recurso o suficiente pra suprir. Processo[" + processo + "] esta inseguro.");
					break;
				}
				
				//chegou ate o fim dos recursos e nao houve impasses
				else if((recurso >= (totalDeRecursos-1)) && (!impasse[processo])){
					
					System.out.println("\nTudo ok pra emprestar todos os recursos!!! O processo " + processo + " esta seguro!");
					usarRecurso(processo);
					devolverRecurso(processo);
					
					//forçando a voltar a comparar desde o primeiro
					//processo, em vez de seguir em frente.
					//Necessario colocar -1 pois se colocar 0, o loop
					//vai começar do 1.
					processo = -1;
					
					mostrarMatrizDe("Rec. necessarios", recursosNecessarios);
					mostrarMatrizDe("Rec. alocados", recursosAlocados);
					mostrarVetorDe("Rec. disponiveis", recursosDisponiveis);
					mostrarVetorDe("Rec. alocados", recursosExistentes);
				}
				else {
					System.out.println("Tem recurso disponivel pra suprir!");
				}
			}
		}
	}
	
	private void mostrarAndamentoDaComparacao(int processo, int recurso){
		System.out.println();
		System.out.println("=============================================");
		System.out.println("Analisando o processo " + processo + ", recurso " + recurso);
		System.out.println("Analisando se recurso disponivel[" + recurso + "] < recurso necessario["+ recurso + "]...");
	}
	
	private void mostrarVezesExecutadas(int processo){
		System.out.println("\n*********************************************");
		System.out.println("*Processo[" + processo + "] esta sendo executado pela " + vezesExecutadasDoProcesso[processo] + "x...*");
		System.out.println("*********************************************\n");
	}
	
	public void mostrarMatrizDe(String mensagem, int[][] matriz){
		System.out.println("================");
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
	
	//TODO
	public void realizarAnalise(){
		
		somatoriaRecursosAlocados = calcularRecursosEmUso(recursosAlocados);
		recursosDisponiveis = calcularRecursosDisponiveis(recursosExistentes, somatoriaRecursosAlocados);
		
		mostrarMatrizDe("Rec. necessarios", recursosNecessarios);
		mostrarMatrizDe("Rec. alocados", recursosAlocados);
		mostrarVetorDe("Rec. disponiveis", recursosDisponiveis);
		mostrarVetorDe("Rec. alocados", recursosExistentes);
		
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
