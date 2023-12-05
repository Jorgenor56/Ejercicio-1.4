import java.io.File;

public class UsaAccesoDOM {
    public static void main(String[] args) {
        AccesoDOM accesoDOM = new AccesoDOM();
        //Reemplaza con la ubicación de tu archivo XML
        File archivoXML = new File("Libros.xml");

        //Abrir el archivo XML
        int resultadoApertura = accesoDOM.abrirXMLaDOM(archivoXML);
        if (resultadoApertura == 0) {
            //Mostrar el contenido del archivo XML
            accesoDOM.recorreDOMyMuestra();

            //Insertar un libro en el DOM
            accesoDOM.insertarLibroEnDOM("Yerma", "Lorca", "1935");

            //Borrar un nodo por título
            accesoDOM.deleteNode("Don Quijote");

            //Guardar el DOM modificado en un nuevo archivo
            accesoDOM.guardarDOMcomoArchivo("LibrosDeDOM.xml");
        }
    }
}
