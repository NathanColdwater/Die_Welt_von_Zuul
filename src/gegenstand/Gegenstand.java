package gegenstand;

public abstract class Gegenstand {
	private String name, beschreibung;
	private int gewicht;
	private boolean essbar = false;
	
	public Gegenstand(String name, String beschreibung, int gewicht, boolean essbar) {
		this.name = name;
		this.beschreibung = beschreibung;
		this.gewicht = gewicht;
		this.essbar = essbar;
	}

	public String toString() {
		return "Name: " + name + System.getProperty("line.separator") + "Beschreibung: " + beschreibung
				+ System.getProperty("line.separator") + "Gewicht: " + gewicht;
	}

	public String getName() {
		return name;
	}

	public String getBeschreibung() {
		return beschreibung;
	}

	public int getGewicht() {
		return gewicht;
	}

	public boolean isEssbar() {
		return essbar;
	}

	public void setEssbar(boolean essbar) {
		this.essbar = essbar;
	}
}