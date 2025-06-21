package estacionamientoAutomoviles.mundo.java;

import java.time.LocalDateTime;

public class Estacionamiento extends Vehiculo
	{
	private Vehiculo vehiculo;

	private LocalDateTime fechaIngreso;
	
	private LocalDateTime fechaRetiro;
	
	private int numParqueo;
	
	public Estacionamiento( LocalDateTime fechaIngreso, int numParqueo, Vehiculo vehiculo ) 
	{
		this.fechaIngreso = fechaIngreso;
		this.fechaRetiro = null;
		this.numParqueo = numParqueo;
		this.vehiculo = vehiculo;
	
	}
	
	public LocalDateTime getFechaIngreso( )
	{
		return fechaIngreso;
	}
	
	public LocalDateTime getFechaRetiro( )
	{
		return fechaRetiro;
	}
	
	public int getNumParqueo( )
	{
		return numParqueo;
	}
	
}
