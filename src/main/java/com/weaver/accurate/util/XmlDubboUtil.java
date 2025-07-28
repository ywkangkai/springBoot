package com.weaver.accurate.util;

import cn.hutool.core.io.FileUtil;

import com.weaver.accurate.common.LoggerUtil;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;


@Slf4j
public class XmlDubboUtil {
    private static DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

    public static List<String> getDubboService(File inputStream) {
        List<String> classNames = new ArrayList<>();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setEntityResolver((publicId, systemId) -> {
//                System.out.println("Ignoring " + publicId + ", " + systemId);
                return new InputSource(new StringReader(""));
            });
            Document document = builder.parse(inputStream);
            //获取dubbo的service
            NodeList serviceList = document.getElementsByTagName("dubbo:service");
            for (int i = 0; i < serviceList.getLength(); i++) {
                Element serviceElement = (Element) serviceList.item(i);
                String className = serviceElement.getAttribute("interface");
                className = className.replace(".", "/");
                classNames.add(className);
            }
            return classNames;
        } catch (IOException | SAXException | ParserConfigurationException e) {
            LoggerUtil.error(log, "非dubbo xml文件", e.getMessage());
            return null;
        }
    }


    /**
     * 扫描dubbo xml
     *
     * @param resourcePath 资源路径
     * @return {@link List}<{@link String}>
     */
    public static List<String> scanDubboService(String resourcePath) {
        List<File> xmlFiles = FileUtil.loopFiles(new File(resourcePath), pathname -> pathname.getName().endsWith(".xml"));
        List<String> dubboServices = new ArrayList<String>();
        xmlFiles.forEach(file -> {
            List<String> dubboService = getDubboService(file);
            dubboServices.addAll(dubboService);
        });
        return dubboServices;
    }


}
