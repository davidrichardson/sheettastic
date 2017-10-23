package sheetastic.templates;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import sheettastic.templates.AttributeCapture;
import sheettastic.templates.JsonType;
import sheettastic.templates.Parser;
import sheettastic.templates.SingleColumnCapture;

/**
 * Created by Dave on 18/10/2017.
 */

public class CaptureTest {

    public String[] headers = {"alias", "taxon id", "weight","units","taxon"};
    public String[] values = {"a sample", "9606", "75","kg","Homo sapiens"};

    public Parser parser = new Parser();

    @Before
    public void before() {
        parser
                .add(
                        SingleColumnCapture.builder().displayName("alias").fieldName("alias").build()
                )
                .add(
                        SingleColumnCapture.builder().displayName("taxon id").fieldName("taxonId").type(JsonType.IntegerNumber).build()
                )
                .add(
                        SingleColumnCapture.builder().displayName("taxon").fieldName("taxon").build()
                );

        parser.setDefaultCapture(
                AttributeCapture.builder().build()
        );


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


        JSONObject actual = parser.parser(headers,values);

        Assert.assertEquals(expected.toString(),actual.toString());
    }
}
