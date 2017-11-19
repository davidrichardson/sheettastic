package sheetastic.templates;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sheettastic.App;
import sheettastic.sheets.Sheet;
import sheettastic.sheets.SheetHelper;
import sheettastic.templates.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Created by Dave on 18/10/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class CaptureTest {

    private String[] headers = {"alias", "taxon id", "weight", "units", "taxon"};
    private String[] values = {"a sample", "9606", "75", "kg", "Homo sapiens"};


    @Autowired
    private SheetHelper sheetHelper;

    private Template template;
    private Sheet sheet;

    @Before
    public void before() {
        template = Template.builder().name("samples").targetType("samples").build();
        template
                .add(
                        "alias",
                        FieldCapture.builder().fieldName("alias").build()
                )
                .add(
                        "taxon id",
                        FieldCapture.builder().fieldName("taxonId").fieldType(JsonFieldType.IntegerNumber).build()
                )
                .add(
                        "taxon",
                        FieldCapture.builder().fieldName("taxon").build()
                );

        template.setDefaultCapture(
                AttributeCapture.builder().build()
        );

        sheet = new Sheet();
        sheet.setTemplate(template);
        sheet.addRow(headers);
        sheet.addRow(values);


    }

    @Test
    public void loadTemplateFromJson() throws IOException {
        File file = new File(
                CaptureTest.class.getClassLoader()
                        .getResource("sample.template.json").getFile()
        );

        ObjectMapper mapper = new ObjectMapper();
        Template loadedTemplate = mapper.readValue(file, Template.class);

        Assert.assertEquals(template, loadedTemplate);
    }

    static String readFileToString(String path, Charset encoding) throws IOException {
        String filePath = CaptureTest.class.getClassLoader().getResource(path).getFile();
        File file = new File(filePath);
        byte[] encoded = Files.readAllBytes(file.toPath());
        return new String(encoded, encoding);
    }


    @Test
    public void test() throws IOException {

        String expectedSampleJson = readFileToString("sample.expected.json", UTF_8);
        JSONObject expected = new JSONObject(expectedSampleJson);

        sheetHelper.beforeCreate(sheet);
        List<JSONObject> jsonObjects = sheetHelper.parse(sheet).collect(Collectors.toList());


        JSONObject actual = jsonObjects.get(0);

        Assert.assertEquals(expected.toString(), actual.toString());
    }
}
