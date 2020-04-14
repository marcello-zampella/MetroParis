package it.polito.tdp.metroparis.model;

import java.util.List;

public class TestModel {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Model m= new Model();
		m.creaGrafo();
		System.out.println("Creati "+m.getGrafo().vertexSet().size()+" e archi "+m.getGrafo().edgeSet().size());
		Fermata source= m.getFermate().get(0);
		System.out.println("parto da: "+source);
		
		List<Fermata> raggiungibili= m.fermataRaggiungibili(source);
		System.out.println("fermate raggiungibili "+raggiungibili+" \n  "+raggiungibili.size());
		
		Fermata target=m.getFermate().get(150);
		System.out.println("ARRIVO A: "+target);
		List<Fermata> percorso=m.percorsoFinoA(target);
		System.out.println(percorso);
		
	}

}
