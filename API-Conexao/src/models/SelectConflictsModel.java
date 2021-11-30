package models;

public class SelectConflictsModel {
	String tempo;
	String name;
	String Confl;
	String Confl_dead;

	public String getConfl() {
		return Confl;
	}

	public void setConfl(String confl) {
		this.Confl = confl;
	}

	public String getConfl_dead() {
		return Confl_dead;
	}

	public void setConfl_dead(String confl_dead) {
		this.Confl_dead = confl_dead;
	}

	public String getTempo() {
		return tempo;
	}

	public void setTempo(String tempo) {
		this.tempo = tempo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}