package controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

//import models.CpuUsageModel;
import models.LoginModel;
import models.SelectConflictsModel;
import models.SelectMaisDemoradasModel;
import models.SelectsChamadas1000xModel;
import models.SelectsMaisDemoradasMediaModel;
import models.TamanhoBancos;
import models.TamanhoTabelasModel;

public class ObterMetricas {

	private String caminho;
	private String caminholite;
	private String porta;
	private String banco;
	private String usuario;
	private String senha;
	private Connection con;
	private Connection in;

	public ObterMetricas(LoginModel login) {
		usuario = login.getUsuario();
		senha = login.getSenha();
		banco = login.getBanco();
		porta = login.getPorta();

		caminho = "jdbc:postgresql://localhost:" + porta + "/" + banco;

		caminholite = "jdbc:sqlite:C:/Vigilant/banco_de_dados/banco_sqlite.db/";
	}

	public void iniciarConexao() throws SQLException {

		// Conexão do PostgreSQL
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}

		try {
			con = DriverManager.getConnection(caminho, usuario, senha);
		} catch (SQLException ex) {
			System.out.println("Falha de Conexão com PostgreSQL");
			ex.printStackTrace();
			throw ex;
		}

		// Conexão do SQLite
		try {
			in = DriverManager.getConnection(caminholite);
		} catch (SQLException ex) {
			System.out.println("Falha de Conexão com SQLite");
			ex.printStackTrace();
			throw ex;
		}
	}

	public void desconecta() {
		try {
			con.close();
			in.close();
		} catch (SQLException ex) {
		}
	}
	
	
	// ** Select de Informações do Tamanho dos Bancos **
	public ArrayList<TamanhoBancos> TamanhoBanco() {

		String sql = "SELECT pg_database.datname, pg_size_pretty(pg_database_size(pg_database.datname)),current_timestamp(0) AS data FROM pg_database;";

		try {
			iniciarConexao();
			PreparedStatement pesquisa = con.prepareStatement(sql);
			ResultSet result = pesquisa.executeQuery();
			ArrayList<TamanhoBancos> lista = new ArrayList<TamanhoBancos>();

			String query = "INSERT INTO TamanhoBanco (datname,pg_size_pretty,data)VALUES (?, ?, ?)";
			PreparedStatement st = in.prepareStatement(query);

			while (result.next()) {
				TamanhoBancos tamTab = new TamanhoBancos();

				tamTab.setNome(result.getString("datname"));
				tamTab.setTamanho(result.getString("pg_size_pretty"));
				tamTab.setData(result.getString("data"));
				lista.add(tamTab);

				st.setString(1, tamTab.getNome());
				st.setString(2, tamTab.getTamanho());
				st.setString(3, tamTab.getData());
				st.executeUpdate();
			}
			return lista;
		} catch (SQLException e) {
			throw new SQLRunTimeException(e.getMessage(), e);
		} finally {
			desconecta();
		}
	}

	// ** Select de Informações das Tabelas dos Bancos **
	ArrayList<TamanhoTabelasModel> TamanhoTabelas() {

		String sql = "SELECT tabela, pg_size_pretty(pg_total_relation_size(esq_tab)),"
				+ " current_timestamp(0) AS date FROM (SELECT tablename AS tabela, "
				+ "schemaname||'.'||tablename AS esq_tab FROM pg_catalog.pg_tables "
				+ "WHERE schemaname NOT IN ('pg_catalog', 'information_schema', 'pg_toast') ) "
				+ "AS x ORDER BY pg_total_relation_size(esq_tab) DESC;";

		try {
			iniciarConexao();
			PreparedStatement pesquisa = con.prepareStatement(sql);
			ResultSet result = pesquisa.executeQuery();
			ArrayList<TamanhoTabelasModel> lista = new ArrayList<TamanhoTabelasModel>();

			String query = "INSERT INTO TamanhoTabela (tabela,pg_size_pretty,data)VALUES (?, ?, ?)";
			PreparedStatement st = in.prepareStatement(query);

			while (result.next()) {
				TamanhoTabelasModel tamTab = new TamanhoTabelasModel();

				tamTab.setNome(result.getString("tabela"));
				tamTab.setTamanhoTotal(result.getString("pg_size_pretty"));
				tamTab.setData(result.getString("date"));
				lista.add(tamTab);

				st.setString(1, tamTab.getNome());
				st.setString(2, tamTab.getTamanhoTotal());
				st.setString(3, tamTab.getData());
				st.executeUpdate();

			}
			return lista;
		} catch (SQLException e) {
			throw new SQLRunTimeException(e.getMessage(), e);
		} finally {
			desconecta();
		}
	}

	// ** Select de Informações das Selects Chamadas mais de 1000x **
	ArrayList<SelectsChamadas1000xModel> SelectsChamadas1000x() {

		String sql = "SELECT calls, SUBSTRING (query FROM 1 for 50) AS querys, total_exec_time, current_timestamp (0) AS data"
				+ " FROM pg_stat_statements where calls > 100;";

		try {
			iniciarConexao();
			PreparedStatement pesquisa = con.prepareStatement(sql);
			ResultSet result = pesquisa.executeQuery();
			ArrayList<SelectsChamadas1000xModel> lista = new ArrayList<SelectsChamadas1000xModel>();
			Double num;

			String query = "INSERT INTO ChamadasMuitas (calls,querys,data,total_exec_time)VALUES (?, ?, ?, ?)";
			PreparedStatement st = in.prepareStatement(query);

			while (result.next()) {
				SelectsChamadas1000xModel tamTab = new SelectsChamadas1000xModel();
				tamTab.setCalls(result.getString("calls"));
				tamTab.setQuery(result.getString("querys"));
				tamTab.setDate(result.getString("data"));
				DecimalFormat df = new DecimalFormat("####.00");
				num = Double.parseDouble(result.getString("total_exec_time"));
				tamTab.setTotal_exec_time(df.format(num).toString());
				lista.add(tamTab);

				st.setString(1, tamTab.getCalls());
				st.setString(2, tamTab.getQuery());
				st.setString(3, tamTab.getDate());
				st.setString(4, tamTab.getTotal_exec_time());
				st.executeUpdate();
			}
			return lista;
		} catch (SQLException e) {
			throw new SQLRunTimeException(e.getMessage(), e);
		} finally {
			desconecta();
		}
	}

	// ** Select de Informações das Selects Mais Demoradas **
	List<SelectMaisDemoradasModel> SelectMaisDemoradas() {

		String sql = "SELECT SUBSTRING (query FROM 1 for 50), total_exec_time, current_timestamp(0) AS data \n"
				+ " FROM pg_stat_statements ORDER BY total_exec_time DESC LIMIT 10;";

		try {
			iniciarConexao();
			PreparedStatement pesquisa = con.prepareStatement(sql);
			ResultSet result = pesquisa.executeQuery();
			List<SelectMaisDemoradasModel> lista = new LinkedList<SelectMaisDemoradasModel>();
			Double num;

			String query = "INSERT INTO SelectsDemoradas (substring,data,total_exec_time)VALUES (?, ?, ?)";
			PreparedStatement st = in.prepareStatement(query);

			while (result.next()) {
				SelectMaisDemoradasModel tamTab = new SelectMaisDemoradasModel();

				tamTab.setQuery(result.getString("substring"));
				tamTab.setData(result.getString("data"));
				DecimalFormat df = new DecimalFormat("####.00");
				num = Double.parseDouble(result.getString("total_exec_time"));
				tamTab.setTempo(df.format(num).toString());
				lista.add(tamTab);

				st.setString(1, tamTab.getQuery());
				st.setString(2, tamTab.getData());
				st.setString(3, tamTab.getTempo());
				st.executeUpdate();
			}
			return lista;
		} catch (SQLException e) {
			throw new SQLRunTimeException(e.getMessage(), e);
		} finally {
			desconecta();
		}
	}

	// ** Select de Informações das Selects Mais Demoradas (Média) **
	ArrayList<SelectsMaisDemoradasMediaModel> SelectsMaisDemoradasMedia() {

		String sql = "SELECT SUBSTRING (query FROM 1 for 50), mean_exec_time, current_timestamp (0) AS data FROM pg_stat_statements ORDER BY "
				+ "mean_exec_time DESC LIMIT 10;";

		try {
			iniciarConexao();
			PreparedStatement pesquisa = con.prepareStatement(sql);
			ResultSet result = pesquisa.executeQuery();
			ArrayList<SelectsMaisDemoradasMediaModel> lista = new ArrayList<SelectsMaisDemoradasMediaModel>();
			Double num;

			String query = "INSERT INTO SelectsDemoradasMedia (substring,data,mean_exec_time)VALUES (?, ?, ?)";
			PreparedStatement st = in.prepareStatement(query);

			while (result.next()) {

				SelectsMaisDemoradasMediaModel tamTab = new SelectsMaisDemoradasMediaModel();
				tamTab.setQuery(result.getString("substring"));
				tamTab.setData(result.getString("data"));
				DecimalFormat df = new DecimalFormat("####.00");
				num = Double.parseDouble(result.getString("mean_exec_time"));
				tamTab.setTempoMedio(df.format(num).toString());
				lista.add(tamTab);

				st.setString(1, tamTab.getQuery());
				st.setString(2, tamTab.getData());
				st.setString(3, tamTab.getTempoMedio());
				st.executeUpdate();
			}
			return lista;
		} catch (SQLException e) {
			throw new SQLRunTimeException(e.getMessage(), e);
		} finally {
			desconecta();
		}
	}

	// ** Select de Informações de Conflicts **
	List<SelectConflictsModel> SelectConflicts() {

		String sql = "SELECT datname, confl_lock, confl_deadlock, current_timestamp (0) AS data FROM pg_stat_database_conflicts;";

		try {
			iniciarConexao();
			PreparedStatement pesquisa = con.prepareStatement(sql);
			ResultSet result = pesquisa.executeQuery();
			List<SelectConflictsModel> lista = new LinkedList<SelectConflictsModel>();

			String query = "INSERT INTO SelectsConflicts (datname,Confl_lock,confl_deadlock,data)VALUES (?, ?, ?, ?)";
			PreparedStatement st = in.prepareStatement(query);

			while (result.next()) {
				SelectConflictsModel tamTab = new SelectConflictsModel();

				tamTab.setName(result.getString("datname"));
				tamTab.setTempo(result.getString("data"));
				tamTab.setConfl(result.getString("confl_lock"));
				tamTab.setConfl_dead(result.getString("confl_deadlock"));
				lista.add(tamTab);

				st.setString(1, tamTab.getName());
				st.setString(2, tamTab.getConfl());
				st.setString(3, tamTab.getConfl_dead());
				st.setString(4, tamTab.getTempo());
				st.executeUpdate();
			}
			return lista;
		} catch (SQLException e) {
			throw new SQLRunTimeException(e.getMessage(), e);
		} finally {
			desconecta();
		}
	}

	// ** Excel do Tamanho dos Bancos **
	public ArrayList<TamanhoBancos> ObterExcelTamanhoBancos(String data1, String data2) {

		String sql = "select * from TamanhoBanco where data  between '" + data1 + "' and '" + data2 + "'";

		try {
			iniciarConexao();
			PreparedStatement st = in.prepareStatement(sql);
			ResultSet result = st.executeQuery();
			ArrayList<TamanhoBancos> lista = new ArrayList<TamanhoBancos>();

			while (result.next()) {
				TamanhoBancos tamBan = new TamanhoBancos();

				tamBan.setNome(result.getString("datname"));
				tamBan.setTamanho(result.getString("pg_size_pretty"));
				tamBan.setData(result.getString("data"));
				lista.add(tamBan);

			}
			return lista;
		} catch (SQLException e) {
			throw new SQLRunTimeException(e.getMessage(), e);
		} finally {
			desconecta();
		}
	}

	// ** Excel do Tamanho das Tabelas **
	public List<TamanhoTabelasModel> ObterExcelTamanhoTabela(String datatab, String datatab1) {

		String sql = "select * from TamanhoTabela where data between '" + datatab + "' and '" + datatab1 + "'";

		try {
			iniciarConexao();
			PreparedStatement st = in.prepareStatement(sql);
			ResultSet result = st.executeQuery();
			ArrayList<TamanhoTabelasModel> lista = new ArrayList<TamanhoTabelasModel>();

			while (result.next()) {
				TamanhoTabelasModel tamBan = new TamanhoTabelasModel();

				tamBan.setNome(result.getString("tabela"));
				tamBan.setTamanhoTotal(result.getString("pg_size_pretty"));
				tamBan.setData(result.getString("data"));
				lista.add(tamBan);
			}
			return lista;
		} catch (SQLException e) {
			throw new SQLRunTimeException(e.getMessage(), e);
		} finally {
			desconecta();
		}
	}

	// ** Excel dos Conflitos **
	public List<SelectConflictsModel> ObterExcelConflicts(String datacon, String datacon1) {

		String sql = "select * from SelectsConflicts where data between '" + datacon + "' and '" + datacon1 + "'";

		try {
			iniciarConexao();
			PreparedStatement st = in.prepareStatement(sql);
			ResultSet result = st.executeQuery();
			List<SelectConflictsModel> lista = new LinkedList<SelectConflictsModel>();

			while (result.next()) {
				SelectConflictsModel tamTab = new SelectConflictsModel();

				tamTab.setName(result.getString("datname"));
				tamTab.setTempo(result.getString("data"));
				tamTab.setConfl(result.getString("confl_lock"));
				tamTab.setConfl_dead(result.getString("confl_deadlock"));
				lista.add(tamTab);

			}
			return lista;
		} catch (SQLException e) {
			throw new SQLRunTimeException(e.getMessage(), e);
		} finally {
			desconecta();
		}
	}

	// ** Excel das Selects Mais Demoradas Media **
	public List<SelectsMaisDemoradasMediaModel> ObterExcelSelectsMaisDemoradasMedia(String datatab, String datatab1) {

		String sql = "select * from SelectsDemoradasMedia where data between '" + datatab + "' and '" + datatab1 + "'";

		try {
			iniciarConexao();
			PreparedStatement st = in.prepareStatement(sql);
			ResultSet result = st.executeQuery();
			List<SelectsMaisDemoradasMediaModel> lista = new LinkedList<SelectsMaisDemoradasMediaModel>();

			while (result.next()) {
				SelectsMaisDemoradasMediaModel tamTab = new SelectsMaisDemoradasMediaModel();
				tamTab.setQuery(result.getString("substring"));
				tamTab.setData(result.getString("data"));
				tamTab.setTempoMedio(result.getString("mean_exec_time"));
				lista.add(tamTab);
			}
			return lista;
		} catch (SQLException e) {
			throw new SQLRunTimeException(e.getMessage(), e);
		} finally {
			desconecta();
		}
	}

	// ** Excel das Selects Mais Demoradas **
	public List<SelectMaisDemoradasModel> ObterExcelSelectMaisDemoradas(String datadem, String datadem1) {

		String sql = "select * from ChamadasMuitas where data between '" + datadem + "' and '" + datadem1 + "'";

		try {
			iniciarConexao();
			PreparedStatement st = in.prepareStatement(sql);
			ResultSet result = st.executeQuery();
			List<SelectMaisDemoradasModel> lista = new LinkedList<SelectMaisDemoradasModel>();

			while (result.next()) {
				SelectMaisDemoradasModel tamTab = new SelectMaisDemoradasModel();

				tamTab.setQuery(result.getString("querys"));
				tamTab.setTempo(result.getString("total_exec_time"));
				tamTab.setData(result.getString("data"));
				lista.add(tamTab);
			}
			return lista;
		} catch (SQLException e) {
			throw new SQLRunTimeException(e.getMessage(), e);
		} finally {
			desconecta();
		}
	}

	// ** Excel das Selects Mais Demoradas **
	public List<SelectsChamadas1000xModel> ObterExcelSelectsChamadas1000x(String datax, String datax1) {

		String sql = "select * from ChamadasMuitas where data between '" + datax + "' and '" + datax1 + "'";

		try {
			iniciarConexao();
			PreparedStatement st = in.prepareStatement(sql);
			ResultSet result = st.executeQuery();
			List<SelectsChamadas1000xModel> lista = new LinkedList<SelectsChamadas1000xModel>();

			while (result.next()) {
				SelectsChamadas1000xModel tamTab = new SelectsChamadas1000xModel();

				tamTab.setCalls(result.getString("calls"));
				tamTab.setQuery(result.getString("querys"));
				tamTab.setTotal_exec_time(result.getString("total_exec_time"));
				tamTab.setDate(result.getString("data"));
				lista.add(tamTab);
			}
			return lista;
		} catch (SQLException e) {
			throw new SQLRunTimeException(e.getMessage(), e);
		} finally {
			desconecta();
		}
	}
}