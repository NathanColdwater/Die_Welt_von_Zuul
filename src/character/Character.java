package character;

import java.util.LinkedList;

import Verhalten.AngriffsVerhalten;
import Verhalten.NPCAngriffVerhalten;
import item.Brustplatte;
import item.Gegenstand;
import item.Hand;
import item.Helm;
import item.Hose;
import item.Schuhe;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import main.ZuulUI;
import ort.Raum;

public abstract class Character {
	protected Raum aktuellerRaum;
	protected int maxTraglast, traglast, geld;
	protected LinkedList<Gegenstand> gegenstaende;
	protected AngriffsVerhalten angriffsVerhalten;
	protected Point2D pos;
	protected Image image;
	
	protected String name;
	protected String beschreibung;

	@SuppressWarnings("unchecked")
	public Character(String name, int maxTraglast, Raum raum, int x, int y, Image image, LinkedList<Gegenstand> gegenstaende) {
		this.name = name;
		this.aktuellerRaum = raum;
		this.maxTraglast = maxTraglast;
		this.gegenstaende = gegenstaende == null ? new LinkedList<Gegenstand>()
				: (LinkedList<Gegenstand>) gegenstaende.clone();
		traglast = ermittleGewicht();
		angriffsVerhalten = NPCAngriffVerhalten.getInstance();
		pos = new Point2D(x, y);
		this.image = image;
	}
	
	public void show() {
		double x = pos.getX() - image.getWidth() * 0.5;
		double y = pos.getY() - image.getHeight() * 0.5;
		ZuulUI.gc.drawImage(image, x, y);
	}

	public abstract void interagieren(Spieler spieler);

	public boolean gegenstandAufnehmen(Gegenstand gegenstand) {
		if (traglast + gegenstand.getGewicht() <= maxTraglast) {
			gegenstaende.add(gegenstand);
			traglast += gegenstand.getGewicht();
			return true;
		} else {
			return false;
		}
	}

	public Gegenstand gegenstandAblegen(String name) {
		Gegenstand gs = getGegenstand(name);
		if (gs != null) {
			gegenstaende.remove(gs);
			traglast = ermittleGewicht();
		}
		return gs;
	}

	public Gegenstand eat(String name) {
		Gegenstand essen = getGegenstand(name);
		if (essen != null && essen.isEssbar()) {
			gegenstaende.remove(essen);
			traglast = ermittleGewicht();
			if (essen.getName().equalsIgnoreCase("muffin")) {
				maxTraglast += 10;
			}
		} else {
			essen = null;
		}
		return essen;
	}

	public int ermittleGewicht() {
		int gewicht = 0;
		for (Gegenstand gs : gegenstaende) {
			gewicht += gs.getGewicht();
		}
		return gewicht;
	}

	public Gegenstand getGegenstand(String name) {
		for (Gegenstand gs : gegenstaende) {
			if (gs.getName().equalsIgnoreCase(name)) {
				return gs;
			}
		}
		return null;
	}

	public String getInventory() {
		StringBuilder sb = new StringBuilder();
		int i = 1;
		sb.append("Gegenstšnde im Inventar: ");
		sb.append(System.getProperty("line.separator"));
		for (Gegenstand gs : gegenstaende) {
			sb.append(i);
			sb.append(". Gegenstand: ");
			sb.append(System.getProperty("line.separator"));
			sb.append("Name: ");
			sb.append(gs.getName());
			sb.append(System.getProperty("line.separator"));
			sb.append("Gewicht: ");
			sb.append(gs.getGewicht());
			sb.append(System.getProperty("line.separator"));
			sb.append("Preis: ");
			sb.append(gs.getPreis());
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			i++;
		}
		return sb.toString();
	}
	
	public String getStatus() {
		StringBuilder sb = new StringBuilder();
		sb.append("Gewicht: ");
		sb.append(traglast);
		sb.append("/");
		sb.append(maxTraglast);
		sb.append(System.getProperty("line.separator"));
		sb.append("Geld: ");
		sb.append(geld);
		sb.append(System.getProperty("line.separator"));
		sb.append("Zustand: ");
		//TODO fix no more zustand.
		//sb.append(zustand.getName());
		sb.append(System.getProperty("line.separator"));
		return sb.toString();
	}


	public Raum getAktuellerRaum() {
		return aktuellerRaum;
	}

	public void setAktuellerRaum(Raum aktuellerRaum) {
		this.aktuellerRaum = aktuellerRaum;
	}

	public int getMaxTraglast() {
		return maxTraglast;
	}

	public int getTraglast() {
		return traglast;
	}

	public LinkedList<Gegenstand> getGegenstaende() {
		return gegenstaende;
	}

	public String getName() {
		return name;
	}

	public String getBeschreibung() {
		return beschreibung;
	}

	public void setTraglast(int traglast) {
		this.traglast = traglast;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setBeschreibung(String beschreibung) {
		this.beschreibung = beschreibung;
	}

	public int getGeld() {
		return geld;
	}

	public void setGeld(int geld) {
		this.geld = geld;
	}

	public Point2D getPos() {
		return pos;
	}
	
	public double getW() {
		return image.getWidth();
	}
	
	public double getH() {
		return image.getHeight();
	}

	public void setPos(Point2D pos) {
		this.pos = pos;
	}
}
