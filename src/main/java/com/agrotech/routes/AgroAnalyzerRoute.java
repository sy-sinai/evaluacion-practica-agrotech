package com.agrotech.routes;

import org.apache.camel.builder.RouteBuilder;
import java.util.Map;

public class AgroAnalyzerRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("direct:agroanalyzer.ingestar")
            .routeId("agroanalyzer-shared-db")
            .log("[ANALYZER] Insertando datos en base de datos...")

            .process(exchange -> {
                Map<String, Object> data = exchange.getIn().getBody(Map.class);
                String query = String.format(
                    "INSERT INTO lecturas (id_sensor, fecha, humedad, temperatura) VALUES ('%s', '%s', %s, %s)",
                    data.get("id_sensor"),
                    data.get("fecha"),
                    data.get("humedad"),
                    data.get("temperatura")
                );
                exchange.getIn().setBody(query);
            })
            .to("jdbc:myDataSource")
            .log("[ANALYZER] Insert OK en DB compartida.");
    }
}
