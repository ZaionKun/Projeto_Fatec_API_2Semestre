package controllers;

import java.io.IOException;
import java.util.Properties;
import models.LoginModel;

public class Principal {

	public static void main(String[] args) throws IOException {
		LoginModel loginModel = LoginController.PreencherLogin();
		Menu menu = new Menu(loginModel);
		Properties prop = LoginController.getProp();
		String openMenu = prop.getProperty("openMenu");

		if (openMenu.equals("y")) {
			menu.startmenu();
		} else {
			ImprimeMetricas imprimeMetricas = new ImprimeMetricas(loginModel);
			imprimeMetricas.tamanhobancos();
			imprimeMetricas.tamanhoTabelas();
			imprimeMetricas.selectsChamadas1000x();
			imprimeMetricas.SelectMaisDemoradas();
			imprimeMetricas.selectsMaisDemoradasMedia();
			imprimeMetricas.conflicts();
		}
	}
}