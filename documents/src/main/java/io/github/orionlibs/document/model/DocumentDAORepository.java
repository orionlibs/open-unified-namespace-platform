package io.github.orionlibs.document.model;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentDAORepository extends JpaRepository<DocumentModel, Integer>
{
    /**
     * Finds documents by type
     * @param type The type of document
     * @return The documents of given type
     */
    List<DocumentModel> findAllByType(DocumentType.Type type);


    @Query(value = "SELECT 1", nativeQuery = true)
    Integer testConnection();
}
