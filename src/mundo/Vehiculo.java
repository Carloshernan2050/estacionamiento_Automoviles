package mundo;

public class Vehiculo {
    private String marca;
    private String modelo;
    private String placa;
    private String propietario;
    private String imagenUrl;

    public Vehiculo(String marca, String modelo, String placa, String propietario) {
        this.marca = marca;
        this.modelo = modelo;
        this.placa = placa;
        this.propietario = propietario;
    }

    // Getters y Setters
    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }
    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }
    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }
    public String getPropietario() { return propietario; }
    public void setPropietario(String propietario) { this.propietario = propietario; }
    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }
}