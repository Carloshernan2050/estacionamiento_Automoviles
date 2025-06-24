package mundo;

import java.time.Duration;
import java.time.LocalDateTime;

public class Estacionamiento {
    private LocalDateTime fechaIngreso;
    private LocalDateTime fechaRetiro;
    private int numParqueo;
    private Vehiculo vehiculo;

    public Estacionamiento(LocalDateTime fechaIngreso, int numParqueo, Vehiculo vehiculo, LocalDateTime fechaRetiro) {
        this.fechaIngreso = fechaIngreso;
        this.numParqueo = numParqueo;
        this.vehiculo = vehiculo;
        this.fechaRetiro = fechaRetiro;
    }

    // Getters y Setters
    public LocalDateTime getFechaIngreso() { return fechaIngreso; }
    public void setFechaIngreso(LocalDateTime fechaIngreso) { this.fechaIngreso = fechaIngreso; }

    public LocalDateTime getFechaRetiro() { return fechaRetiro; }
    public void setFechaRetiro(LocalDateTime fechaRetiro) { this.fechaRetiro = fechaRetiro; }

    public int getNumParqueo() { return numParqueo; }
    public void setNumParqueo(int numParqueo) { this.numParqueo = numParqueo; }

    public Vehiculo getVehiculo() { return vehiculo; }
    public void setVehiculo(Vehiculo vehiculo) { this.vehiculo = vehiculo; }

    // Calcula la duración total si ya se retiró
    public Duration calcularDuracionTotal() {
        if (fechaIngreso != null && fechaRetiro != null) {
            return Duration.between(fechaIngreso, fechaRetiro);
        }
        return null;
    }

    // Calcula el tiempo desde el ingreso hasta ahora
    public Duration calcularTiempoEnParqueo() {
        if (fechaIngreso != null && fechaRetiro == null) {
            return Duration.between(fechaIngreso, LocalDateTime.now());
        }
        return null;
    }

    // Retorna la duración formateada como texto: "X h Y min"
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
