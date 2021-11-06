package com.swl.config.swagger;

import com.google.common.base.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.spring.web.DescriptionResolver;
import springfox.documentation.swagger.common.SwaggerPluginSupport;


@Component
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER)
public class OperationNotesResourcesReader
        implements springfox.documentation.spi.service.OperationBuilderPlugin {
    private final DescriptionResolver descriptions;

    final static Logger logger = LoggerFactory.getLogger(OperationNotesResourcesReader.class);

    @Autowired
    public OperationNotesResourcesReader(DescriptionResolver descriptions) {
        this.descriptions = descriptions;
    }

    @Override
    public void apply(OperationContext context) {
        try {
            Optional<ApiRoleAccessNotes> methodAnnotation =
                    context.findAnnotation(ApiRoleAccessNotes.class);
            if (!methodAnnotation.isPresent()) {
                // the REST Resource does not have the @ApiRoleAccessNotes annotation -> ignore
                return;
            }

            String apiRoleAccessNoteText;
            // get @Secured annotation
            Optional<Secured> securedAnnotation = context.findAnnotation(Secured.class);
            if (!securedAnnotation.isPresent()) {
                apiRoleAccessNoteText = "Accessible by all user roles";
            } else {
                apiRoleAccessNoteText = "Accessible by users having one of the following roles: ";
                Secured secure = securedAnnotation.get();
                for (String role : secure.value()) {
                    // add the roles to the notes. Use Markdown notation to create a list
                    apiRoleAccessNoteText = apiRoleAccessNoteText + "\n * " + String.join("\n * ", role);
                }
            }
            // add the note text to the Swagger UI
            context.operationBuilder().notes(descriptions.resolve(apiRoleAccessNoteText));
        } catch (Exception e) {
            logger.error("Error when creating swagger documentation for security roles: " + e);
        }
    }

    @Override
    public boolean supports(DocumentationType delimiter) {
        return SwaggerPluginSupport.pluginDoesApply(delimiter);
    }
}