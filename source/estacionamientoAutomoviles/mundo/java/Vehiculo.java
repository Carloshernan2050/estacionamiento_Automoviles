package estacionamientoAutomoviles.mundo.java;



public class Vehiculo 
{

	private String placa;
	
	private String marcaVehiculo;
	
	private String modeloVehiculo;
	
	private String nombrePropietario;
	
	
	public Vehiculo( String placa, String marcaVehiculo, String modeloVehiculo, String nombrePropietario )
	{
		this.placa = placa;
		this.marcaVehiculo = marcaVehiculo;
		this.modeloVehiculo = modeloVehiculo;
		this.nombrePropietario = nombrePropietario;
	}
	
	public String getPlaca( )
	{
		return placa;
	}
	
	public String getMarcaVehiculo( )
	{
		return marcaVehiculo;
	}
	
	public String getModeloVehiculo( )
	{
		return modeloVehiculo;
	}
	
	public String getNombrePropietario( )
	{
		return nombrePropietario;
	}
	
	public void setPlaca( String placa )
	{
		this.placa = placa;
	}
	
	public void setMarcaVehiculo( String marcaVehiculo )
	{
		this.marcaVehiculo = marcaVehiculo;
	}
	
	public void setModeloVehiculo( String modeloVehiculo )
	{
		this.modeloVehiculo = modeloVehiculo;
	}
	
	public void setNombrePropietario( String nombrePropietario )
	{
		this.nombrePropietario = nombrePropietario;
	}
	
	
	public String ingresarPlaca( )
	{
		
	}
	
	public String ingresarModelo( )
	{
		
	}
	
	public String ingresarMarca( )
	{
		
	}
	
	public String ingresarNombrePropietario( )
	{
		
	}
	
	
}
