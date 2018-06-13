package xmlvpdf;

import java.io.File;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;

import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.FileUtils;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import vao.Zapis;

public class Xml {
	
    String home = System.getProperty("user.home");
   
	
	public void writeXmlFile(ArrayList<Zapis> list) throws FOPException, TransformerException, IOException {

	    try {

	        DocumentBuilderFactory dFact = DocumentBuilderFactory.newInstance();
	        DocumentBuilder build = dFact.newDocumentBuilder();
	        Document doc = build.newDocument();

	        Element root = doc.createElement("recept");
	        doc.appendChild(root);
            
	        Element podrobnosti = doc.createElement("podrobnosti");
	        root.appendChild(podrobnosti);

	       


	        for (Zapis dtl : list) {

	            Element name = doc.createElement("id");
	            name.appendChild(doc.createTextNode(String.valueOf(dtl
	                    .getId())));
	            podrobnosti.appendChild(name);

	            Element id = doc.createElement("cas");
	            id.appendChild(doc.createTextNode(String.valueOf(dtl.getDatumCas())));
	            podrobnosti.appendChild(id);

	            Element mmi = doc.createElement("tip");
	            mmi.appendChild(doc.createTextNode(String.valueOf(dtl.getTip())));
	            podrobnosti.appendChild(mmi);
	            
	            Element x = doc.createElement("avtor");
	            x.appendChild(doc.createTextNode(String.valueOf(dtl.getAvtor())));
	            podrobnosti.appendChild(x);
	            

	            Element y = doc.createElement("dopolnila");
	            for(int i=0;i<dtl.getDopolnila().size();i++) 
	            y.appendChild(doc.createTextNode(String.valueOf(dtl.getDopolnila().get(i)+" ")));
	            
	            podrobnosti.appendChild(y);
	            

	        }

	        // Save the document to the disk file
	        TransformerFactory tranFactory = TransformerFactory.newInstance();
	        Transformer aTransformer = tranFactory.newTransformer();

	        // format the XML nicely
	        aTransformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");

	        aTransformer.setOutputProperty(
	                "{http://xml.apache.org/xslt}indent-amount", "4");
	        aTransformer.setOutputProperty(OutputKeys.INDENT, "yes");

	        DOMSource source = new DOMSource(doc);
	        try {
	            // location and name of XML file you can change as per need
	            FileWriter fos = new FileWriter(home+ "//recept.xml");
	            StreamResult result = new StreamResult(fos);
	            aTransformer.transform(source, result);

	        } catch (IOException e) {

	            e.printStackTrace();
	        }

	    } catch (TransformerException ex) {
	        System.out.println("Error outputting document");

	    } catch (ParserConfigurationException ex) {
	        System.out.println("Error building document");
	        
	        
	        
	        
	        
	    }
	    File dataDir = new File(System.getProperty("jboss.server.data.dir"));
	    File xsltFile = new File(dataDir,"template.xsl");
        // the XML file which provides the input
        StreamSource xmlSource = new StreamSource(new File(home + "//recept.xml"));
        // create an instance of fop factory
        FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
        // a user agent is needed for transformation
        FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
        // Setup output
        OutputStream out;
        out = new java.io.FileOutputStream(home + "//desktop//recept"+list.get(0).getId()+".pdf");
        
        try {
            // Construct fop with desired output format
            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);

            // Setup XSLT
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(new StreamSource(xsltFile));

            // Resulting SAX events (the generated FO) must be piped through to
            // FOP
            Result res = new SAXResult(fop.getDefaultHandler());

            // Start XSLT transformation and FOP processing
            // That's where the XML is first transformed to XSL-FO and then
            // PDF is created
            transformer.transform(xmlSource, res);

            
        } finally {
            out.close();
        }
	    
	}
	
	

}
