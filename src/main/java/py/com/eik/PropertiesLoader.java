package py.com.eik;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;


/**
 * Clase para cargar propiedades del servidor definidas en el archivo serverConfig.properties.
 * <p>
 * Es obligatorio que el archivo se encuentre en el mismo directorio desde el que se ejecutará
 * el comando para levantar el servidor.
 * <p>
 * En caso de error al obtener alguna propiedad específica, se debe retornar valores por defecto
 * codificados en duro dentro de cada método getter de propiedad
 * Inspirado en:
 * https://stackoverflow.com/questions/8775303/read-properties-file-outside-jar-file
 */
public class PropertiesLoader {

    private static Properties properties;

    static {
        properties = buildProperties();
    }

    /**
     * Obtener propiedad server.port
     * Utilizado para iniciar el socket server en el puerto especificado.
     * Valor por defecto: 6666
     *
     * @return cadena del número del puerto
     */
    public static String getServerPort() {
        return properties.getProperty("server.port", "6666");
    }

    private static FileInputStream openFile() {
        FileInputStream file;

        // ubicación del archivo de propiedades, reelativo al directorio desde el que se ejecuta el jar
        String filePath = "./serverConfig.properties";

        try {
            file = new FileInputStream(filePath);
        } catch (FileNotFoundException e) {
            // no se pudo abrir el archivo. Imprimir recomendaciones en consola
            String userDir = System.getProperty("user.dir");
            String errMsg = String.format("No se encontró el archivo de configuraciones \"%s\". Ubíquelo en: %s",
                    filePath, userDir);
            System.out.println(errMsg);
            file = null;
        }

        return file;
    }

    private static Properties buildProperties() {
        Properties mainProperties = new Properties();
        FileInputStream file = openFile();
        if (file == null) {
            System.out.println("Error al intentar leer archivo de propiedades");

        } else {
            try {
                mainProperties.load(openFile());
                file.close();
            } catch (Exception e) {
                System.out.println("Error al construir propiedades. Se utilizarán propiedades por defecto");
            }
        }

        return mainProperties;
    }

}
