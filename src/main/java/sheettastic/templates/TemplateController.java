package sheettastic.templates;

import lombok.Data;
import lombok.NonNull;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sheettastic.sheets.Sheet;

import java.io.IOException;

@Data
@Controller
public class TemplateController {

    @NonNull
    private TemplateRepository templateRepository;

    @NonNull
    TemplateToSheet templateToSheet;

    @ResponseBody
    @RequestMapping(
            path = "templates/{templateName}",
            produces = {
                    "text/csv;charset=UTF-8",
                    "text/csv"
            })
    public Sheet templateAsSheet(@PathVariable String templateName) throws IOException {

        Template template = templateRepository.fetchById(templateName);

        if (template == null) {
            throw new ResourceNotFoundException();
        }

        Sheet templateSheet = templateToSheet.convert(template);

        return templateSheet;

    }

}
