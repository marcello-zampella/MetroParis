package it.polito.tdp.metroparis.model;

import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

import it.polito.tdp.metroparis.db.MetroDAO;

public class Model {
	
	private Graph<Fermata,DefaultEdge> grafo;
	private Graph<Fermata,DefaultEdge> grafo2;
	private List<Fermata> fermate;
	
	public void creaGrafo() {
		//Creo il grafo
		this.grafo=new SimpleDirectedGraph<>(DefaultEdge.class);
		this.grafo2=new SimpleDirectedGraph<>(DefaultEdge.class);
		
		//aggiungo i vertici
		MetroDAO dao= new MetroDAO();
		this.fermate=dao.getAllFermate();
		Graphs.addAllVertices(this.grafo, this.fermate);
		Graphs.addAllVertices(this.grafo2, this.fermate);
		/*
		//Aggiungi gli archi
		for(Fermata partenza: this.grafo.vertexSet()) {
			for(Fermata arrivo: this.grafo.vertexSet()) {
				if(dao.esisteConnessione(partenza,arrivo)) { //va a vedere se tra le due fermate date esiste il collegamento
					this.grafo.addEdge(partenza, arrivo); //processo lunghissimo
				}
			}
		}
		*/
		//opzione 2
		
		for(Fermata partenza: this.grafo.vertexSet()) {
			List<Fermata> arrivi= dao.stazioneArrivo(partenza);
			for(Fermata arrivo: arrivi) {
				int temp=this.fermate.indexOf(arrivo);
				this.grafo.addEdge(partenza, this.fermate.get(temp)); //dato una fermata, prendo tutte le stazioni a cui essa e' collegata
			}
		}
		
		
		//opzione 3
		
		List<PartenzaArrivo> collegamenti=dao.getCollegamenti();
		for(PartenzaArrivo temp: collegamenti) {
			int part=fermate.indexOf(temp.getPartenza());
			int arr=fermate.indexOf(temp.getArrivo());
			this.grafo2.addEdge(fermate.get(part), fermate.get(arr));
		}
		
		
	}

	public Graph<Fermata, DefaultEdge> getGrafo() {
		return grafo;
	}
	
	

	public Graph<Fermata, DefaultEdge> getGrafo2() {
		return grafo2;
	}

	public List<Fermata> getFermate() {
		return fermate;
	}
	
	
	
}
