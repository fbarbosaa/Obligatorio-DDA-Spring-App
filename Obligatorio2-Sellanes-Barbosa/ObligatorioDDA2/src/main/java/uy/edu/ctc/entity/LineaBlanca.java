package uy.edu.ctc.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class LineaBlanca extends Electrodomestico {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Column
	private Double capacidad;

	public Double getCapacidad() {
		return capacidad;
	}

	public void setCapacidad(Double capacidad) {
		this.capacidad = capacidad;
	}
	

	public LineaBlanca(String nombre, String modelo,String marca, Double precio, Double capacidad) {
		super(nombre, modelo ,marca, precio);
		this.capacidad = capacidad;
	}
	
	public LineaBlanca() {
		
	}
	
	
}
