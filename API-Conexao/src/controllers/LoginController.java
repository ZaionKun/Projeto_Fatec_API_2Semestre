package controllers;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.Properties;

import models.LoginModel;

public class LoginController {

	@SuppressWarnings("unused")
	private String driver = "org.postgresql.Driver";
	@SuppressWarnings("unused")
	private String caminho;
	private String porta;
	private String banco;
	@SuppressWarnings("unused")
	private String usuario;
	@SuppressWarnings("unused")
	private String senha;
	@SuppressWarnings("unused")
	private String caminholite;
	public Connection con;
	public Connection in;

	// Conexao do Postgres
	public LoginController(LoginModel login) {
		usuario = login.getUsuario();
		senha = login.getSenha();
		banco = login.getBanco();
		porta = login.getPorta();

		// Postgres
		caminho = "jdbc:postgresql://localhost:" + porta + "/" + banco;

		// SQLite
		caminholite = "jdbc:sqlite:banco_de_dados/banco_sqlite.db";
	}

	public static Properties getProp() throws IOException {
		Properties props = new Properties();
		FileInputStream file = new FileInputStream("C:/Vigilant/properties/login.config");
		props.load(file);
		return props;

	}

	public static LoginModel PreencherLogin() throws IOException {

		String usuario; // Variavel que armazena o login do servidor.
		String porta; // Variavel que armazena o host do servidor.
		String banco; // Variavel que armazena o nome do servidor.
		String senha; // Variável que armazena o password do usúario.

		Properties prop = getProp();

		usuario = prop.getProperty("usuario");
		porta = prop.getProperty("porta");
		senha = prop.getProperty("senha");
		banco = prop.getProperty("banco");

		LoginModel loginModel = new LoginModel();
		loginModel.setBanco(banco);
		loginModel.setPorta(porta);
		loginModel.setSenha(senha);
		loginModel.setUsuario(usuario);
		return loginModel;
	}
}