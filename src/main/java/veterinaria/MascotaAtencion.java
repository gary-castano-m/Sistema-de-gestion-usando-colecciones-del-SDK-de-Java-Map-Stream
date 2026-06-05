package veterinaria;

import java.util.Objects;

//Entidad principal del sistema de Veterinaria.

public class MascotaAtencion {

    private String codigoAtencion;
    private String nombreMascota;
    private String especie;
    private String nombrePropietario;
    private String motivoConsulta;
    private String estado;

    public MascotaAtencion(String codigoAtencion, String nombreMascota, String especie,
                           String nombrePropietario, String motivoConsulta) {
        this.codigoAtencion   = codigoAtencion;
        this.nombreMascota    = nombreMascota;
        this.especie          = especie;
        this.nombrePropietario = nombrePropietario;
        this.motivoConsulta   = motivoConsulta;
        this.estado           = "PENDIENTE";
    }

    // Getters
    public String getCodigoAtencion(){
        return codigoAtencion; }

    public String getNombreMascota(){
        return nombreMascota; }

    public String getEspecie(){
        return especie; }

    public String getNombrePropietario(){
        return nombrePropietario; }

    public String getMotivoConsulta(){
        return motivoConsulta; }

    public String getEstado(){
        return estado; }

    // Setters
    public void setCodigoAtencion(String codigoAtencion){
        this.codigoAtencion = codigoAtencion;
    }

    public void setNombreMascota(String nombreMascota){
        this.nombreMascota = nombreMascota;
    }

    public void setEspecie(String especie){
        this.especie = especie;
    }

    public void setNombrePropietario(String nombrePropietario){
        this.nombrePropietario = nombrePropietario;
    }

    public void setMotivoConsulta(String motivoConsulta){
        this.motivoConsulta = motivoConsulta;
    }

    public void setEstado(String estado){
        this.estado = estado;
    }

    //toString
    @Override
    public String toString() {
        return String.format(
                "[%s] %-15s | Especie: %-8s | Propietario: %-20s | Motivo: %-25s | Estado: %s",
                codigoAtencion, nombreMascota, especie, nombrePropietario, motivoConsulta, estado
        );
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MascotaAtencion)) return false;
        MascotaAtencion that = (MascotaAtencion) o;
        return Objects.equals(codigoAtencion, that.codigoAtencion);
    }

    @Override
    public int hashCode() {

        return Objects.hash(codigoAtencion);
    }
}
