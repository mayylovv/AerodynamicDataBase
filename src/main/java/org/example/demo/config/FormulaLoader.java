package org.example.demo.config;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

@Component
@RequiredArgsConstructor
public class FormulaLoader {

    public static String loadFormula(String formName, String formulaType) {

        try {
            File file = new File("src/main/resources/xml/" + formName.toLowerCase() + ".xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName(formulaType);
            Node node = nodeList.item(0);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                return element.getTextContent();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

