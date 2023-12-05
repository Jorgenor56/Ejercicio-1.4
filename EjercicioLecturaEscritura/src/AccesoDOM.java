import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;
import java.io.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.xml.sax.SAXException;

public class AccesoDOM {

    Document doc;

    //Método para abrir un archivo XML y generar un objeto dom
    public int abrirXMLaDOM(File f) {
        try {
            System.out.println("Abriendo archivo XML file y generando DOM....;");
            //Crea una instancia de DocumentBuilder
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            //Ignora los comentarios del XML
            factory.setIgnoringComments(true);
            //Ignora los espacios en blanco
            factory.setIgnoringElementContentWhitespace(true); 
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.parse(f); //Parsea el archivo y lo asigna a doc
            System.out.println("DOM creado con éxito.");
            return 0; //Devuelve 0
        } catch (IOException | ParserConfigurationException | SAXException e) {
            System.out.println(e);
            return -1; //Devuelve -1 si hay algun error
        }
    }

    //Método para recorrer el DOM y mostrar su contenido
    public void recorreDOMyMuestra() {
        String[] datos = new String[3];
        Node nodo;
        //Crea el elemento raíz
        Node root = doc.getFirstChild(); 
        //Obtiene todos los nodos hijos del raíz
        NodeList nodelist = root.getChildNodes(); 
        //Bucle for usando la longitud del nodo
        for (int i = 0; i < nodelist.getLength(); i++) {
            nodo = nodelist.item(i);
            //Verifica si es un nodo de tipo Elemento
            if (nodo.getNodeType() == Node.ELEMENT_NODE) { 
                Node ntemp;
                int contador = 1;
                //Obtiene el valor del primer atributo del nodo
                datos[0] = nodo.getAttributes().item(0).getNodeValue(); 
                //Obtiene los nodos hijos del nodo actual
                NodeList nl2 = nodo.getChildNodes(); 
                for (int j = 0; j < nl2.getLength(); j++) {
                    ntemp = nl2.item(j);
                    if (ntemp.getNodeType() == Node.ELEMENT_NODE) {
                        //Asigna el contenido del nodo al array de datos
                        datos[contador] = ntemp.getTextContent(); 
                        contador++;
                    }
                }
                //Imprime los datos
                System.out.println(datos[0] + "-- " + datos[2] + "-- " + datos[1]); 
            }
        }
    }

    //Método para insertar un nuevo libro en el DOM
    public int insertarLibroEnDOM(String titulo, String autor, String fecha) {
        try {
            System.out.println("Añadir libro al DOM:" + titulo + " " + autor + " " + fecha);
            //Creo los nodos del nuevo libro y los añade al DOM
            Node ntitulo = doc.createElement("Titulo");
            Node ntitulo_text = doc.createTextNode(titulo);
            //Añado el texto al nodo titulo
            ntitulo.appendChild(ntitulo_text);
            //Lo mismo pero con autor
            Node nautor = doc.createElement("Autor");
            Node nautor_text = doc.createTextNode(autor);
            nautor.appendChild(nautor_text);
            //Creo un nuevo elemento con la etiqueta libro
            Node nLibro = doc.createElement("Libro");
            //Establezco el atributo publicado
            ((Element) nLibro).setAttribute("publicado", fecha); 
            //Aqui los pongo como hijos
            nLibro.appendChild(ntitulo);
            nLibro.appendChild(nautor);

            nLibro.appendChild(doc.createTextNode("\n &")); //Añade un salto de línea
            //Obtengo el nodo raíz
            Node raiz = doc.getFirstChild();
            //Añado el nuevo libro al nodo raíz
            raiz.appendChild(nLibro); 
            System.out.println("Libro insertado en DOM.");
            return 0; //Devuelvo 0
        } catch (DOMException e) {
            System.out.println(e);
            return -1; //Devuelvo -1 si hay un error
        }
    }

    //Método para eliminar un nodo del DOM
    public int deleteNode(String tit) {
        System.out.println("Buscando el Libro " + tit + " para borrarlo");
        try {
            //Obtiene el elemento raíz del DOM
            Node raiz = doc.getDocumentElement(); 
            //Obtiene todos los nodos Titulo
            NodeList nl1 = doc.getElementsByTagName("Titulo"); 
            Node n1;
            //Hago un for que itera sobre cada nodo de la lista nl1
            for (int i = 0; i < nl1.getLength(); i++) {
                //Obtengo el nodo en la posición i de la lista nl1 y lo asigno a la variable n1
                n1 = nl1.item(i);
                //Este if comprueba que n1 sea un nodo de elemento y que su texto sea igual que tit
                if (n1.getNodeType() == Node.ELEMENT_NODE && n1.getTextContent().equals(tit)) {
                    System.out.println("Borrando el nodo <Libro> con título " + tit);
                    //Obtiene el nodo padre del nodo Titulo
                    Node libroNode = n1.getParentNode().getParentNode(); 
                    //Elimina el nodo libro
                    raiz.removeChild(libroNode); 
                }
            }
            System.out.println("Nodo borrado");
            return 0; //Devuelvo 0
        } catch (DOMException e) {
            System.out.println(e);
            return -1; //Devuelvo -1 si hay un error
        }
    }

    //Método para guardar el DOM modificado como un nuevo archivo XML
    void guardarDOMcomoArchivo(String nuevoArchivo) {
        try {
            //Creo una fuente DOMSource con el documento
            Source src = new DOMSource(doc); 
            //Establezco el destino del archivo
            StreamResult rst = new StreamResult(new File(nuevoArchivo));

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            //Establece la propiedad de indentacion
            transformer.setOutputProperty(OutputKeys.INDENT, "yes"); 
            //Realiza la transformacion
            transformer.transform(src, rst); 

            System.out.println("Archivo creado del DOM con éxito\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
