package com.agileengine;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import static com.agileengine.JsoupCssSelectSnippet.findElementsByQuery;

public class Main {

    private static Logger LOGGER = LoggerFactory.getLogger(Main.class);
    private static String targetElementId = "make-everything-ok-button";
    private final static String OK="Ok";
    public static void main(String[] args){

        File originalHTMLFile = new File(args[0]);
        File differentHTMLFile = new File(args[1]);
//---------------- collect necessary data
        Optional<Element> originalButton = JsoupFindByIdSnippet.findElementById(originalHTMLFile, targetElementId);

        Optional<Attributes> originalAttributes =originalButton.map(Element::attributes);

        Optional<String> cssQuery = originalButton.map(button ->
                button.attributes().asList().stream()
                        .map(attr -> "a[" + attr.getKey() + "="  +"\""+ attr.getValue()+"\"]")
                        .collect(Collectors.joining(",")));
//------------------ find similar button

        Optional<Elements> elementsOpt = findElementsByQuery(differentHTMLFile, cssQuery.get());

        for (Element elementFromDiff: elementsOpt.get()){
            Attributes diffAttributes = elementFromDiff.attributes();
            boolean isSimilarButton =false;
            for (Map.Entry<String,String> entry: originalAttributes.get()) {
                isSimilarButton = isSimilarButton
                        || diffAttributes.get(entry.getKey()).equals(entry.getValue())
                        && elementFromDiff.hasText()
                        && elementFromDiff.text().toLowerCase().contains(OK.toLowerCase());

            }

            if (isSimilarButton){
                LOGGER.info(elementFromDiff.cssSelector());
            }else {
                LOGGER.info("Button Not Found");
            }
        }


    }


}
