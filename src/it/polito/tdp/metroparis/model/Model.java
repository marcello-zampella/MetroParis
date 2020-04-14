package it.polito.tdp.metroparis.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.event.ConnectedComponentTraversalEvent;
import org.jgrapht.event.EdgeTraversalEvent;
import org.jgrapht.event.TraversalListener;
import org.jgrapht.event.VertexTraversalEvent;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.DepthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

import it.polito.tdp.metroparis.db.MetroDAO;

public class Model {
	
	private class EdgeTraversedGraphListener implements TraversalListener<Fermata,DefaultEdge> {

		@Override
		public void connectedComponentFinished(ConnectedComponentTraversalEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void connectedComponentStarted(ConnectedComponentTraversalEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void edgeTraversed(EdgeTraversalEvent<DefaultEdge> ev) {
			//ci interessa il cammino, perciò ogni volta che creo un nuovo arco da un nodo in cui mi trovo è necessario salvare qualche informazione
			//voglio tenere traccia per ogni vertice, qual è il vertice precedente, per immagazzinare questa informazione
			//creo una mappa che abbia la chiave la fermata figlia e il valore la fermata padre.
			// necessario che padre sia stato visitato, ma figlio ancora sconosciuto
			
			Fermata sourceVertex=grafo.getEdgeSource(ev.getEdge());
			Fermata targetVertex=grafo.getEdgeTarget(ev.getEdge());
			//se il grafo e' orientato, allora sono sicuro che la source e' il padre e il target e' il figlo
			// se il grafo non e' orientato, potrebbe essere il contrario, magari ho gia' visto in un verso quell'arco e adesso sto ricapitando nel verso opposto
			// er capire se quell'arco l'ho gia' visitato vado a vedere se il nodo che penso possa essere figlio e' gia' inserito nella mappa
			
			if(!backVisit.containsKey(targetVertex) && backVisit.containsKey(sourceVertex)) {
				//affinchè sia un nuovo arco, la mappa deve contenere la source (il padre) (in teoria questo dovrebbe accadere sempre, perche' altrimenti
				//non potrei vedere proprio l'arco) e NON contenere la destinazione (il figlio)
				backVisit.put(targetVertex, sourceVertex);
			} else if(!backVisit.containsKey(sourceVertex) && backVisit.containsKey(targetVertex)) { //SOLO NEL CASO DI ARCHI NON ORIENTATI
				backVisit.put(sourceVertex,targetVertex);
			}		
		}
		

		@Override
		public void vertexFinished(VertexTraversalEvent<Fermata> arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void vertexTraversed(VertexTraversalEvent<Fermata> arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private Graph<Fermata,DefaultEdge> grafo;
	private Graph<Fermata,DefaultEdge> grafo2;
	private List<Fermata> fermate;
	private Map<Fermata,Fermata> backVisit;
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
	
	
	public List<Fermata> fermataRaggiungibili (Fermata source) {
		
		List<Fermata> result = new ArrayList<Fermata>();
		backVisit= new HashMap<>();
		GraphIterator<Fermata,DefaultEdge> it=new BreadthFirstIterator<>(this.grafo, source); // crea un nuovo iteratore e lo associa a questo grafo
	// per scegliere quale sia il nodo in cui deve iniziare glielo devo specificare nel secondo parametro
		
		//GraphIterator<Fermata,DefaultEdge> it=new DepthFirstIterator<>(this.grafo, source); //modo diverso di visitare il grafico
		
		it.addTraversalListener(new Model.EdgeTraversedGraphListener());
				
		backVisit.put(source, null); // devo dargli un nodo da cui partire
		while(it.hasNext()) {
			result.add(it.next());
		}
		return result;
	}
	
	
	public List<Fermata> percorsoFinoA (Fermata target) { //voglio vedere qual è il percorso che mi porta fino ad una fermata
		if(!this.backVisit.containsKey(target)) {
			return null;
		}
		List<Fermata> percorso=new LinkedList<>();
		Fermata f=target;
		while (f!= null) { //percorro l'albero a ritroso, dalla fermata in cui mi trovo fino alla prima (che ha padre null)
		percorso.add(0,f); 
		f=backVisit.get(f);//voglio cercare il padre del padre (quindi il padre è il nuovo figlio)
		}
		return percorso;
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
