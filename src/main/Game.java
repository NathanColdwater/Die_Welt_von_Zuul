package main;

import java.util.HashMap;
import java.util.LinkedList;

import befehlsVerarbeitung.Befehl;
import befehlsVerarbeitung.Parser;
import character.NPC;
import character.Player;
import item.Item;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import ort.Landkarte;
import ort.Landscape;
import ort.Raum;

/**
 * Dies ist die Hauptklasse der Anwendung "Die Welt von Zuul". "Die Welt von
 * Zuul" ist ein sehr einfaches, textbasiertes Adventure-Game. Ein Spieler kann
 * sich in einer Umgebung bewegen, mehr nicht. Das Spiel sollte auf jeden Fall
 * ausgebaut werden, damit es interessanter wird!
 * 
 * Zum Spielen muss eine Instanz dieser Klasse erzeugt werden und an ihr die
 * Methode "spielen" aufgerufen werden.
 * 
 * Diese Instanz erzeugt und initialisiert alle anderen Objekte der Anwendung:
 * Sie legt alle R�ume und einen Parser an und startet das Spiel. Sie wertet
 * auch die Befehle aus, die der Parser liefert, und sorgt f�r ihre Ausf�hrung.
 * 
 * @author Michael K�lling und David J. Barnes
 * @version 2008.03.30
 */

public class Game {
	private Parser parser;
	private HashMap<String, Player> party = new HashMap<String, Player>();
	private HashMap<KeyCode, Runnable> actions = new HashMap<KeyCode, Runnable>();
	private Player spieler;
	private Landkarte land;
	private KampfSystem kampfSystem;
	private Textverwaltung tv;
	private GraphicsContext gc;

	/**
	 * Erzeuge ein Spiel und initialisiere die interne Raumkarte.
	 */
	public Game(GraphicsContext gc) {
		land = new Landkarte(gc);
		land.raeumeAnlegen();
		parser = new Parser();
		party.put("Dave", new Player("Dave", 100, land.getStartpoint(), 20, 20,
				Landkarte.linkToImage("/Bilder/Dave.png"), gc, null));
		spieler = party.get("Dave");
		spieler.setGeld(300);
		this.gc = gc;
		tv = new Textverwaltung(gc);

		setActions();
		willkommenstextAusgeben();
	}

	private void setActions() {
		actions.put(KeyCode.W, () -> {
			spieler.move(KeyCode.W);
		});

		actions.put(KeyCode.A, () -> {
			spieler.move(KeyCode.A);
		});

		actions.put(KeyCode.S, () -> {
			spieler.move(KeyCode.S);
		});

		actions.put(KeyCode.D, () -> {
			spieler.move(KeyCode.D);
		});
	}

	public boolean intersects(double x1, double y1, double w1, double h1, double x2, double y2, double w2, double h2) {
		return ((x1 + w1 > x2 && x1 < x2 + w2) && (y1 + h1 > y2 && y1 < y2 + h2));
	}

	/**
	 * Die Hauptmethode zum Spielen. L�uft bis zum Ende des Spiels in einer
	 * Schleife.
	 */
	public void update(HashMap<KeyCode, Boolean> keys) {
		for (KeyCode key : keys.keySet()) {
			try {
				actions.get(key).run();
			} catch (Exception ex) {
			}
		}

		gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

		spieler.getAktuellerRaum().show();

		Point2D pos = spieler.getPos();
		pos = new Point2D(pos.getX() - spieler.getW() / 2 , pos.getY() - spieler.getH() / 2);
		HashMap<String, Raum> ausgeange = spieler.getAktuellerRaum().getAusgaenge();
		if (ausgeange.get("north") != null) {
			gc.fillRect(300, 0, 200, 50);
			if (intersects(300, 0, 200, 50, pos.getX(), pos.getY(), spieler.getW(), spieler.getH())) {
				spieler.setPos(new Point2D(spieler.getPos().getX(), 700));
				wechsleRaum(new Befehl(null, "north"));
			}
		}

		if (ausgeange.get("east") != null) {
			gc.fillRect(750, 300, 50, 200);
			if (intersects(750, 300, 50, 200, pos.getX(), pos.getY(), spieler.getW(), spieler.getH())) {
				spieler.setPos(new Point2D(100, spieler.getPos().getY()));
				wechsleRaum(new Befehl(null, "east"));
			}
		}

		if (ausgeange.get("south") != null) {
			gc.fillRect(300, 750, 200, 50);
			if (intersects(300, 750, 200, 50, pos.getX(), pos.getY(), spieler.getW(), spieler.getH())) {
				spieler.setPos(new Point2D(spieler.getPos().getX(), 100));
				wechsleRaum(new Befehl(null, "south"));
			}
		}

		if (ausgeange.get("west") != null) {
			gc.fillRect(0, 300, 50, 200);
			if (intersects(0, 300, 50, 200, pos.getX(), pos.getY(), spieler.getW(), spieler.getH())) {
				spieler.setPos(new Point2D(700, spieler.getPos().getY()));
				wechsleRaum(new Befehl(null, "west"));
			}
		}

		spieler.show();
		tv.refresh();
	}

