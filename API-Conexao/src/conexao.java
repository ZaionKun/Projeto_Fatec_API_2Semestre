import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class conexao {
	private String url;
	private String usuario;
	private String senha;
	private Connection con;
	
	public conexao(){
		url = "jdbc:postgresql://localhost:5432/teste";
		usuario = "postgres";
		senha = "toto190100";
		// Necessário alterar a url, usuario e senha para o banco que será conectado
		
		try {
			Class.forName("org.postgresql.Driver");
			con = DriverManager.getConnection(url,usuario,senha);
			System.out.println("Conexão realizada com sucesso!!!");
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//ExibirSelectTamanhoBanco(con);
		//ExibirSelect10MaisDemoradas(con);
		//ExibirSelect10MaisDemoradasEmMedia(con);
		//ExibirSelectsChamadasMaisDe1000x(con);
		ExibirTamanhoTabelas(con);
		
		
	}
	public static void ExibirTamanhoTabelas(Connection con) {
		String sql = "SELECT esquema, tabela,\r\n"
				+ "       pg_size_pretty(pg_relation_size(esq_tab)) AS tamanho,\r\n"
				+ "       pg_size_pretty(pg_total_relation_size(esq_tab)) AS tamanho_total\r\n"
				+ "  FROM (SELECT tablename AS tabela,\r\n"
				+ "               schemaname AS esquema,\r\n"
				+ "               schemaname||'.'||tablename AS esq_tab\r\n"
				+ "          FROM pg_catalog.pg_tables\r\n"
				+ "         WHERE schemaname NOT\r\n"
				+ "            IN ('pg_catalog', 'information_schema', 'pg_toast') ) AS x\r\n"
				+ " ORDER BY pg_total_relation_size(esq_tab) DESC;";
		
		try {
			PreparedStatement pesquisa = con.prepareStatement(sql);
			ResultSet result = pesquisa.executeQuery();
			
			while(result.next()) {
				System.out.println("==========================================================");
				System.out.println("NOME: " + result.getString("tabela") + "\n");
				System.out.println("TAMANHO: "+result.getString("tamanho") + "\n");
				System.out.println("TAMANHO TOTAL: " + result.getString("tamanho_total")); //Tempo somado de todas as selects
				System.out.println("==========================================================");
			}
		}
		catch(Exception e) {
			
		}
	}
	
	
	public static void ExibirSelectsChamadasMaisDe1000x(Connection con) {
		String sql = "SELECT calls, query, total_exec_time\r\n"
				+ "FROM pg_stat_statements\r\n"
				+ "where calls > 1000;";
		
		try {
			PreparedStatement pesquisa = con.prepareStatement(sql);
			ResultSet result = pesquisa.executeQuery();
			
			while(result.next()) {
				System.out.println("==========================================================");
				System.out.println("TOTAL DE CHAMADAS: " + result.getString("calls") + "\n");
				System.out.println(result.getString("query") + "\n");
				System.out.println("TEMPO TOTAL: " + result.getString("total_exec_time")+"ms"); //Tempo somado de todas as selects
				System.out.println("==========================================================");
			}
		}
		catch(Exception e) {
			
		}
	}
	
	public static void ExibirSelect10MaisDemoradasEmMedia(Connection con) {
		String sql = "SELECT mean_exec_time, query\r\n"
				+ "FROM pg_stat_statements\r\n"
				+ "ORDER BY mean_exec_time\r\n"
				+ "DESC LIMIT 10;";
		
		try {
			PreparedStatement pesquisa = con.prepareStatement(sql);
			ResultSet result = pesquisa.executeQuery();
			
			while(result.next()) {
				System.out.println("==========================================================");
				System.out.println(result.getString("query") + "\n");
				System.out.println("TEMPO TOTAL MÉDIO: " + result.getString("mean_exec_time")+"ms");
				System.out.println("==========================================================");
			}
		}
		catch(Exception e) {
			
		}
	}
	
	public static void ExibirSelectTamanhoBanco(Connection con) {
		String sql = "SELECT pg_database.datname, pg_size_pretty(pg_database_size(pg_database.datname)) AS size FROM pg_database;";
		
		try {
			PreparedStatement pesquisa = con.prepareStatement(sql);
			ResultSet result = pesquisa.executeQuery();
			
			while(result.next()) {
				System.out.println(result.getString("datname") + " " + result.getString("size"));
			}
		}
		catch(Exception e) {
			
		}
	}
	
	public static void ExibirSelect10MaisDemoradas(Connection con) {
		String sql = "SELECT total_exec_time, query\r\n"
				+ "FROM pg_stat_statements\r\n"
				+ "ORDER BY total_exec_time\r\n"
				+ "DESC LIMIT 10;";
		
		try {
			PreparedStatement pesquisa = con.prepareStatement(sql);
			ResultSet result = pesquisa.executeQuery();
			
			while(result.next()) {
				System.out.println("==========================================================");
				System.out.println(result.getString("query") + "\n");
				System.out.println("TEMPO TOTAL: " + result.getString("total_exec_time"));
				System.out.println("==========================================================");
			}
		}
		catch(Exception e) {
			
		}
	}
	
	
	public static void main(String[] args) {
		@SuppressWarnings("unused")
		conexao con = new conexao();
	}
}