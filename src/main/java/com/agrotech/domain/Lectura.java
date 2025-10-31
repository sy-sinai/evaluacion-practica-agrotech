package com.agrotech.domain;

public class Lectura {
    public String id_sensor;
    public String fecha;
    public double humedad;
    public double temperatura;

    public String toString() {
        return "Lectura{id=" + id_sensor + ", fecha=" + fecha + ", hum=" + humedad + ", temp=" + temperatura + "}";
    }
}