	/**
	 * Einen Begr��ungstext f�r den Spieler ausgeben.
	 */
	private void willkommenstextAusgeben() {
		tv.addText("Willkommen zu Zuul!");
		tv.addText("Tippen sie 'help', wenn Sie Hilfe brauchen.");
		tv.addText();
		tv.addText("Seltsame Ereignisse haben ihre Schatten vorausgeworfen."
				+ System.getProperty("line.separator")
				+ "�ber Nacht viel der Goldpreis auf 3 US-Dollar pro Feinunze und die Menscheit strebte nach einen neuen Wertanlage:"
				+ System.getProperty("line.separator") + "Lutetium!" + System.getProperty("line.separator")
				+ "Niemand wei�, was dann geschah. Vielleicht gruben wir zu tief, vielleicht lie�en wir uns auf falsche G�tzen ein,"
				+ System.getProperty("line.separator") + "fest steht, dass das Ende der Welt �ber uns kam."
				+ System.getProperty("line.separator")
				+ "Nur hatten die Propheten keine Ahnung, wie seltsam das Ende werden w�rde."
				+ System.getProperty("line.separator")
				+ "Jetzt stehst du alleine vor der Universit�t. In der Ferne schnurrt eine Katze."
				+ System.getProperty("line.separator") + "");
		System.out.println();
		// System.out.println(spieler.getAktuellerRaum().getLongDesciption());
	}

	/**
	 * Verarbeite einen gegebenen Befehl (f�hre ihn aus).
	 * 
	 * @param befehl
	 *            Der zu verarbeitende Befehl.
	 * @return 'true', wenn der Befehl das Spiel beendet, 'false' sonst.
	 */
	private boolean verarbeiteBefehl(Befehl befehl) {
		boolean moechteBeenden = false;

		if (befehl.istUnbekannt()) {
			System.out.println("Ich wei� nicht, was Sie meinen...");
			return false;
		}

		String befehlswort = befehl.gibBefehlswort();
		if (befehlswort.equalsIgnoreCase("help")) {
			hilfstextAusgeben();
		} else if (befehlswort.equalsIgnoreCase("go")) {
			//TODO fix no more zustand.
			/*if (spieler.getZustand().isMovable())
				wechsleRaum(befehl);
			else
				System.out.println("Sie k�nnen sich nicht bewegen!");*/
		} else if (befehlswort.equalsIgnoreCase("quit")) {
			moechteBeenden = beenden(befehl);
		} else if (befehlswort.equalsIgnoreCase("look")) {
			lookAround(befehl.gibZweitesWort());
		} else if (befehlswort.equalsIgnoreCase("take")) {
			nimmGegenstand(befehl.gibZweitesWort());
		} else if (befehlswort.equalsIgnoreCase("drop")) {
			legeGegenstandAb(befehl.gibZweitesWort());
		} else if (befehlswort.equalsIgnoreCase("inventory")) {
			System.out.println(spieler.getInventory());
		} else if (befehlswort.equalsIgnoreCase("eat")) {
			eat(befehl.gibZweitesWort());
		} else if (befehlswort.equalsIgnoreCase("heal")) {
			//TODO fix no more zustand. 
			//spieler.kleineHeilung(spieler);
		} else if (befehlswort.equalsIgnoreCase("hurt")) {
			//TODO fix no more zustand. 
			//pieler.leichtVerletzen(spieler);
		} else if (befehlswort.equalsIgnoreCase("stab")) {
			//TODO fix no more zustand. 
			//spieler.schwerVerletzen(spieler);
		} else if (befehlswort.equalsIgnoreCase("suicide")) {
			//TODO fix no more zustand. 
			//spieler.toeten(spieler);
		} else if (befehlswort.equalsIgnoreCase("use")) {
			nutzeLandschaft(befehl.gibZweitesWort());
		} else if (befehlswort.equalsIgnoreCase("smallRevival")) {
			//TODO fix no more zustand. 
			//spieler.kleineWiederbelebung(spieler);
		} else if (befehlswort.equalsIgnoreCase("largeRevial")) {
			//TODO fix no more zustand. 
			//spieler.grosseWiederbelebung(spieler);
		} else if (befehlswort.equalsIgnoreCase("talk")) {
			talk(befehl.gibZweitesWort());
		} else if (befehlswort.equalsIgnoreCase("status")) {
			System.out.println(spieler.getStatus());
		} else if (befehlswort.equalsIgnoreCase("changePlayer")) {
			changePlayer(befehl.gibZweitesWort());
		}
		return moechteBeenden;
	}

