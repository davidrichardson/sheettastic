package sheettastic.sheets;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.NonNull;
import org.json.JSONObject;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import sheettastic.samples.Sample;
import sheettastic.samples.SampleRepo;
import sheettastic.templates.Template;
import sheettastic.templates.TemplateRepository;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Dave on 21/10/2017.
 */
@Data
@RepositoryRestController
public class SheetsController {

    @NonNull
    private CsvStreamToSheetConverter csvStreamToSheetConverter;

    @NonNull
    private SheetRepository sheetRepository;

    @NonNull
    private SheetHelper sheetHelper;

    @NonNull
    private TemplateRepository templateRepository;

    @NonNull
    private SampleRepo sampleRepo;

    @NonNull
    private ObjectMapper objectMapper;

    @RequestMapping(path = "/sheets/{templateName}", method = RequestMethod.POST, consumes = {"text/csv"})
    public ResponseEntity<Sheet> uploadCsv(@PathVariable String templateName, InputStream inputStream) throws IOException {

        Template template = templateRepository.fetchById(templateName);

        Sheet sheet = new Sheet();
        sheet.setTemplate(template);
        csvStreamToSheetConverter.convert(sheet, inputStream);

        sheet.setTemplate(template);

        sheetHelper.beforeCreate(sheet);

        sheetRepository.insert(sheet);


        ResponseEntity<Sheet> responseEntity = new ResponseEntity<>(sheet, HttpStatus.CREATED);

        return responseEntity;
    }

    @RequestMapping(path = "/sheets/{sheetId}/convert", method = RequestMethod.POST)
    public ResponseEntity convertSample(@PathVariable String sheetId)  {

        Sheet sheet = sheetRepository.findOne(sheetId);

        sheetHelper.parse(sheet)
                .map(JSONObject::toString)
                .map(json -> {
                    try {
                        return objectMapper.readValue(json, Sample.class);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }) //ENSURE Aliases set
                .forEach(sample -> sampleRepo.save(sample)); //BEFORE SAVE, AFTER SAVE LIFECYCLE

        return new ResponseEntity(HttpStatus.CREATED);

    }


    /**
     * Consider a command approach here (instead of REST)
     *
     * user commands:
     *  - change header row (
     *  - ignore column
     *  - ignore row
     *  - set this column to that mapping
     *  - change template
     *
     */

}
