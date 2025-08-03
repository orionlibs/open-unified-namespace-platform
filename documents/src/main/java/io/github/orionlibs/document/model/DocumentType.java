package io.github.orionlibs.document.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DocumentType
{
    public enum Type
    {
        DOCUMENTATION,
        OTHER
    }


    @NotNull(message = "document type must be provided")
    private Type type;
}
