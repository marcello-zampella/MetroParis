package it.polito.tdp.metroparis.model;

public class PartenzaArrivo {
	private Fermata partenza;
	private Fermata arrivo;
	public PartenzaArrivo(Fermata partenza, Fermata arrivo) {
		super();
		this.partenza = partenza;
		this.arrivo = arrivo;
	}
	public Fermata getPartenza() {
		return partenza;
	}
	public void setPartenza(Fermata partenza) {
		this.partenza = partenza;
	}
	public Fermata getArrivo() {
		return arrivo;
	}
	public void setArrivo(Fermata arrivo) {
		this.arrivo = arrivo;
	}
	
}
