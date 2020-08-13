/*
 * Copyright (c)
 * 
 * Ersteller: Tim Muehle und Moritz Wolter
 * 
 * Entwicklungsprojekt im Auftrag von Professorin K. Brabender und Herrn A. Koch
 * Entwickelt für das AID-Labor der Hochschule Bochum
 * 
 */

package bitchanger.gui.views;

import java.util.HashMap;
import java.util.Map;

import bitchanger.gui.controller.Controller;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

/**	<!-- $LANGUAGE=DE -->
 * 
 * Basis-Klasse für alle View-Klassen, die das Interface {@code Viewable} implementieren und mit einem
 * Controller eine Funktion erhalten.
 * <p>
 * Die grundlegenden Methoden der Schnittstellen {@code Viewable} und {@code Controllable} sind bereits implementiert.
 * Die dazugehörigen Attribute sind mit protected geschützt, damit diese direkt in Unterklassen verwendet werden können.
 * </p>
 * <p>
 * Es werden abstrakte Methoden definiert, die zur Erstellung des Scenegraphens benötigt werden.
 * </p>
 * 
 * @param <T> Typ des Wurzelknotens im Scenegraphen
 * 
 * @author Tim Mühle
 * 
 * @since Bitchanger 0.1.0
 * @version 0.1.4
 *
 */
public abstract class ViewBase<T extends Parent> implements Viewable {
	
// Attribute	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	/** <!-- $LANGUAGE=DE --> Szene, die von der View repräsentiert wird */
	protected Scene scene;
	
	/** <!-- $LANGUAGE=DE --> Wurzelknoten im Scenegraph */
	protected T root;
	
	/** <!-- $LANGUAGE=DE -->
	 * {@code Map}, in die alle vom Controller benötigten Textfelder der View 
	 * mit einem eindeutigen Schlüssel abgelegt werden */
	private Map<String, TextField> tfMap;	// Hiermit koennen die entsprechenden TFs direkt gesucht werden -> hilfreich fuer Actions!
	
	/** <!-- $LANGUAGE=DE --> 
	 * {@code Map}, in die alle vom Controller benötigten Buttons der View mit 
	 * einem eindeutigen Schlüssel abgelegt werden */
	private Map<String, Button> btnMap;
	
	/** <!-- $LANGUAGE=DE --> 
	 * {@code Map}, in die alle vom Controller benötigten Elemente der View mit einem 
	 * eindeutigen Schlüssel abgelegt werden, die keine Buttons oder Textfelder sind */
	private Map<String, Node> nodeMap;
	
	/** <!-- $LANGUAGE=DE --> MenuBar des Scenegraphen */
	private MenuBar menubar;
	
