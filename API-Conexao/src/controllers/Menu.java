package controllers;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.nio.file.FileStore;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.NumberFormat;
import java.util.ArrayList;

import com.sun.management.OperatingSystemMXBean;

import models.LoginModel;

public class Menu {

	ImprimeMetricas imprimeMetricas;

	public Menu(LoginModel loginModel) {
		this.imprimeMetricas = new ImprimeMetricas(loginModel);
	}

	public void startmenu() {

		int choice = 1;

		try (Leitor datametrica = new Leitor()) {

			while (choice != 0) {
				choice = controle(datametrica);

				switch (choice) {

				case 1:
					System.out.print("Data Inicial: ");
					String data1 = datametrica.getTexto();
					System.out.print("Data final: ");
					String data2 = datametrica.getTexto();
					imprimeMetricas.ImprimeExcelTamanhoBancos(data1, data2);
					break;

				case 2:
					System.out.print("Data Inicial: ");
					String datatab = datametrica.getTexto();
					System.out.print("Data final: ");
					String datatab1 = datametrica.getTexto();
					imprimeMetricas.ImprimeExcelTamanhoTabelas(datatab, datatab1);
					break;

				case 3:
					System.out.print("Data Inicial: ");
					String datax = datametrica.getTexto();
					System.out.print("Data final: ");
					String datax1 = datametrica.getTexto();
					imprimeMetricas.ImprimeExcelselectsChamadas1000x(datax, datax1);
					break;

				case 4:
					System.out.print("Data Inicial: ");
					String datadem = datametrica.getTexto();
					System.out.print("Data final: ");
					String datadem1 = datametrica.getTexto();
					imprimeMetricas.ImprimeExcelSelectMaisDemoradas(datadem, datadem1);
					break;

				case 5:
					System.out.print("Data Inicial: ");
					String datamed = datametrica.getTexto();
					System.out.print("Data final: ");
					String datamed1 = datametrica.getTexto();
					imprimeMetricas.ImprimeExcelselectsMaisDemoradasMedia(datamed, datamed1);
					break;

				case 6:
					System.out.print("Data Inicial: ");
					String datacon = datametrica.getTexto();
					System.out.print("Data final: ");
					String datacon1 = datametrica.getTexto();
					imprimeMetricas.ImprimeExcelconflicts(datacon, datacon1);
					break;

				case 7: 
					OperatingSystemMXBean bean = (com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
					NumberFormat nf = NumberFormat.getNumberInstance();
					
					Double cpu_uso;
					Double ram_livre;
					Double ram_tam;
					Double swap_mem;
					@SuppressWarnings({ "rawtypes", "unchecked" }) 
					ArrayList<String> lista = new ArrayList();
					
					
					for (Path root : FileSystems.getDefault().getRootDirectories()) {

						System.out.print(root + ": ");
						lista.add("Root");
						lista.add(root.toString());
						
						try {
							FileStore store = Files.getFileStore(root);
							String uso_disco = nf.format(store.getUsableSpace() / 1024 / 1024 / 1024);
							String tam_disco = nf.format(store.getTotalSpace() / 1024 / 1024 / 1024);
							System.out.println("Disponível: " + uso_disco + " Total: " + tam_disco);
							
							
							lista.add("Uso do disco");
							lista.add(uso_disco);
							lista.add("Tamanho do disco");
							lista.add(tam_disco);
							
						} catch (IOException e) {
							System.out.println("error querying space: " + e.toString());
						}
					}

					do {
						cpu_uso = bean.getCpuLoad();
						ram_livre = (double) bean.getFreeMemorySize();
						ram_tam = (double) bean.getTotalMemorySize();
						swap_mem = (double) bean.getFreeSwapSpaceSize() / 1024 / 1024 / 1024;

					} while (cpu_uso == -1.0);

					System.out.printf("CPU: %.2f %n", cpu_uso);
					System.out.printf("RAM: %.2f %n", (1 - (ram_livre / ram_tam)));
					System.out.printf("Memória Swap: %.0f %n", swap_mem); 
					
					imprimeMetricas.ImprimeExcelDesempenho(cpu_uso, 1- (ram_livre/ram_tam), swap_mem, lista);
					
					break;
				 

				}

			}
			System.out.println("\nDeseja Fechar o Programa?");
			System.out.println("[1] Sim");
			System.out.println("[2] Não");
			System.out.print("Opção: ");

			int opcao = datametrica.getValor();

			if (opcao == 1) {
				System.out.println("\n Programa Encerrado..");
				System.out.println("----------------------------");
				System.exit(0);
			} else {
				startmenu();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int controle(Leitor datametrica) {
		System.out.println("\n-------- Métricas --------");
		System.out.println(" [1] - Tamanho do Banco");
		System.out.println(" [2] - Tamanho das Tabelas");
		System.out.println(" [3] - Queries Mais Chamadas");
		System.out.println(" [4] - Queries Mais Demoradas");
		System.out.println(" [5] - Tempo Medio de Execução");
		System.out.println(" [6] - Conflitos no Banco de Dados");
		System.out.println(" [7] - Status Usos de Cpu e Ram");
		System.out.println(" [0] - Sair");
		System.out.print("Opção: ");

		int choice = datametrica.getValor();
		return choice;
	}
}