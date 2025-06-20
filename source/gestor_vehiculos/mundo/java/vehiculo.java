package gestor_vehiculos.mundo.java;

public class vehiculo {

	private String placa;
	
	private String marcaVehiculo;
	
	private String modeloVehiculo;
	
	private String nombrePropietario;
	
	
	public vehiculo( String placa, String marcaVehiculo, String modeloVehiculo, String nombrePropietario)
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
}