	public void changePlayer(String name) {
		Player zw = party.get(name);
		if (zw != null) {
			spieler = zw;
			System.out.println("du spielst nun als " + name + ".");
		} else {
			System.out.println("Diesen Spieler gibt es nicht!");
		}
	}

	public void talk(String name) {
		NPC npc = spieler.getAktuellerRaum().getNPC(name);
		if (npc != null) {
			npc.interagieren(spieler);
		} else {
			System.out.println("Diesen NPC gibt es nicht!");
		}
	}

	public void eat(String name) {
		Item gs = spieler.eat(name);
		if (gs != null) {
			System.out.println(gs.getName() + " gegessen");
		} else {
			System.out.println("Gegenstand " + name + " existiert nicht oder ist nicht essbar!");
		}
	}

	public void nutzeLandschaft(String name) {
		Landscape ls = spieler.getAktuellerRaum().getLandschaft(name);
		if (ls != null) {
			ls.onUse(spieler);
		}
	}

	public void nimmGegenstand(String name) {
		Item gs = spieler.getAktuellerRaum().getGegenstand(name);
		if (gs != null) {
			if (spieler.gegenstandAufnehmen(gs)) {
				spieler.getAktuellerRaum().gegenstandAufheben(name);
				System.out.println(gs.getName() + " Aufgehoben");
			} else {
				System.out.println("Der Gegenstand " + gs.getName() + " ist zu schwer");
			}
		}
	}

	public void legeGegenstandAb(String name) {
		Item gs = spieler.gegenstandAblegen(name);
		if (gs != null) {
			spieler.getAktuellerRaum().gegenstandAblegen(gs);
			System.out.println(gs.getName() + " abgelegt");
		} else {
			System.out.println("Den Gegenstand " + name + " gibt es in deinem Inventar nicht");
		}
	}

	// Implementierung der Benutzerbefehle:

	/**
	 * Gib Hilfsinformationen aus. Hier geben wir eine etwas alberne und unklare
	 * Beschreibung aus, sowie eine Liste der Befehlsw�rter.
	 */
	private void hilfstextAusgeben() {
		System.out.println("Sie haben sich verlaufen. Sie sind allein.");
		System.out.println("Sie irren auf dem Unigel�nde herum.");
		System.out.println();
		System.out.println("Ihnen stehen folgende Befehle zur Verf�gung:");
		System.out.println("	" + parser.getBefehle());
	}

	private void lookAround(String item) {
		if (item != null) {
			Item gs = spieler.getGegenstand(item);
			gs = gs != null ? gs : spieler.getAktuellerRaum().getGegenstand(item);
			if (gs != null) {
				System.out.println(gs.getBeschreibung());
			} else {
				System.out.println("Diesen Gegenstand gibt es nicht in diesem Raum oder deinem Inventar");
			}
		} else {
			System.out.println(spieler.getAktuellerRaum().getLongDesciption());
		}
	}

	/**
	 * Versuche, den Raum zu wechseln. Wenn es einen Ausgang gibt, wechsele in den
	 * neuen Raum, ansonsten gib eine Fehlermeldung aus.
	 */
	private void wechsleRaum(Befehl befehl) {
		if (!befehl.hatZweitesWort()) {
			// Gibt es kein zweites Wort, wissen wir nicht, wohin...
			System.out.println("Wohin m�chten Sie gehen?");
			return;
		}

		String richtung = befehl.gibZweitesWort();

		// Wir versuchen den Raum zu verlassen.
		Raum naechsterRaum = spieler.getAktuellerRaum().getAusgang(richtung);

		if (naechsterRaum == null) {
			tv.addText("Dort ist keine T�r!");
		} else {
			spieler.setAktuellerRaum(naechsterRaum);
			tv.addText(spieler.getAktuellerRaum().getLongDesciption());
			LinkedList<character.Character> spielerGroup = new LinkedList<character.Character>();
			spielerGroup.add(spieler);
			//kampfSystem = new KampfSystem(spielerGroup, naechsterRaum.getGegnerList());
			naechsterRaum.onEnterRoomEvent(spieler);
			//if (kampfSystem.checkKampfStart(naechsterRaum)) {
			//	kampfSystem.startKampf();
			//}
		}
	}

	/**
	 * "quit" wurde eingegeben. �berpr�fe den Rest des Befehls, ob das Spiel
	 * wirklich beendet werden soll.
	 * 
	 * @return 'true', wenn der Befehl das Spiel beendet, 'false' sonst.
	 */
	private boolean beenden(Befehl befehl) {
		if (befehl.hatZweitesWort()) {
			System.out.println("Was soll beendet werden?");
			return false;
		} else {
			return true; // Das Spiel soll beendet werden.
		}
	}
}