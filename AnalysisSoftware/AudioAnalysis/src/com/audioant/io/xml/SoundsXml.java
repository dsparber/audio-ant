package com.audioant.io.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.ProcessingInstruction;

import com.audioant.audio.model.Sound;
import com.audioant.config.Config;

public class SoundsXml {

	private String pathname;

	public SoundsXml(String pathname) {
		this.pathname = pathname;
	}

	public void write(List<Sound> soundModels) throws ParserConfigurationException, TransformerException {

		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

		Document doc = docBuilder.newDocument();

		Element rootElement = doc.createElement(Config.SOUNDS_XML_ROOT);
		doc.appendChild(rootElement);

		doc.setXmlStandalone(true);
		ProcessingInstruction pi = doc.createProcessingInstruction("xml-stylesheet",
				"type=\"text/xsl\" href=\"sounds.xsl\"");

		doc.insertBefore(pi, rootElement);

		for (Sound model : soundModels) {

			Element sound = doc.createElement(Config.SOUNDS_XML_SOUND);
			rootElement.appendChild(sound);

			Element named = doc.createElement(Config.SOUNDS_XML_NAMED);
			named.appendChild(doc.createTextNode(Boolean.toString(model.isNamed())));
			sound.appendChild(named);

			Element number = doc.createElement(Config.SOUNDS_XML_NUMBER);
			number.appendChild(doc.createTextNode(Integer.toString(model.getId())));
			sound.appendChild(number);

			Element name = doc.createElement(Config.SOUNDS_XML_NAME);
			name.appendChild(doc.createTextNode(model.getNameNotNull()));
			sound.appendChild(name);

			Element path = doc.createElement(Config.SOUNDS_XML_PATH);
			path.appendChild(doc.createTextNode(model.getPath()));
			sound.appendChild(path);

			Element alertId = doc.createElement(Config.SOUNDS_XML_ALERT_ID);
			alertId.appendChild(doc.createTextNode(model.getAlertIdNotNull()));
			sound.appendChild(alertId);
		}

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);

		StreamResult result = new StreamResult(new File(pathname));

		transformer.transform(source, result);

	}

	public List<Sound> read() throws FileNotFoundException, XMLStreamException {
		List<Sound> sounds = new ArrayList<Sound>();

		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		InputStream in = new FileInputStream(pathname);
		XMLEventReader eventReader = inputFactory.createXMLEventReader(in);

		Sound sound = null;

		while (eventReader.hasNext()) {

			XMLEvent event = eventReader.nextEvent();

			if (event.isStartElement()) {

				String name = event.asStartElement().getName().getLocalPart();

				if (name.equals(Config.SOUNDS_XML_SOUND)) {
					sound = new Sound();
				} else {

					if (name.equals(Config.SOUNDS_XML_NAME)) {
						if ((event = eventReader.nextEvent()).isCharacters()) {
							sound.setName(event.asCharacters().getData());
						}
					}
					if (name.equals(Config.SOUNDS_XML_NUMBER)) {
						event = eventReader.nextEvent();
						sound.setId(Integer.parseInt(event.asCharacters().getData()));
					}
					if (name.equals(Config.SOUNDS_XML_PATH)) {
						event = eventReader.nextEvent();
						sound.setPath(event.asCharacters().getData());
					}
					if (name.equals(Config.SOUNDS_XML_ALERT_ID)) {
						event = eventReader.nextEvent();
						sound.setAlertId(event.asCharacters().getData());
					}
				}
			}
			if (event.isEndElement()) {
				if (event.asEndElement().getName().getLocalPart().equals(Config.SOUNDS_XML_SOUND)) {
					sounds.add(sound);
				}
			}

		}
		return sounds;
	}
}