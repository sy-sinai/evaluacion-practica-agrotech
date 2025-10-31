package com.agrotech.routes;

import org.apache.camel.builder.RouteBuilder;
import java.util.HashMap;
import java.util.Map;

public class SensDataRoute extends RouteBuilder {
    @Override
    public void configure() {

        from("file://input?move=../output&noop=false")
            .routeId("sensdata-file-transfer")
            .log("[FILE] Detectado archivo ${header.CamelFileName}, iniciando procesamiento...")

            .split(body().tokenize("\n")).streaming()
            .filter(body().isNotEqualTo("")) // evita líneas vacías
            .process(exchange -> {
                String line = exchange.getIn().getBody(String.class).trim();

                // Omitimos el encabezado
                if (line.startsWith("id_sensor")) {
                    exchange.setProperty("skipHeader", true);
                    return;
                }

                // Limpieza general: eliminamos ';' y espacios extra
                line = line.replace(";", "").trim();

                String[] campos = line.split(",");

                // Validación de columnas esperadas
                if (campos.length < 4) {
                    exchange.setProperty("skipLine", true);
                    return;
                }

                // Mapeo limpio de los valores
                Map<String, Object> data = new HashMap<>();
                data.put("id_sensor", campos[0].trim());
                data.put("fecha", campos[1].trim());
                data.put("humedad", Double.parseDouble(campos[2].trim()));
                data.put("temperatura", Double.parseDouble(campos[3].trim()));

                exchange.getMessage().setBody(data);
            })
            .filter(exchangeProperty("skipHeader").isNull())
            .filter(exchangeProperty("skipLine").isNull())

            // Envío a la siguiente ruta
            .to("direct:agroanalyzer.ingestar")

            .log("[FILE] Línea procesada y enviada: ${body}");
    }
}
