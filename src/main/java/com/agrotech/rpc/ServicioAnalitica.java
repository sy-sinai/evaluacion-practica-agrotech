package com.agrotech.rpc;

import org.apache.camel.Header;

public class ServicioAnalitica {
    public String getUltimoValor(@Header("id_sensor") String id) {
        return String.format("""
            {"id":"%s","humedad":48,"temperatura":26.7,"fecha":"2025-05-22T10:30:00Z"}
        """, id);
    }
}
