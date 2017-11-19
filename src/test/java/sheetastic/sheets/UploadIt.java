package sheetastic.sheets;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.http.HttpEntity;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import sheettastic.App;
import sheettastic.sheets.Sheet;
import sheettastic.sheets.SheetRepository;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Dave on 21/10/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UploadIt {

    @LocalServerPort
    private int port;
    private String rootUri;

    @Autowired
    private SheetRepository sheetRepository;

    @Before
    public void buildUp() {
        rootUri = "http://localhost:" + port;
        sheetRepository.deleteAll();
    }

    @After
    public void tearDown() {
        sheetRepository.deleteAll();
    }

    @Test
    public void uploadCsv() throws UnirestException {
        _uploadCsv();
    }

    private Sheet _uploadCsv() throws UnirestException {
        String csv = String.join(
                "\n",
                Arrays.asList(
                        "a,b,c",
                        "4,5,6",
                        "d,e,f",
                        "x,y,z",
                        "#this is a comment,a,b,c,d"
                )
        );

        Assert.assertEquals(0, sheetRepository.findAll().size());

        HttpResponse<JsonNode> response = Unirest.post(rootUri + "/sheets/samples")
                .header("content-type", "text/csv")
                .body(csv)
                .asJson();

        List<Sheet> sheets = sheetRepository.findAll();

        Assert.assertEquals(1, sheets.size());

        Sheet sheet = sheets.get(0);

        Assert.assertNotNull(sheet);
        Assert.assertNotNull(sheet.getId());

        Sheet expectedSheet = new Sheet();
        expectedSheet.addRow(new String[]{"a", "b", "c"});
        expectedSheet.addRow(new String[]{"4", "5", "6"});
        expectedSheet.addRow(new String[]{"d", "e", "f"});
        expectedSheet.addRow(new String[]{"x", "y", "z"});
        expectedSheet.addRow(new String[]{"#this is a comment", "a", "b", "c", "d"});
        expectedSheet.getRows().get(4).setIgnored(true);

        Assert.assertEquals(expectedSheet.getRows(), sheet.getRows());

        return sheet;
    }

    @Test
    public void testProjection() throws UnirestException {
        Sheet sheet = _uploadCsv();

        String url = MessageFormat.format("{0}/sheets/{1}?projection=truncatedColumns", rootUri, sheet.getId());

        HttpResponse<JsonNode> response = Unirest.get(url).asJson();
        System.out.println(response.getBody().toString());
    }


    @Test
    public void uploadCsvWithEmptyBits() throws UnirestException {
        String csv = String.join(
                "\n",
                Arrays.asList(
                        "a,,b,c,,,",
                        ",,,,,,,",
                        "4,,5,6,,",
                        ",,,,,,,"
                )
        );

        Assert.assertEquals(0, sheetRepository.findAll().size());

        HttpResponse<JsonNode> response = Unirest.post(rootUri + "/sheets/samples")
                .header("content-type", "text/csv")
                .body(csv)
                .asJson();

        List<Sheet> sheets = sheetRepository.findAll();

        Assert.assertEquals(1, sheets.size());

        Sheet sheet = sheets.get(0);

        Assert.assertNotNull(sheet);
        Assert.assertNotNull(sheet.getId());

        Sheet expectedSheet = new Sheet();
        expectedSheet.setHeaderRowIndex(0);
        expectedSheet.addRow(new String[]{"a", "", "b", "c"});
        expectedSheet.addRow(new String[]{"4", "", "5", "6"});

        Assert.assertEquals(expectedSheet.getRows(), sheet.getRows());
    }

}
