package it.polito.tdp.extflightdelays.model;

import java.util.HashMap;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.extflightdelays.db.ExtFlightDelaysDAO;

public class Model {
	
	private Graph<Airport,DefaultWeightedEdge> grafo;
	private HashMap<Integer,Airport> idMapAereoporti;
	private HashMap<Integer,Airport> idMapAereoportiCollegati;
	private ExtFlightDelaysDAO dao;
	
	public Model() {
		idMapAereoporti= new HashMap<>();
		idMapAereoportiCollegati= new HashMap<>();
		dao= new ExtFlightDelaysDAO();
	}

	public void creaGrafo(double distanzaMinima) {
		
		this.dao.loadAllAirports(idMapAereoporti);
		
		for(Flight f: this.dao.loadAllFlights()) {
			if(!this.idMapAereoportiCollegati.containsKey(f.getOriginAirportId())) {
				this.idMapAereoportiCollegati.put(f.getOriginAirportId(), this.idMapAereoporti.get(f.getOriginAirportId()));
			}
		}
		
		grafo= new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		// Aggiungo i vertici
		Graphs.addAllVertices(grafo, idMapAereoportiCollegati.values());
		
		// Aggiungo gli archi
		
		for(Adiacenza a: this.dao.getAdiacenze(distanzaMinima)) {
			Graphs.addEdge(grafo, this.idMapAereoportiCollegati.get(a.id1), this.idMapAereoportiCollegati.get(a.id2), a.peso);
		}
		
	}
	
	public int getNVertici() {
		return this.grafo.vertexSet().size();
	}
	public int getNArchi() {
		return this.grafo.edgeSet().size();
	}

	public Graph<Airport, DefaultWeightedEdge> getGrafo() {
		return grafo;
	}
	
	
	
	

}
