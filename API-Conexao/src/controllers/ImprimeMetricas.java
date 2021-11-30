package controllers;

import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import models.CaminhoExcel;
import models.LoginModel;
import models.SelectConflictsModel;
import models.SelectMaisDemoradasModel;
import models.SelectsChamadas1000xModel;
import models.SelectsMaisDemoradasMediaModel;
import models.TamanhoBancos;
import models.TamanhoTabelasModel;

public class ImprimeMetricas {

	private LoginModel loginModel;
	CaminhoExcel caminho = new CaminhoExcel();

	public ImprimeMetricas(LoginModel loginModel) {
		this.loginModel = loginModel;
	}
	
	// ** Métrica de Mais Selects Chamadas **
	@SuppressWarnings("unused")
	public void SelectMaisDemoradas() {
		ObterMetricas obterMetricas = new ObterMetricas(loginModel);
		List<SelectMaisDemoradasModel> selectMaisDemoradas = obterMetricas.SelectMaisDemoradas();

		for (SelectMaisDemoradasModel selectMaisDemoradasModel : selectMaisDemoradas) {
			String query = selectMaisDemoradasModel.getQuery();
			String data = selectMaisDemoradasModel.getData();
			String tempo = selectMaisDemoradasModel.getTempo();
		}
	}

	// ** Métrica de Selects Chamadas Mais de 100 vezes **
	@SuppressWarnings("unused")
	public void selectsChamadas1000x() {
		ObterMetricas obterMetricas1 = new ObterMetricas(loginModel);
		List<SelectsChamadas1000xModel> selectsChamadas1000x = obterMetricas1.SelectsChamadas1000x();

		for (SelectsChamadas1000xModel selectsChamadas1000xModel : selectsChamadas1000x) {
			String calls = selectsChamadas1000xModel.getCalls();
			String query = selectsChamadas1000xModel.getQuery();
			String time_exec = selectsChamadas1000xModel.getTotal_exec_time();
			String date = selectsChamadas1000xModel.getDate();
		}
	}

	// ** Métrica de Média das Selects Mais Demoradas **
	@SuppressWarnings("unused")
	public void selectsMaisDemoradasMedia() {
		ObterMetricas obterMetricas2 = new ObterMetricas(loginModel);
		List<SelectsMaisDemoradasMediaModel> selectsMaisDemoradasMedia = obterMetricas2.SelectsMaisDemoradasMedia();

		for (SelectsMaisDemoradasMediaModel selectsMaisDemoradasMediaModel : selectsMaisDemoradasMedia) {
			String date = selectsMaisDemoradasMediaModel.getData();
			String query = selectsMaisDemoradasMediaModel.getQuery();
			String time_avar = selectsMaisDemoradasMediaModel.getTempoMedio();
		}
	}

	// ** Métrica do Tamanho dos Bancos **
	@SuppressWarnings("unused")
	public void tamanhobancos() {
		ObterMetricas obterMetricas3 = new ObterMetricas(loginModel);
		List<TamanhoBancos> tamanhobancos1 = obterMetricas3.TamanhoBanco();

		for (TamanhoBancos tamanhobancos : tamanhobancos1) {
			String nome = tamanhobancos.getNome();
			String size = tamanhobancos.getTamanho();
			String date = tamanhobancos.getData();
		}
	}

	// ** Métrica do Tamanho das Tabelas **
	@SuppressWarnings("unused")
	public void tamanhoTabelas() {
		ObterMetricas obterMetricas4 = new ObterMetricas(loginModel);
		List<TamanhoTabelasModel> tamanhoTabelas = obterMetricas4.TamanhoTabelas();

		for (TamanhoTabelasModel tamanhoTabelasModel : tamanhoTabelas) {
			String nome = tamanhoTabelasModel.getNome();
			String size = tamanhoTabelasModel.getTamanhoTotal();
			String date = tamanhoTabelasModel.getData();
		}
	}

	// ** Métrica dos Número de Conflitos do Bancos **
	@SuppressWarnings("unused")
	public void conflicts() {
		ObterMetricas obterMetricas5 = new ObterMetricas(loginModel);
		List<SelectConflictsModel> conflict = obterMetricas5.SelectConflicts();

		for (SelectConflictsModel selectConflictsModel : conflict) {
			String nome = selectConflictsModel.getName();
			String confl = selectConflictsModel.getConfl();
			String confl_dead = selectConflictsModel.getConfl_dead();
			String tempo = selectConflictsModel.getTempo();
		}
	}

