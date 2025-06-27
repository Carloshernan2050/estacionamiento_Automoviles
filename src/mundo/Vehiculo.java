package mundo;

/**
 * Clase que representa un vehículo registrado en el sistema de estacionamiento.
 * Contiene la información básica como marca, modelo, placa, propietario e imagen asociada.
 */
public class Vehiculo {

    // Marca del vehículo (ej. Ford, Chevrolet)
    private String marca;

    // Modelo del vehículo (ej. Mustang, Camaro)
    private String modelo;

    // Placa del vehículo, usada como identificador único
    private String placa;

    // Nombre del propietario del vehículo
    private String propietario;

    // Ruta o URL de la imagen asociada al vehículo
    private String imagenUrl;

    /**
     * Constructor para inicializar un vehículo con sus datos principales.
     * 
     * @param marca Marca del vehículo.
     * @param modelo Modelo del vehículo.
     * @param placa Placa del vehículo.
     * @param propietario Nombre del propietario del vehículo.
     */
    public Vehiculo(String marca, String modelo, String placa, String propietario) {
        this.marca = marca;
        this.modelo = modelo;
        this.placa = placa;
        this.propietario = propietario;
    }

    /**
     * @return Marca del vehículo.
     */
    public String getMarca() { 
        return marca; 
    }

    /**
     * @param marca Marca a establecer.
     */
    public void setMarca(String marca) { 
        this.marca = marca; 
    }

    /**
     * @return Modelo del vehículo.
     */
    public String getModelo() { 
        return modelo; 
    }

    /**
     * @param modelo Modelo a establecer.
     */
    public void setModelo(String modelo) { 
        this.modelo = modelo; 
    }

    /**
     * @return Placa del vehículo.
     */
    public String getPlaca() { 
        return placa; 
    }

    /**
     * @param placa Placa a establecer.
     */
    public void setPlaca(String placa) { 
        this.placa = placa; 
    }

    /**
     * @return Nombre del propietario del vehículo.
     */
    public String getPropietario() { 
        return propietario; 
    }

    /**
     * @param propietario Nombre del propietario a establecer.
     */
    public void setPropietario(String propietario) { 
        this.propietario = propietario; 
    }

    /**
     * @return Ruta o URL de la imagen del vehículo.
     */
    public String getImagenUrl() { 
        return imagenUrl; 
    }

    /**
     * @param imagenUrl Ruta o URL de la imagen a establecer.
     */
    public void setImagenUrl(String imagenUrl) { 
        this.imagenUrl = imagenUrl; 
    }
}
