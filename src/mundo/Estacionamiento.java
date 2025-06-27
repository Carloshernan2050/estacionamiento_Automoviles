package mundo;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Clase que representa un registro de estacionamiento para un vehículo.
 * Almacena la información de ingreso, retiro, número del parqueadero y el vehículo asociado.
 */
public class Estacionamiento {

    // Fecha y hora de ingreso al estacionamiento
    private LocalDateTime fechaIngreso;

    // Fecha y hora de retiro del estacionamiento (puede ser null si aún no ha salido)
    private LocalDateTime fechaRetiro;

    // Número de espacio asignado en el estacionamiento
    private int numParqueo;

    // Vehículo asociado a este registro
    private Vehiculo vehiculo;

    /**
     * Constructor de la clase Estacionamiento.
     * 
     * @param fechaIngreso Fecha y hora de ingreso
     * @param numParqueo Número de espacio en el parqueadero
     * @param vehiculo Vehículo que se estaciona
     * @param fechaRetiro Fecha y hora de retiro (puede ser null)
     */
    public Estacionamiento(LocalDateTime fechaIngreso, int numParqueo, Vehiculo vehiculo, LocalDateTime fechaRetiro) {
        this.fechaIngreso = fechaIngreso;
        this.numParqueo = numParqueo;
        this.vehiculo = vehiculo;
        this.fechaRetiro = fechaRetiro;
    }

    // Getters y Setters
    public LocalDateTime getFechaIngreso() { 
        return fechaIngreso; 
    }

    public void setFechaIngreso(LocalDateTime fechaIngreso) { 
        this.fechaIngreso = fechaIngreso; 
    }

    public LocalDateTime getFechaRetiro() { 
        return fechaRetiro; 
    }

    public void setFechaRetiro(LocalDateTime fechaRetiro) { 
        this.fechaRetiro = fechaRetiro; 
    }

    public int getNumParqueo() { 
        return numParqueo; 
    }

    public void setNumParqueo(int numParqueo) { 
        this.numParqueo = numParqueo; 
    }

    public Vehiculo getVehiculo() { 
        return vehiculo; 
    }

    public void setVehiculo(Vehiculo vehiculo) { 
        this.vehiculo = vehiculo; 
    }

    /**
     * Calcula la duración total entre la hora de ingreso y la de retiro.
     * @return Duración entre ingreso y retiro, o null si falta alguno.
     */
    public Duration calcularDuracionTotal() {
        if (fechaIngreso != null && fechaRetiro != null) {
            return Duration.between(fechaIngreso, fechaRetiro);
        }
        return null;
    }

    /**
     * Calcula el tiempo transcurrido desde el ingreso hasta el momento actual.
     * Solo se aplica si el vehículo aún no ha salido.
     * 
     * @return Duración desde el ingreso hasta ahora, o null si ya salió o no hay ingreso.
     */
    public Duration calcularTiempoEnParqueo() {
        if (fechaIngreso != null && fechaRetiro == null) {
            return Duration.between(fechaIngreso, LocalDateTime.now());
        }
        return null;
    }

    /**
     * Retorna la duración como una cadena legible, por ejemplo: "3 h 20 min".
     * Si el vehículo ya salió, se usa el total entre ingreso y retiro.
     * Si aún está parqueado, se calcula hasta el momento actual.
     * 
     * @return Representación legible del tiempo transcurrido.
     */
    public String getDuracionComoTexto() {
        Duration duracion = fechaRetiro != null ? calcularDuracionTotal() : calcularTiempoEnParqueo();
        if (duracion != null) {
            long horas = duracion.toHours();
            long minutos = duracion.toMinutes() % 60;
            return horas + " h " + minutos + " min";
        }
        return "Duración no disponible";
    }
}
