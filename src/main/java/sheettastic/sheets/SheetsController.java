package sheettastic.sheets;

import lombok.Data;
import lombok.NonNull;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

    @RequestMapping(path = "sheets", method = RequestMethod.POST, consumes = {"text/csv"})
    public ResponseEntity<Sheet> uploadCsv(InputStream inputStream) throws IOException {

        Sheet sheet = csvStreamToSheetConverter.convert(inputStream);

        sheetRepository.insert(sheet);

        ResponseEntity<Sheet> responseEntity = new ResponseEntity<Sheet>(sheet, HttpStatus.CREATED);

        return responseEntity;
    }


}