package it.polito.tdp.metroparis;


import java.util.*;

import it.polito.tdp.metroparis.model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class MetroController {

    @FXML
    private ComboBox<Fermata> listaPartenza;

    @FXML
    private ComboBox<Fermata> listaArrivo;

    @FXML
    private TextArea txtArea;

	private Model model;

    @FXML
    void doSearch(ActionEvent event) {
    	Fermata partenza=this.listaPartenza.getValue();
    	Fermata arrivo=this.listaArrivo.getValue();
    	List<Fermata> percorso=model.getCamminoMinimo(partenza, arrivo);
    	int k=1;
    	this.txtArea.clear();
    	for(Fermata f: percorso) {
    		this.txtArea.appendText(k+". "+f.getNome()+"\n");
    		k++;
    	}
    	
    }

	public void setModel(Model model) {
		this.model=model;
		model.creaGrafo();
		ArrayList<Fermata> fermate=new ArrayList<Fermata>(model.getFermate());
		this.listaArrivo.getItems().setAll(fermate);
		this.listaPartenza.getItems().setAll(fermate);
		
	}

}
