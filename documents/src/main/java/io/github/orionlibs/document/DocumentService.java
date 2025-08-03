package io.github.orionlibs.document;

import io.github.orionlibs.core.Logger;
import io.github.orionlibs.core.event.Publishable;
import io.github.orionlibs.document.api.SaveDocumentRequest;
import io.github.orionlibs.document.api.UpdateDocumentRequest;
import io.github.orionlibs.document.converter.NewDocumentDTOToEntityConverter;
import io.github.orionlibs.document.event.EventDocumentDeleted;
import io.github.orionlibs.document.event.EventDocumentDeletedAll;
import io.github.orionlibs.document.event.EventDocumentSaved;
import io.github.orionlibs.document.event.EventDocumentUpdated;
import io.github.orionlibs.document.model.DocumentDAORepository;
import io.github.orionlibs.document.model.DocumentModel;
import io.github.orionlibs.document.model.DocumentType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DocumentService implements Publishable
{
    @Autowired
    private DocumentDAORepository dao;
    @Autowired
    private NewDocumentDTOToEntityConverter newDocumentDTOToEntityConverter;


    public List<DocumentModel> getByType(DocumentType.Type documentType)
    {
        return dao.findAllByType(documentType);
    }


    public Optional<DocumentModel> getByID(Integer documentID)
    {
        return dao.findById(documentID);
    }


    public DocumentModel save(SaveDocumentRequest newDocument)
    {
        DocumentModel toSave = newDocumentDTOToEntityConverter.convert(newDocument);
        DocumentModel saved = save(toSave);
        return saved;
    }


    @Transactional
    public DocumentModel save(DocumentModel toSave)
    {
        DocumentModel saved = dao.save(toSave);
        publish(EventDocumentSaved.EVENT_NAME, EventDocumentSaved.builder()
                        .documentID(saved.getId())
                        .documentLocation(saved.getDocumentURL())
                        .build());
        Logger.info("Saved document");
        return saved;
    }


    @Transactional
    public boolean update(Integer documentID, UpdateDocumentRequest document)
    {
        Optional<DocumentModel> docTemp = getByID(documentID);
        if(docTemp.isPresent())
        {
            DocumentModel doc = docTemp.get();
            doc.setDocumentURL(document.getDocumentURL());
            doc.setType(document.getType());
            doc.setTitle(document.getTitle());
            doc.setDescription(document.getDescription());
            doc.setUpdatedAt(LocalDateTime.now());
            DocumentModel updated = save(doc);
            publish(EventDocumentUpdated.EVENT_NAME, EventDocumentUpdated.builder()
                            .documentID(updated.getId())
                            .build());
            Logger.info("Updated document");
            return true;
        }
        else
        {
            return false;
        }
    }


    public void delete(Integer documentID)
    {
        dao.deleteById(documentID);
        publish(EventDocumentDeleted.EVENT_NAME, EventDocumentDeleted.builder()
                        .documentID(documentID)
                        .build());
        Logger.info("Deleted document");
    }


    public void deleteAll()
    {
        dao.deleteAll();
        publish(EventDocumentDeletedAll.EVENT_NAME, EventDocumentDeletedAll.builder().build());
        Logger.info("Deleted all documents");
    }
}
