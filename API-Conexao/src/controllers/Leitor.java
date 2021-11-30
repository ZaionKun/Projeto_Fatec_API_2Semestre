package controllers;

import java.util.Scanner;

public class Leitor implements AutoCloseable {

	public Scanner scanner;

	// Construtor da Classe Controle
	public Leitor() {
		scanner = new Scanner(System.in);
	}

	// Função: lê e retorna a entrada do usuario (numeral inteiro)
	public int getValor() {
		scanner.reset();
		int op = Integer.valueOf(scanner.nextLine());
		return op;
	}

	// Função: lê e retorna a entrada do usuario (caractere)
	public String getTexto() {
		scanner.reset();
		String t = scanner.nextLine();
		return t;
	}

	// Sobrescrevendo o método da Classe Throwable
	@Override
	protected void finalize() throws Throwable {
		close();
	}

	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub
		scanner.close();
	}
}