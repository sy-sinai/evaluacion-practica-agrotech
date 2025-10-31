package com.agrotech.routes;

import org.apache.camel.builder.RouteBuilder;

public class FieldControlRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("timer:fieldcontrol?period=5000")
            .routeId("fieldcontrol-shared-db")
            .setBody(constant("SELECT * FROM lecturas ORDER BY fecha DESC"))
            .to("jdbc:myDataSource")
            .log("[FIELDCONTROL] Última lectura: ${body}");
    }
}
