package com.tumbleweed.platform.trunk.base.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

/**
 * Created by mylover on 1/25/16.
 */
public class XMLUtil {

    private static final Logger log = LogManager.getLogger(XMLUtil.class.getName());

    private Document doc;

    public XMLUtil(String xml) {
        try {
            this.doc = DocumentHelper.parseText(xml);
        } catch (DocumentException var3) {
            log.error(var3);
            throw new RuntimeException("112613");
        }
    }

    public XMLUtil(InputStream xml) {
        try {
            SAXReader e = new SAXReader();
            this.doc = e.read(xml);
        } catch (DocumentException var3) {
            log.error(var3);
            throw new RuntimeException("112613");
        }
    }

    private Element findElement(List<Element> elements, String text) {
        Iterator var4 = elements.iterator();

        while(var4.hasNext()) {
            Element element = (Element)var4.next();
            log.debug("Get Element: " + element.getName());
            if(element.elements().size() > 0) {
                Element elementText = this.findElement(element.elements(), text);
                if(elementText != null) {
                    return elementText;
                }

                log.debug("continue find next node.");
            } else {
                String elementText1 = element.getName();
                if(elementText1.equalsIgnoreCase(text)) {
                    log.debug("found the samed node [" + text + "].");
                    return element;
                }

                log.debug("continue find node [" + text + "].");
            }
        }

        return null;
    }

    public Element getChildElement(Element root, String visitedNodeName) {
        try {
            log.debug("RootElement: " + root.getName());
            Element e = this.findElement(root.elements(), visitedNodeName);
            if(e != null) {
                return e;
            }
        } catch (Exception var4) {
            log.error(var4);
        }

        log.warn("Can\'t find Element: " + visitedNodeName);
        return DocumentHelper.createElement("");
    }

    public Element getChildElement(String visitedNodeName) {
        return this.getChildElement(this.doc.getRootElement(), visitedNodeName);
    }

    public List<Element> getChildElementList(Element root) {
        return root.elements();
    }

    public List<Element> getChildElementList() {
        return this.getChildElementList(this.doc.getRootElement());
    }
}
