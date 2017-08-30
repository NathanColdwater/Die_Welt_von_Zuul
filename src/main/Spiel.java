package main;
import befehlsVerarbeitung.Befehl;
import befehlsVerarbeitung.Parser;
import character.Spieler;
import gegenstand.Gegenstand;
import ort.Raum;

/**
 * Dies ist die Hauptklasse der Anwendung "Die Welt von Zuul".
 * "Die Welt von Zuul" ist ein sehr einfaches, textbasiertes Adventure-Game. Ein
 * Spieler kann sich in einer Umgebung bewegen, mehr nicht. Das Spiel sollte auf
 * jeden Fall ausgebaut werden, damit es interessanter wird!
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

public class Spiel {
	private Parser parser;
	private Spieler spieler;

	/**
	 * Erzeuge ein Spiel und initialisiere die interne Raumkarte.
	 */
	public Spiel() {
		raeumeAnlegen();
		parser = new Parser();
	}

	/**
	 * Erzeuge alle R�ume und verbinde ihre Ausg�nge miteinander.
	 */
	private void raeumeAnlegen() {
		Raum draussen, hoersaal, cafeteria, labor, buero, keller;
		Gegenstand regenschirm, tasse, messer, erlenmeyerkolben, ventilator, peitsche, muffin;

		// die R�ume erzeugen
		draussen = new Raum("vor dem Haupteingang der Universit�t");
		hoersaal = new Raum("in einem Vorlesungssaal");
		cafeteria = new Raum("in der Cafeteria der Uni");
		labor = new Raum("in einem Rechnerraum");
		buero = new Raum("im Verwaltungsb�ro der Informatik");
		keller = new Raum("im Keller des Rechenzentrums");
		
		regenschirm = new Gegenstand("Schirmy", "Ein pinker Regenschirm", 5);
		tasse = new Gegenstand("Tasse", "Auf Ihr Steht: '#1 Dad'", 2);
		messer = new Gegenstand("Messer", "Es hat 'Made with Kinderarbeit' aufgedruckt", 1);
		erlenmeyerkolben = new Gegenstand("Erlenmeyerkolben", "Die Fl�ssigkeit darin riecht Alkoholisch", 3);
		ventilator = new Gegenstand("Ventilator", "F�r die schwitzige Jahreszeit", 30);
		peitsche = new Gegenstand("Peitsche", "Sie hat 'BDSM' eingraviert", 10);
		muffin = new Gegenstand("Muffin", "Er glitzert :O", 3);
		muffin.setEssbar(true);

		// die Ausg�nge initialisieren
		draussen.setzeAusgang("east", hoersaal);
		draussen.setzeAusgang("south", labor);
		draussen.setzeAusgang("west", cafeteria);
		draussen.gegenstandAblegen(regenschirm);
		
		hoersaal.setzeAusgang("west", draussen);
		hoersaal.gegenstandAblegen(tasse);
		
		cafeteria.setzeAusgang("east", draussen);
		cafeteria.gegenstandAblegen(messer);
		
		labor.setzeAusgang("north", draussen);
		labor.setzeAusgang("east", buero);
		labor.setzeAusgang("down", keller);
		labor.gegenstandAblegen(erlenmeyerkolben);
		labor.gegenstandAblegen(muffin);
		
		buero.setzeAusgang("west", labor);
		buero.gegenstandAblegen(ventilator);
		
		keller.setzeAusgang("up", labor);
		keller.gegenstandAblegen(peitsche);
		
		spieler = new Spieler(100, draussen, null); // das Spiel startet draussen
	}

	/**
	 * Die Hauptmethode zum Spielen. L�uft bis zum Ende des Spiels in einer
	 * Schleife.
	 */
	public void spielen() {
		willkommenstextAusgeben();

		// Die Hauptschleife. Hier lesen wir wiederholt Befehle ein
		// und f�hren sie aus, bis das Spiel beendet wird.

		boolean beendet = false;
		while (!beendet) {
			Befehl befehl = parser.liefereBefehl();
			beendet = verarbeiteBefehl(befehl);
		}
		System.out.println("Danke f�r dieses Spiel. Auf Wiedersehen.");
	}

	/**
	 * Einen Begr��ungstext f�r den Spieler ausgeben.
	 */
	private void willkommenstextAusgeben() {
		System.out.println("Willkommen zu Zuul!");
		System.out.println("Tippen sie 'help', wenn Sie Hilfe brauchen.");
		System.out.println();
		System.out.println(spieler.getAktuellerRaum().getLongDesciption());
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
			wechsleRaum(befehl);
		} else if (befehlswort.equalsIgnoreCase("quit")) {
			moechteBeenden = beenden(befehl);
		} else if (befehlswort.equalsIgnoreCase("look")) {
			lookAround(befehl.gibZweitesWort());
		} else if (befehlswort.equalsIgnoreCase("take")) {
			nimmGegenstand(befehl.gibZweitesWort());
		} else if (befehlswort.equalsIgnoreCase("drop")) {
			legeGegenstandAb(befehl.gibZweitesWort());
		} else if (befehlswort.equalsIgnoreCase("inventory")) {
			System.out.println(spieler.showStatus());
		} else if (befehlswort.equalsIgnoreCase("eat")) {
			eat(befehl.gibZweitesWort());
		} else if (befehlswort.equalsIgnoreCase("heal")) {
			spieler.heilen();
		} else if (befehlswort.equalsIgnoreCase("hurt")) {
			spieler.leichtVerletzen();
		} else if (befehlswort.equalsIgnoreCase("stab")) {
			spieler.schwerVerletzen();
		}
		return moechteBeenden;
	}
	
	public void eat(String name) {
		Gegenstand gs = spieler.eat(name);
		if (gs != null) {
			System.out.println(gs.getName() + " gegessen");
		} else {
			System.out.println("Gegenstand " + name + " existiert nicht oder ist nicht essbar!");
		}
	}
	
	public void nimmGegenstand(String name) {
		Gegenstand gs = spieler.getAktuellerRaum().getGegenstand(name);
		if (gs != null) {
			if (spieler.gegenstandAufnehmen(gs)) {
				System.out.println(gs.getName() + " Aufgehoben");
			} else {
				System.out.println("Der Gegenstand " + gs.getName() + " ist zu schwer");
			}
		}
	}
	
	public void legeGegenstandAb(String name) {
		Gegenstand gs = spieler.gegenstandAblegen(name);
		if (gs != null) {
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
			Gegenstand gs = spieler.getGegenstand(item);
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
	 * Versuche, den Raum zu wechseln. Wenn es einen Ausgang gibt, wechsele in
	 * den neuen Raum, ansonsten gib eine Fehlermeldung aus.
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
			System.out.println("Dort ist keine T�r!");
		} else {
			spieler.setAktuellerRaum(naechsterRaum);
			System.out.println(spieler.getAktuellerRaum().getLongDesciption());
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