	/** <!-- $LANGUAGE=DE --> 
	 * Controller, der die Funktion zu den Bedienelementen hinzufügt. 
	 */
	protected Controller controller;
	
	
// Konstruktor	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	/** <!-- $LANGUAGE=DE -->
	 * Erzeugt eine neue View, die eine neue {@code Scene} mit dem übergebenen Wurzelknoten {@code root} kapselt.
	 * <p>
	 * Nach der Initialisierung der Attribute werden nacheinander die Methoden {@link #init()} und {@link #createScenegraph()} 
	 * aufgerufen, um den Scenegraphen zu konstruieren und im Anschluss wird mit Hilfe eines Controllers die Funktion der Bedienelemente 
	 * hinzugefügt, wenn ein passender Controller registriert wurde.
	 * </p>
	 * 
	 * @param root	Wurzelknoten des Scenegraphen
	 * 
	 * @see #init()
	 * @see #createScenegraph()
	 * @see #buildScenegraph()
	 */
	public ViewBase(T root) {
		this(root, true);
	}
	
	
	/** <!-- $LANGUAGE=DE -->
	 * Erzeugt eine neue View, die eine neue {@code Scene} mit dem übergebenen Wurzelknoten {@code root} kapselt.
	 * <p>
	 * Nach der Initialisierung der Attribute werden nacheinander die Methoden {@link #init()} und ggf. {@link #buildScenegraph()} 
	 * aufgerufen, um den Scenegraphen zu konstruieren und im Anschluss wird mit Hilfe eines Controllers die Funktion der Bedienelemente 
	 * hinzugefügt, wenn ein passender Controller registriert wurde.
	 * </p>
	 * 
	 * @param root				Wurzelknoten des Scenegraphen
	 * @param buildScenegraph	{@code true}, wenn der Scenegraph mit der Methode {@link #buildScenegraph()} erstellt und ein Controller
	 * 							erzeugt werden soll, sonst {@code false}
	 * 
	 * @see #buildScenegraph()
	 */
	public ViewBase(T root, boolean buildScenegraph) {
		this.root = root;
		this.tfMap = new HashMap<String, TextField>();
		this.btnMap = new HashMap<String, Button>();
		this.nodeMap = new HashMap<String, Node>();
		this.scene = new Scene(this.root);
		
		init();
		
		if(buildScenegraph) {
			buildScenegraph();
		}
	}

	
	
// Methoden		##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	/** <!-- $LANGUAGE=DE -->
	 * Initialisierungsmethode. 
	 * <b> Diese Methode wird als erstes vom Konstruktor aufgerufen, nachdem die Attribute initialisiert wurden. </b>
	 * <p>
	 * Unterklassen können diese Methode überschreiben, um Attribute vor der Konstruktion des Scenegraphen zu initialisieren.
	 * </p>
	 * <p>
	 * Die Implementierung der Methode in dieser Klasse hat keine Anweisungen.
	 * </p>
	 */
	protected void init() {
		// nothing to do
	}
	
	
	/** <!-- $LANGUAGE=DE -->
	 * Konstruiert den Scenegraphen mit der Methode {@link #createScenegraph()} und gibt den Bedienelementen mit einem
	 * Controller eine Funktion, falls ein passender Controller mit {@link Controller#register(Class, Class)} registriert
	 * wurde.
	 * 
	 * @see #createScenegraph()
	 */
	protected void buildScenegraph() {
		createScenegraph();
		
		this.controller = Controller.of(this);
		
		if(controller != null) {
			controller.setActions();
		}
	}
	
	
	/** <!-- $LANGUAGE=DE -->
	 * Methode, die den Scenegraphen konstruiert. <b> Diese Methode wird aufgerufen, nachdem die init-Methode beendet wurde. </b>
	 * <p>
	 * In dieser Methode müssen alle Bedien- und Oberflächenelemente zum Scenegraphen hinzugefügt werden. Alle Elemente, die 
	 * auch im Controller benötigt werden, müssen zu der entsprechenden Map hinzugefügt werden: 
	 * {@link #btnMap}, {@link #tfMap}, {@link #nodeMap}
	 * </p>
	 */
	protected abstract void createScenegraph();
	
	

// Getter und Setter	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	/** {@inheritDoc} */
	@Override
	public Scene getScene() {
		return scene;
	}

	/** {@inheritDoc} */
	@Override
	public Map<String, TextField> getTextFieldMap() {
		return tfMap;
	}

	/** {@inheritDoc} */
	@Override
	public Map<String, Button> getButtonMap() {
		return btnMap;
	}
	
	/** {@inheritDoc} */
	@Override
	public Map<String, Node> getNodeMap() {
		return nodeMap;
	}
	
	/**  <!-- $LANGUAGE=DE -->
	 * {@inheritDoc}
	 * 
	 * <p><b>
	 * Wenn der Wurzelknoten dieser View eine Instanz von BorderPane ist, wird
	 * die übergebene MenuBar in Top gelegt, ansonsten wird nur die alte MenuBar
	 * entfernt.
	 * </b></p>
	 */
	/*  <!-- $LANGUAGE=EN -->
	 * {@inheritDoc}
	 * 
	 * <p><b>
	 * If root is an instance of BorderPane the MenuBar is placed in Top, 
	 * otherwise only the old MenuBar is removed.
	 * </b></p>
	 */
	@Override
	public <V extends MenuBar> void setMenuBar(V menubar) {
		if(! (root instanceof Pane)) {
			return;
		}
		
		((Pane)root).getChildren().remove(this.menubar);
		this.menubar = menubar;
		
		if(root instanceof BorderPane && menubar != null) {
			((BorderPane) root).setTop(menubar);
		}
	}
	
}
