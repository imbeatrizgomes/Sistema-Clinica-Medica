package util;

import java.io.File;

public class CaminhoCSV {

    public static final String BASE = "C:\\temp\\";

    static {
        File dir = new File(BASE);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public static String pacientes() {
        return BASE + "pacientes.csv";
    }

    public static String medicos() {
        return BASE + "medicos.csv";
    }

    public static String consultas() {
        return BASE + "consultas.csv";
    }
}
