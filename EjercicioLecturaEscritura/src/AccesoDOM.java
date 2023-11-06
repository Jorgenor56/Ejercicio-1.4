
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;
import org.w3c.dom.Document;
import java.io.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.xml.sax.SAXException;

public class AccesoDOM {

    Document doc;

    public int abriXMLaDOM(File f) {
        try {
            System.out.println("Abriendo archivo XML file y generando DOM....;");

            DocumentBuilderFactory factory
                    = DocumentBuilderFactory.newInstance();

            factory.setIgnoringComments(true);
            factory.setIgnoringElementContentWhitespace(true);

            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.parse(f);

            System.out.println("DOM creado con éxito.");
            return 0;
        } catch (IOException | ParserConfigurationException | SAXException e) {
            System.out.println(e);
            return -1;
        }
    }

    public void recorreDOMyMuestra() {
        String[] datos = new String[3];
        Node nodo = null;
        Node root = doc.getFirstChild();
        NodeList nodelist = root.getChildNodes();

        for (int i = 0; i < nodelist.getLength(); i++) {
            nodo = nodelist.item(i);
            if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                Node ntemp = null;
                int contador = 1;

                datos[0] = nodo.getAttributes().item(0).getNodeValue();

                NodeList nl2 = nodo.getChildNodes();
                for (int j = 0; j < nl2.getLength(); j++) {
                    ntemp = nl2.item(j);
                    if (ntemp.getNodeType() == Node.ELEMENT_NODE) {

                        if (ntemp.getNodeType() == Node.ELEMENT_NODE) {

                            datos[contador] = ntemp.getTextContent();

                            contador++;

                        }
                    }

                }
                System.out.println(datos[0] + "-- " + datos[2] + "-- " + datos[1]);
            }
        }

    }

    public int insertarLibroEnDOM(String titulo, String autor, String fecha) {
        try {
            System.out.println("Añadir libro al árbol DOM:" + titulo + " " + autor + " " + fecha);

//crea los nodos=&gt;los añade al padre desde las hojas a la raíz
//CREATE TITULO con el texto en medio
            Node ntitulo = doc.createElement("Titulo ");//crea etiquetas & lt;Titulo & gt;...&lt; / Titulo & gt;

            Node ntitulo_text = doc.createTextNode(titulo);//crea el nodo texto para el Titulo 

            ntitulo
                    .appendChild(ntitulo_text);//añade el titulo a las etiquetas =  & gt; & lt;Titulo & gt;titulo & lt; / Titulo & gt;

            //Node nautor = doc.createElement("Autor ").appendChild(doc.createTextNode(autor));//one line doesn't work 
            //CREA AUTOR
            Node nautor = doc.createElement("Autor");

            Node nautor_text = doc.createTextNode(autor);
            nautor.appendChild(nautor_text);
//CREA LIBRO, con atributo y nodos Título y Autor
            Node nLibro = doc.createElement("Libro");
            ((Element) nLibro).setAttribute("publicado ", fecha);
            nLibro.appendChild(ntitulo);
            nLibro.appendChild(nautor);
//APPEND LIBRO TO THE ROOT
            nLibro.appendChild(doc.createTextNode("\n &"));//para insertar salto de línea
            Node raiz = doc.getFirstChild();//tb. doc.getChildNodes().item(0)
            raiz.appendChild(nLibro);
            System.out.println("Libro insertado en DOM.");
            return 0;
        } catch (DOMException e) {
            System.out.println(e);
            return -1;
        }
    }

    public int deleteNode(String tit) {
        System.out.println("Buscando el Libro " + tit + " para borrarlo");
        try {
            // Node root = doc.getFirstChild();
            Node raiz = doc.getDocumentElement();
            NodeList nl1 = doc.getElementsByTagName("Titulo");
            Node n1;
            for (int i = 0; i < nl1.getLength(); i++) {
                n1 = nl1.item(i);

                if (n1.getNodeType() == Node.ELEMENT_NODE) { // redundante por getElementsByTagName, no lo es si buscamos getChildNodes()

                    if (n1.getTextContent().equals(tit)) {
                        System.out.println("Borrando el nodo <Libro> con título " + tit);
                        Node libroNode = n1.getParentNode().getParentNode();
                        raiz.removeChild(libroNode);
                    }
                }
            }
            System.out.println("Nodo borrado");
            return 0;
        } catch (DOMException e) {
            System.out.println(e);
            return -1;
        }
    }

    void guardarDOMcomoArchivo(String nuevoArchivo) {
        try {
            Source src = new DOMSource(doc);

            StreamResult rst = new StreamResult(new File(nuevoArchivo));

            Transformer transformer = TransformerFactory.newInstance().newTransformer();

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            transformer.transform(src, rst);

            System.out.println("Archivo creado del DOM con éxito\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

}
