package sheettastic.templates;

import org.springframework.stereotype.Component;

@Component
public class TemplateRepository {

    public Template fetchById(String id){
        Template template = new Template(id);
        template
                .add(
                        FieldCapture.builder().displayName("alias").fieldName("alias").build()
                )
                .add(
                        FieldCapture.builder().displayName("taxon id").fieldName("taxonId").fieldType(JsonFieldType.IntegerNumber).build()
                )
                .add(
                        FieldCapture.builder().displayName("taxon").fieldName("taxon").build()
                );

        template.setDefaultCapture(
                AttributeCapture.builder().build()
        );


        return template;
    }


}
