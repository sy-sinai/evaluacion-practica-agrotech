package com.agrotech;

import com.agrotech.config.DatabaseConfig;
import com.agrotech.routes.*;
import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.support.SimpleRegistry;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class App {
    public static void main(String[] args) throws Exception {

        // Crear carpeta logs si no existe
        File logDir = new File("logs");
        if (!logDir.exists()) {
            logDir.mkdirs();
        }

        // Crear archivo de log (sobrescribe cada ejecución)
        PrintStream logFile = new PrintStream(new FileOutputStream("logs/agrotech.log", false));
        PrintStream console = System.out; // mantener referencia a la consola

        // Redirigir salida estándar y errores al archivo
        System.setOut(logFile);
        System.setErr(logFile);

        var registry = new SimpleRegistry();
        try (CamelContext camel = new DefaultCamelContext(registry)) {

            DatabaseConfig.bindJdbc(camel);

            camel.addRoutes(new SensDataRoute());
            camel.addRoutes(new AgroAnalyzerRoute());
            camel.addRoutes(new FieldControlRoute());

            camel.start();

            // También imprimir en consola y log
            console.println("🟢 Proyecto AgroTech iniciado correctamente...");
            console.println("📁 Guardando logs en: logs/agrotech.log");

            // Ejecutar simulación de flujo
            Thread.sleep(15000);
            camel.stop();

            console.println("✅ Ejecución finalizada. Revisa logs/agrotech.log");
        }

        // Cerrar stream de log
        logFile.close();
    }
}
