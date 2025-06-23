package mundo;

import java.time.Duration;
import java.time.LocalDateTime;

public class Estacionamiento {
    private LocalDateTime fechaIngreso;
    private LocalDateTime fechaRetiro;
    private int numParqueo;
    private Vehiculo vehiculo;

    public Estacionamiento(LocalDateTime fechaIngreso, int numParqueo, Vehiculo vehiculo, LocalDateTime fechaRetiro) {
        if (fechaIngreso == null) {
            throw new IllegalArgumentException("La fecha de ingreso no puede ser nula");
        }
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

    // Duración entre ingreso y retiro (si el vehículo ya fue retirado)
    public Duration calcularDuracionTotal() {
        if (fechaIngreso != null && fechaRetiro != null) {
            return Duration.between(fechaIngreso, fechaRetiro);
        }
        return null;
    }

    // Duración desde el ingreso hasta ahora (si el vehículo sigue estacionado)
    public Duration calcularTiempoEnParqueo() {
        if (fechaIngreso != null && fechaRetiro == null) {
            return Duration.between(fechaIngreso, LocalDateTime.now());
        }
        return null;
    }
}