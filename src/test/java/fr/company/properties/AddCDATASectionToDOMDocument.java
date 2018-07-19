package fr.company.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

public class AddCDATASectionToDOMDocument {

	private static final String TAB = "    ";

	public static void main(String[] args) throws Exception {

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setValidating(false);
		DocumentBuilder db = dbf.newDocumentBuilder();

		Document doc = db.parse(new FileInputStream(new File("C:/tools/in.xml")));

		Element valueNode = doc.createElement("Value");
		CDATASection cdata = doc.createCDATASection("gfdnlgn<fdkgdfnk");
		valueNode.appendChild(cdata);
		doc.getFirstChild().appendChild(valueNode);

		System.out.println("11111111111111111111");
		prettyPrint(doc);
		System.out.println("11111111111111111111");
		System.out.println("22222222222222222222");
		System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
		if (doc.hasChildNodes()) {
			printNote(doc.getChildNodes());
		}
		System.out.println("22222222222222222222");
		System.out.println("33333333333333333333");
		write(doc);
		System.out.println("33333333333333333333");
		System.out.println("44444444444444444444");
		OutputFormat format = new OutputFormat(doc);
		XMLSerializer serializer = new XMLSerializer(System.out, format);
		serializer.serialize(doc);
		System.out.println("\n44444444444444444444");
	}

	private static void write(Document doc) throws IOException {
		outputHeading(doc);
		outputElement(doc.getDocumentElement(), "");
	}

	private static void outputElement(Element node, String indent) {
		System.out.print(indent + "<" + node.getTagName());
		NamedNodeMap nm = node.getAttributes();
		for (int i = 0; i < nm.getLength(); i++) {
			Attr attr = (Attr) nm.item(i);
			System.out.print(" " + attr.getName() + "=\"" + attr.getValue() + "\"");
		}
		System.out.println(">");
		NodeList list = node.getChildNodes();
		for (int i = 0; i < list.getLength(); i++)
			outputloop(list.item(i), indent + TAB);
		System.out.println(indent + "</" + node.getTagName() + ">");
	}

	private static void outputText(Text node, String indent) {
		System.out.println(indent + node.getData());
	}

	private static void outputCDATASection(CDATASection node, String indent) {
		System.out.println(indent + node.getData() );
	}

	private static void outputComment(Comment node, String indent) {
		System.out.println(indent + "<!-- " + node.getData() + " -->");
	}

	private static void outputProcessingInstructionNode(ProcessingInstruction node, String indent) {
		System.out.println(indent + "<?" + node.getTarget() + " " + node.getData() + "?>");
	}

	private static void outputloop(Node node, String indent) {
		switch (node.getNodeType()) {
		case Node.ELEMENT_NODE:
			outputElement((Element) node, indent);
			break;
		case Node.TEXT_NODE:
			outputText((Text) node, indent);
			break;
		case Node.CDATA_SECTION_NODE:
			outputCDATASection((CDATASection) node, indent);
			break;
		case Node.COMMENT_NODE:
			outputComment((Comment) node, indent);
			break;
		case Node.PROCESSING_INSTRUCTION_NODE:
			outputProcessingInstructionNode((ProcessingInstruction) node, indent);
			break;
		default:
			System.out.println("Unknown node type: " + node.getNodeType());
			break;
		}
	}


	private static void outputHeading(Document doc) {
		System.out.print("<?xml version=\"1.0\"");
		DocumentType doctype = doc.getDoctype();
		if (doctype != null) {
			if ((doctype.getPublicId() == null) && (doctype.getSystemId() == null)) {
				System.out.println(" standalone=\"yes\"?>");
			} else {
				System.out.println(" standalone=\"no\"?>");
			}
		} else {
			System.out.println("?>");
		}
	}

	public static final void prettyPrint(Document xml) throws Exception {
		Transformer tf = TransformerFactory.newInstance().newTransformer();
		tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		tf.setOutputProperty(OutputKeys.INDENT, "yes");
		Writer out = new StringWriter();
		tf.transform(new DOMSource(xml), new StreamResult(out));
		System.out.println(out.toString());
	}

	private static void printNote(NodeList nodeList) {

		for (int count = 0; count < nodeList.getLength(); count++) {

			Node tempNode = nodeList.item(count);

			// make sure it's element node.
			if (tempNode.getNodeType() == Node.ELEMENT_NODE) {

				// get node name and value
				System.out.println("\nNode Name =" + tempNode.getNodeName() + " [OPEN]");
				System.out.println("Node Value =" + tempNode.getNodeValue() + " --- " + tempNode.getTextContent());

				if (tempNode.hasAttributes()) {

					// get attributes names and values
					NamedNodeMap nodeMap = tempNode.getAttributes();

					for (int i = 0; i < nodeMap.getLength(); i++) {

						Node node = nodeMap.item(i);
						System.out.println("attr name : " + node.getNodeName());
						System.out.println("attr value : " + node.getNodeValue() + " -- " + node.getTextContent());
						;

					}

				}

				if (tempNode.hasChildNodes()) {

					// loop again if has child nodes
					printNote(tempNode.getChildNodes());

				}

				System.out.println("Node Name =" + tempNode.getNodeName() + " [CLOSE]");

			}

		}

	}

}

