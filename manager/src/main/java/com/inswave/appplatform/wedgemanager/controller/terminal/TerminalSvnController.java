package com.inswave.appplatform.wedgemanager.controller.terminal;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.difflib.DiffUtils;
import com.github.difflib.UnifiedDiffUtils;
import com.github.difflib.patch.Patch;
import com.inswave.appplatform.svn.SvnManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.subversion.javahl.ClientException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(path = "/api/wem/terminal/svn")
public class TerminalSvnController {

    private SvnManager svnManager;

    public TerminalSvnController() {
        svnManager = SvnManager.getInstance();
    }

    @PostMapping(path = "/diff")
    public ResponseEntity<String> getFileContent(@RequestBody JsonNode req) throws ClientException, JDOMException {
        String repositoryName = req.get("repositoryName").asText();
        String path = req.get("path").asText();
        long revisionOrigin = req.get("revisionOrigin").asLong();
        long revisionOther = req.get("revisionOther").asLong();
        String contentOrigin = svnManager.getFileContent(repositoryName, path, revisionOrigin);
        String contentOther = svnManager.getFileContent(repositoryName, path, revisionOther);

        List<String> originScriptLines = Arrays.asList(contentOrigin.split("\n"));
        List<String> otherScriptLines = Arrays.asList(contentOther.split("\n"));
        Patch<String> patchScript = DiffUtils.diff(originScriptLines, otherScriptLines);

        String result = UnifiedDiffUtils.generateUnifiedDiff(String.format("%s", path, revisionOrigin),
                                                             String.format("%s", path, revisionOther),
                                                             originScriptLines,
                                                             patchScript,
                                                             3)
                                        .stream()
                                        .collect(Collectors.joining("\n"));

        //        String[] result = new String[2];
        //        try {
        //
        //            Map<String, String> originWS5 = parseWS5File(contentOrigin);
        //            Map<String, String> otherWS5 = parseWS5File(contentOther);
        //
        //            List<String> originScriptLines = Arrays.asList(originWS5.get("script").split("\n"));
        //            List<String> otherScriptLines = Arrays.asList(otherWS5.get("script").split("\n"));
        //            Patch<String> patchScript = DiffUtils.diff(originScriptLines, otherScriptLines);
        //
        //            result[0] = UnifiedDiffUtils.generateUnifiedDiff(String.format("%s.js", path, revisionOrigin),
        //                                                             String.format("%s.js", path, revisionOther),
        //                                                             originScriptLines,
        //                                                             patchScript,
        //                                                             3)
        //                                        .stream()
        //                                        .collect(Collectors.joining("\n"));
        //
        //            List<String> originDesignLines = Arrays.asList(originWS5.get("design").split("\n"));
        //            List<String> otherDesignLines = Arrays.asList(otherWS5.get("design").split("\n"));
        //            Patch<String> patchDesign = DiffUtils.diff(originScriptLines, otherScriptLines);
        //
        //            result[1] = UnifiedDiffUtils.generateUnifiedDiff(String.format("%s", path, revisionOrigin),
        //                                                             String.format("%s", path, revisionOther),
        //                                                             originScriptLines,
        //                                                             patchDesign,
        //                                                             3)
        //                                        .stream()
        //                                        .collect(Collectors.joining("\n"));
        //
        //        } catch (IOException e) {
        //            e.printStackTrace();
        //        }

        return ResponseEntity.ok(result);
    }

    public Map<String, String> parseWS5File(String file) throws IOException, JDOMException {
        StringReader stringReader = new StringReader(file);
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(stringReader);
        Element root = doc.getRootElement();
        Element ws5 = root.detach();
        Element escript = ws5.getChildren().stream()
                             .filter(element -> element.getName().equalsIgnoreCase("head"))
                             .findFirst()
                             .get().getChildren().stream()
                             .filter(element -> element.getName().equalsIgnoreCase("script"))
                             .findFirst()
                             .get().detach();

        String scriptStr = escript.getText();
        String designStr = element2String(ws5);

        Map<String, String> ws5file = new HashMap<>();
        ws5file.put("script", scriptStr);
        ws5file.put("design", designStr);
        return ws5file;
    }

    private static XMLOutputter newXMLOutputter() {
        Format format = Format.getPrettyFormat();
        format.setEncoding("UTF-8");
        format.setLineSeparator(System.getProperty("line.separator"));
        return new XMLOutputter(format);
    }

    public static String element2String(Element doc) {
        return newXMLOutputter().outputString(doc);
    }
}
