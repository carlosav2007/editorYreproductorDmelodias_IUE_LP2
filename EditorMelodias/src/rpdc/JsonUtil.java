package rpdc;

import entidades.Melodia;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.*;

public class JsonUtil {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void guardarMelodia(Melodia melodia, String rutaArchivo) throws IOException {
        try (FileWriter writer = new FileWriter(rutaArchivo)) {
            gson.toJson(melodia, writer);
        }
    }

    public static Melodia cargarMelodia(String rutaArchivo) throws IOException {
        try (FileReader reader = new FileReader(rutaArchivo)) {
            return gson.fromJson(reader, Melodia.class);
        }
    }
}