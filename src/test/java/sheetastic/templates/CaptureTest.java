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
import java.util.List;

/**
 * Created by Dave on 18/10/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class CaptureTest {

    private String[] headers = {"alias", "taxon id", "weight","units","taxon"};
    private String[] values = {"a sample", "9606", "75","kg","Homo sapiens"};


    @Autowired
    private SheetHelper sheetHelper;

    private Template template;
    private  Sheet sheet;

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
        Template loadedTemplate = mapper.readValue(file,Template.class);

        Assert.assertEquals(template,loadedTemplate);
    }

    @Test
    public void test() {
        JSONObject expected = new JSONObject();
        JSONArray attributes = new JSONArray();

        JSONObject weightAttribute = new JSONObject();
        attributes.put(weightAttribute);

        weightAttribute.put("name","weight");
        weightAttribute.put("value","75");
        weightAttribute.put("units","kg");


        expected.put("alias","a sample");
        expected.put("taxonId",9606L);

        expected.put("attributes", attributes);
        expected.put("taxon","Homo sapiens");

        sheetHelper.beforeCreate(sheet);
        List<JSONObject> jsonObjects = sheetHelper.parse(sheet);


        JSONObject actual = jsonObjects.get(0);

        Assert.assertEquals(expected.toString(),actual.toString());
    }
}
