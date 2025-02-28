package org.example.demo.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.demo.entity.FormNtu;
import org.example.demo.repository.FormNtuRepository;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class XmlNameLoader {
    private final FormNtuRepository formNtuRepository;

    private static final String XML_DIRECTORY = "src/main/resources/xml";

    @PostConstruct
    public void loadXmlFiles() {
        File folder = new File(XML_DIRECTORY);
        File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".xml"));
        if (files != null) {
            for (File file : files) {
                processXmlFile(file);
            }
        }
    }

    private void processXmlFile(File file) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);
            doc.getDocumentElement().normalize();

            NodeList formList = doc.getElementsByTagName("form");
            List<FormNtu> newForms =
                    IntStream.range(0, formList.getLength())
                            .mapToObj(i -> (Element) formList.item(i))
                            .map(formElement -> formElement.getAttribute("name"))
                            .filter(name -> !name.isEmpty() && !formNtuRepository.existsByName(name)) // Проверка в БД
                            .map(name -> {
                                FormNtu formNtu = new FormNtu();
                                formNtu.setName(name);
                                formNtu.setFileName(file.getName().substring(0, file.getName().lastIndexOf('.')));
                                return formNtu;
                            })
                            .collect(Collectors.toList());

            if (!newForms.isEmpty()) {
                formNtuRepository.saveAll(newForms);
                System.out.println("Добавлены новые формы: " + newForms);
            }
        } catch (Exception e) {
            System.err.println("Ошибка обработки XML файла: " + file.getName() + " - " + e.getMessage());
        }
    }
}
