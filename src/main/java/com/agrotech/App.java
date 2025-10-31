package com.agrotech;

import com.agrotech.config.DatabaseConfig;
import com.agrotech.routes.*;
import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.support.SimpleRegistry;

public class App {
    public static void main(String[] args) throws Exception {
        var registry = new SimpleRegistry();
        try (CamelContext camel = new DefaultCamelContext(registry)) {

            DatabaseConfig.bindJdbc(camel);


            camel.addRoutes(new SensDataRoute());
            camel.addRoutes(new AgroAnalyzerRoute());
            camel.addRoutes(new FieldControlRoute());


            camel.start();

            // ⚠️ Esta llamada NO es necesaria — la ruta direct:solicitarLectura no existe
            // camel.createFluentProducerTemplate()
            //     .to("direct:solicitarLectura")
            //     .withBody("S001")
            //     .request();

            // Espera unos segundos para ver logs
            Thread.sleep(15000);

            // Detiene Camel
            camel.stop();
        }
    }
}
