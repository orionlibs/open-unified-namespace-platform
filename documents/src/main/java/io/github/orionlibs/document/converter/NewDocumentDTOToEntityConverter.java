package io.github.orionlibs.document.converter;

import io.github.orionlibs.core.converter.Converter;
import io.github.orionlibs.document.api.SaveDocumentRequest;
import io.github.orionlibs.document.model.DocumentModel;
import org.springframework.stereotype.Component;

@Component
public class NewDocumentDTOToEntityConverter implements Converter<SaveDocumentRequest, DocumentModel>
{
    @Override
    public DocumentModel convert(SaveDocumentRequest objectToConvert)
    {
        if(objectToConvert == null)
        {
            return null;
        }
        DocumentModel model = new DocumentModel();
        model.setDocumentURL(objectToConvert.getDocumentURL());
        model.setType(objectToConvert.getType());
        model.setTitle(objectToConvert.getTitle());
        model.setDescription(objectToConvert.getDescription());
        model.setCreatedAt(objectToConvert.getCreatedAt());
        model.setUpdatedAt(objectToConvert.getUpdatedAt());
        return model;
    }
}
