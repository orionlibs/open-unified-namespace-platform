package io.github.orionlibs.document.converter;

import io.github.orionlibs.core.converter.Converter;
import io.github.orionlibs.document.api.DocumentDTO;
import io.github.orionlibs.document.model.DocumentModel;
import org.springframework.stereotype.Component;

@Component
public class DocumentEntityToDTOConverter implements Converter<DocumentModel, DocumentDTO>
{
    @Override
    public DocumentDTO convert(DocumentModel objectToConvert)
    {
        if(objectToConvert == null)
        {
            return null;
        }
        return new DocumentDTO(objectToConvert.getDocumentURL(),
                        objectToConvert.getType(),
                        objectToConvert.getTitle(),
                        objectToConvert.getDescription(),
                        objectToConvert.getCreatedAt(),
                        objectToConvert.getUpdatedAt());
    }
}