	// ** Exportação de Métrica do Tamanho dos Bancos (Excel/xls) **
	public void ImprimeExcelTamanhoBancos(String data1, String data2) {
		ObterMetricas obterMetricas4 = new ObterMetricas(loginModel);
		List<TamanhoBancos> tamanhoBancos = obterMetricas4.ObterExcelTamanhoBancos(data1, data2);

		// Excel
		int contColunmExcel = 0;
		String dataAtual = getDateTime();
		String filename = caminho.getNome() + "Tamanho Bancos -" + dataAtual + ".xls";
		@SuppressWarnings("resource")
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("FirstSheet");

		for (TamanhoBancos tamanhoBanco : tamanhoBancos) {
			String nome = tamanhoBanco.getNome();
			String data = tamanhoBanco.getData();
			String tamanho = tamanhoBanco.getTamanho();

			// Excel
			// criando as linhas
			HSSFRow rowhead = sheet.createRow((short) 0);
			rowhead.createCell(0).setCellValue("Nome");
			rowhead.createCell(1).setCellValue("Data");
			rowhead.createCell(2).setCellValue("Tamanho");
			// adiciona os dados no excel
			contColunmExcel += 1;
			// definindo seus valores
			HSSFRow row = sheet.createRow((short) contColunmExcel);
			row.createCell(0).setCellValue(nome);
			row.createCell(1).setCellValue(data);
			row.createCell(2).setCellValue(tamanho);

		}
		// Excel
		try {
			FileOutputStream fileOut = new FileOutputStream(filename);
			workbook.write(fileOut);
			fileOut.close();
			System.out.println(" Arquivo gerado com sucesso!!");
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	// ** Exportação de Métrica de Tamanho das Tabelas (Excel/xls) **
	public void ImprimeExcelTamanhoTabelas(String datatab, String datatab1) {
		ObterMetricas obterMetricas4 = new ObterMetricas(loginModel);
		List<TamanhoTabelasModel> tamanhoTabelas = obterMetricas4.ObterExcelTamanhoTabela(datatab, datatab1);

		// Excel
		int contColunmExcel = 0;
		String dataAtual = getDateTime();
		String filename = caminho.getNome() + "Tamanho Tabelas -" + dataAtual + ".xls";
		@SuppressWarnings("resource")
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("FirstSheet");
		// Excel

		for (TamanhoTabelasModel tamanhoTabelasModel : tamanhoTabelas) {
			String nome = tamanhoTabelasModel.getNome();
			String size = tamanhoTabelasModel.getTamanhoTotal();
			String date = tamanhoTabelasModel.getData();

			// Excel
			// criando as linhas
			HSSFRow rowhead = sheet.createRow((short) 0);
			rowhead.createCell(0).setCellValue("Nome");
			rowhead.createCell(1).setCellValue("Size");
			rowhead.createCell(2).setCellValue("Date");
			// adiciona os dados no excel
			contColunmExcel += 1;
			// definindo seus valores
			HSSFRow row = sheet.createRow((short) contColunmExcel);
			row.createCell(0).setCellValue(nome);
			row.createCell(1).setCellValue(size);
			row.createCell(2).setCellValue(date);
		}

		// Excel
		try {
			FileOutputStream fileOut = new FileOutputStream(filename);
			workbook.write(fileOut);
			fileOut.close();
			System.out.println(" Arquivo gerado com sucesso!!");
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	// ** Exportação de Métricas de Tamanho das Tabelas (Excel/xls) **
	public void ImprimeExcelconflicts(String datacon, String datacon1) {
		ObterMetricas obterMetricas5 = new ObterMetricas(loginModel);
		List<SelectConflictsModel> conflict = obterMetricas5.ObterExcelConflicts(datacon, datacon1);

		// Excel
		int contColunmExcel = 0;
		String dataAtual = getDateTime();
		String filename = caminho.getNome() + "Conflitos -" + dataAtual + ".xls";
		@SuppressWarnings("resource")
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("FirstSheet");

		for (SelectConflictsModel selectConflictsModel : conflict) {
			String nome = selectConflictsModel.getName();
			String confl = selectConflictsModel.getConfl();
			String confl_dead = selectConflictsModel.getConfl_dead();
			String tempo = selectConflictsModel.getTempo();

			// Excel
			// criando as linhas
			HSSFRow rowhead = sheet.createRow((short) 0);
			rowhead.createCell(0).setCellValue("Nome");
			rowhead.createCell(1).setCellValue("Conflito");
			rowhead.createCell(2).setCellValue("Confl_dead");
			rowhead.createCell(3).setCellValue("Tempo");
			// adiciona os dados no excel
			contColunmExcel += 1;
			// definindo seus valores
			HSSFRow row = sheet.createRow((short) contColunmExcel);
			row.createCell(0).setCellValue(nome);
			row.createCell(1).setCellValue(confl);
			row.createCell(2).setCellValue(confl_dead);
			row.createCell(3).setCellValue(tempo);
		}

		// Excel
		try {
			FileOutputStream fileOut = new FileOutputStream(filename);
			workbook.write(fileOut);
			fileOut.close();
			System.out.println(" Arquivo gerado com sucesso!!");
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public void ImprimeExcelDesempenho(Double cpu, Double ram, Double swap, ArrayList<String> lista) {
		@SuppressWarnings("unused")
		ObterMetricas obterMetricas5 = new ObterMetricas(loginModel);

		// Excel
		String dataAtual = getDateTime();
		String filename = caminho.getNome() + "Desempenho -" + dataAtual + ".xls";
		@SuppressWarnings("resource")
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("FirstSheet");
		int contColunmExcel = 3;

			// Excel
			// criando as linhas
			HSSFRow rowhead = sheet.createRow((short) 0);
			rowhead.createCell(0).setCellValue("Cpu");
			rowhead.createCell(1).setCellValue("Ram");
			rowhead.createCell(2).setCellValue("Swap");
			// adiciona os dados no excel

			// definindo seus valores
			HSSFRow row = sheet.createRow((short) 1);
			row.createCell(0).setCellValue(cpu);
			row.createCell(1).setCellValue(ram);
			row.createCell(2).setCellValue(swap);
			
			HSSFRow row2 = sheet.createRow((short) contColunmExcel);
			
			for(int i = 0; i<lista.size(); i++) {
				
				
				row2.createCell(i).setCellValue(lista.get(i));
				i +=1;
				row2.createCell(i).setCellValue(lista.get(i));
				
				
				
			}
			
		// Excel
		try {
			FileOutputStream fileOut = new FileOutputStream(filename);
			workbook.write(fileOut);
			fileOut.close();
			System.out.println(" Arquivo gerado com sucesso!!");
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	// ** Exportação de Métrica das Selects Mais Demoradas (Média)(Excel/xls) **
	public void ImprimeExcelselectsMaisDemoradasMedia(String datamed, String datamed1) {
		ObterMetricas obterMetricas2 = new ObterMetricas(loginModel);
		List<SelectsMaisDemoradasMediaModel> selectsMaisDemoradasMedia = obterMetricas2
				.ObterExcelSelectsMaisDemoradasMedia(datamed, datamed1);

		// Excel
		int contColunmExcel = 0;
		String dataAtual = getDateTime();
		String filename = caminho.getNome() + "Selects Demoradas Media -" + dataAtual + ".xls";
		@SuppressWarnings("resource")
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("FirstSheet");

		for (SelectsMaisDemoradasMediaModel selectsMaisDemoradasMediaModel : selectsMaisDemoradasMedia) {
			String date = selectsMaisDemoradasMediaModel.getData();
			String query = selectsMaisDemoradasMediaModel.getQuery();
			String time_avar = selectsMaisDemoradasMediaModel.getTempoMedio();

			// Excel
			// Criando as linhas
			HSSFRow rowhead = sheet.createRow((short) 0);
			rowhead.createCell(0).setCellValue("Query");
			rowhead.createCell(1).setCellValue("Tempo");
			rowhead.createCell(2).setCellValue("Data");
			// Adiciona os dados no excel
			contColunmExcel += 1;
			// Definindo seus valores
			HSSFRow row = sheet.createRow((short) contColunmExcel);
			row.createCell(0).setCellValue(query);
			row.createCell(1).setCellValue(time_avar);
			row.createCell(2).setCellValue(date);
		}

		// Excel
		try {
			FileOutputStream fileOut = new FileOutputStream(filename);
			workbook.write(fileOut);
			fileOut.close();
			System.out.println(" Arquivo gerado com sucesso!!");
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	// ** Exportação de Métrica das Selects Mais Demoradas (Excel/xls) **
	public void ImprimeExcelSelectMaisDemoradas(String datadem, String datadem1) {
		ObterMetricas obterMetricas = new ObterMetricas(loginModel);
		List<SelectMaisDemoradasModel> selectMaisDemoradas = obterMetricas.ObterExcelSelectMaisDemoradas(datadem,
				datadem1);

		// Excel
		int contColunmExcel = 0;
		String dataAtual = getDateTime();
		String filename = caminho.getNome() + "Selects Mais Demoradas -" + dataAtual + ".xls";
		@SuppressWarnings("resource")
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("FirstSheet");

		for (SelectMaisDemoradasModel selectMaisDemoradasModel : selectMaisDemoradas) {
			String query = selectMaisDemoradasModel.getQuery();
			String tempo = selectMaisDemoradasModel.getTempo();
			String data = selectMaisDemoradasModel.getData();

			// Excel
			// criando as linhas
			HSSFRow rowhead = sheet.createRow((short) 0);
			rowhead.createCell(0).setCellValue("Query");
			rowhead.createCell(2).setCellValue("Tempo");
			rowhead.createCell(1).setCellValue("Data");
			// adiciona os dados no excel
			contColunmExcel += 1;
			// definindo seus valores
			// por exemplo protocolo.getProtocolo();
			HSSFRow row = sheet.createRow((short) contColunmExcel);
			row.createCell(0).setCellValue(query);
			row.createCell(2).setCellValue(tempo);
			row.createCell(1).setCellValue(data);
		}

		// Excel
		try {
			FileOutputStream fileOut = new FileOutputStream(filename);
			workbook.write(fileOut);
			fileOut.close();
			System.out.println(" Arquivo gerado com sucesso!!");
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	// ** Exportação de Métrica de Selects Chamadas mais de 100 vezes (Excel/xls) **
	public void ImprimeExcelselectsChamadas1000x(String datax, String datax1) {
		ObterMetricas obterMetricas1 = new ObterMetricas(loginModel);
		List<SelectsChamadas1000xModel> selectsChamadas1000x = obterMetricas1.ObterExcelSelectsChamadas1000x(datax,
				datax1);

		// Excel
		int contColunmExcel = 0;
		String dataAtual = getDateTime();
		String filename = caminho.getNome() + "Selects Chamadas Muitas Vezes -" + dataAtual + ".xls";
		@SuppressWarnings("resource")
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("FirstSheet");

		for (SelectsChamadas1000xModel selectsChamadas1000xModel : selectsChamadas1000x) {
			String calls = selectsChamadas1000xModel.getCalls();
			String query = selectsChamadas1000xModel.getQuery();
			String time_exec = selectsChamadas1000xModel.getTotal_exec_time();
			String date = selectsChamadas1000xModel.getDate();

			// Excel
			// criando as linhas
			HSSFRow rowhead = sheet.createRow((short) 0);
			rowhead.createCell(0).setCellValue("Calls");
			rowhead.createCell(1).setCellValue("Query");
			rowhead.createCell(2).setCellValue("Time Exec");
			rowhead.createCell(3).setCellValue("Date");
			// adiciona os dados no excel
			contColunmExcel += 1;
			// definindo seus valores
			HSSFRow row = sheet.createRow((short) contColunmExcel);
			row.createCell(0).setCellValue(calls);
			row.createCell(1).setCellValue(query);
			row.createCell(2).setCellValue(time_exec);
			row.createCell(3).setCellValue(date);
		}

		// Excel
		try {
			FileOutputStream fileOut = new FileOutputStream(filename);
			workbook.write(fileOut);
			fileOut.close();
			System.out.println(" Arquivo gerado com sucesso!!");
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private String getDateTime() {
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date date = new Date();
		return dateFormat.format(date);
	}
}