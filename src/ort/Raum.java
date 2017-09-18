package ort;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import character.Gegner;
import character.NPC;
import character.Spieler;
import item.Gegenstand;
import javafx.scene.image.Image;
import main.ZuulUI;

/**
 * Diese Klasse modelliert R�ume in der Welt von Zuul.
 * 
 * Ein "Raum" repr�sentiert einen Ort in der virtuellen Landschaft des Spiels.
 * Ein Raum ist mit anderen R�umen �ber Ausg�nge verbunden. M�gliche Ausg�nge
 * liegen im Norden, Osten, S�den und Westen. F�r jede Richtung h�lt ein Raum
 * eine Referenz auf den benachbarten Raum.
 * 
 * @author Michael Kolling and David J. Barnes
 * @version 2008.03.30
 */
public class Raum {
	private String beschreibung;
	private Landkarte land;
	private HashMap<String, Raum> ausgaenge = new HashMap<String, Raum>();
	private LinkedList<Gegenstand> gegenstaende = new LinkedList<Gegenstand>();
	private ArrayList<Landscape> landschaft = new ArrayList<Landscape>();
	private LinkedList<Gegner> gegner = new LinkedList<Gegner>();
	private LinkedList<NPC> npc = new LinkedList<NPC>();
	private Image bg;

	/**
	 * Erzeuge einen Raum mit einer Beschreibung. Ein Raum hat anfangs keine
	 * Ausg�nge.
	 * 
	 * @param beschreibung
	 *            enth�lt eine Beschreibung in der Form "in einer K�che" oder "auf
	 *            einem Sportplatz".
	 */
	public Raum(String beschreibung, Landkarte land, Image bg) {
		this.beschreibung = beschreibung;
		this.land = land;
		this.bg = bg;
	}

	/**
	 * Definiere die Ausg�nge dieses Raums. Jede Richtung f�hrt entweder in einen
	 * anderen Raum oder ist 'null' (kein Ausgang).
	 * 
	 * @param norden
	 *            Der Nordeingang.
	 * @param osten
	 *            Der Osteingang.
	 * @param sueden
	 *            Der S�deingang.
	 * @param westen
	 *            Der Westeingang.
	 */
	public void setzeAusgang(String richtung, Raum raum) {
		ausgaenge.put(richtung, raum);
	}
	
	public void show() {
		ZuulUI.gc.drawImage(bg, 0, 0);
		
		for (Gegenstand gs : gegenstaende) {
			gs.show();
		}
		
		for (Landscape ls : landschaft) {
			ls.show();
		}
		
		for (NPC np :npc) {
			np.show();
		}
		
		for (Gegner g : gegner) {
			g.show();
		}
	}

	public Raum getAusgang(String richtung) {
		return ausgaenge.get(richtung);
	}

	public String ausgaengeToString() {
		StringBuilder sb = new StringBuilder("");
		for (String key : ausgaenge.keySet()) {
			sb.append(key);
			sb.append(" ");
		}
		return sb.toString();
	}

	public void gegenstandAblegen(Gegenstand gegenstand) {
		gegenstaende.add(gegenstand);
	}

	public void setzeGegner(Gegner gegner) {
		this.gegner.add(gegner);
	}

	public void enterneGegner(Gegner gegner) {
		this.gegner.remove(gegner);
	}
	
	public void setzeNPC(NPC npc) {
		this.npc.add(npc);
	}

	public void enterneNPC(NPC npc) {
		this.npc.remove(npc);
	}
	
	public NPC getNPC(String name) {
		for (NPC np : npc) {
			if (np.getName().equalsIgnoreCase(name)) {
				return np;
			}
		}
		return null;
	}

	public void landschaftBauen(Landscape landscape) {
		landschaft.add(landscape);
		landscape.setRaum(this);
	}
	
	public void landschaftEntfernen(String name) {
		for (Landscape ls : landschaft) {
			if (ls.getName().equalsIgnoreCase(name)) {
				landschaft.remove(ls);
				break;
			}
		}
	}

	public Gegenstand gegenstandAufheben(String name) {
		for (Gegenstand gs : gegenstaende) {
			if (gs.getName().equalsIgnoreCase(name)) {
				gegenstaende.remove(gs);
				return gs;
			}
		}
		return null;
	}

	public Gegenstand getGegenstand(String name) {
		for (Gegenstand gs : gegenstaende) {
			if (gs.getName().equalsIgnoreCase(name)) {
				return gs;
			}
		}
		return null;
	}

	public Landscape getLandschaft(String name) {
		for (Landscape ls : landschaft) {
			if (ls.getName().equalsIgnoreCase(name)) {
				return ls;
			}
		}
		return null;
	}

	public void onEnterRoomEvent(Spieler spieler) {
		for (int i = landschaft.size() - 1; i >= 0; i--) {
			Landscape ls = landschaft.get(i); 
			ls.onEnterRoom(spieler);
		}
	}

	public String gegenstaendeToString() {
		StringBuilder sb = new StringBuilder("");
		for (Gegenstand gegenstand : gegenstaende) {
			sb.append(gegenstand.getName());
			sb.append(" ");
		}
		return sb.toString();
	}

	public String landschaftToString() {
		StringBuilder sb = new StringBuilder("");
		for (Landscape landscape : landschaft) {
			sb.append(landscape.getName());
			sb.append(" ");
		}
		return sb.toString();
	}

	private String gegnerToString() {
		StringBuilder sb = new StringBuilder("");
		for (Gegner gn : gegner) {
			sb.append(gn.getName());
			sb.append(" ");
		}
		return sb.toString();
	}

	private String npcsToString() {
		StringBuilder sb = new StringBuilder("");
		for (NPC np : npc) {
			sb.append(np.getName());
			sb.append(" ");
		}
		return sb.toString();
	}

	public String getLongDesciption() {
		StringBuilder sb = new StringBuilder("");
		sb.append(gibBeschreibung());
		sb.append(System.getProperty("line.separator"));
		sb.append("Hier ist: ");
		sb.append(landschaftToString());
		sb.append(System.getProperty("line.separator"));
		sb.append("Gegenst�nde: ");
		sb.append(gegenstaendeToString());
		sb.append(System.getProperty("line.separator"));
		sb.append("Ausg�nge: ");
		sb.append(ausgaengeToString());
		sb.append(System.getProperty("line.separator"));
		sb.append("Gegner: ");
		sb.append(gegnerToString());
		sb.append(System.getProperty("line.separator"));
		sb.append("NPC's: ");
		sb.append(npcsToString());
		return sb.toString();
	}

	/**
	 * @return Die Beschreibung dieses Raums.
	 */
	public String gibBeschreibung() {
		return beschreibung;
	}

	public Landkarte getLand() {
		return land;
	}

	public LinkedList<Gegner> getGegnerList() {
		return gegner;
	}

	public HashMap<String, Raum> getAusgaenge() {
		return ausgaenge;
	}
}