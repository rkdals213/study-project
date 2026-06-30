package mhk.test26restdocs.document

import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.restdocs.request.ParameterDescriptor
import org.springframework.restdocs.request.RequestDocumentation

sealed class DocsFieldType(
    val type: JsonFieldType
)

data object ARRAY : DocsFieldType(JsonFieldType.ARRAY)
data object BOOLEAN : DocsFieldType(JsonFieldType.BOOLEAN)
data object OBJECT : DocsFieldType(JsonFieldType.OBJECT)
data object NUMBER : DocsFieldType(JsonFieldType.NUMBER)
data object NULL : DocsFieldType(JsonFieldType.NULL)
data object STRING : DocsFieldType(JsonFieldType.STRING)
data object ANY : DocsFieldType(JsonFieldType.VARIES)
data object DATE : DocsFieldType(JsonFieldType.STRING)
data object DATETIME : DocsFieldType(JsonFieldType.STRING)

infix fun String.type(
    docsFieldType: DocsFieldType
): FieldDescriptor {
    return PayloadDocumentation.fieldWithPath(this).type(docsFieldType.type)
}

infix fun String.means(description: String): ParameterDescriptor {
    return RequestDocumentation.parameterWithName(this).description(description)
}

infix fun FieldDescriptor.means(description: String): FieldDescriptor {
    return this.description(description)
}

infix fun ParameterDescriptor.isOptional(value: Boolean): ParameterDescriptor {
    if (value) {
        this.optional()
    }

    return this
}

infix fun ParameterDescriptor.isIgnored(value: Boolean): ParameterDescriptor {
    if (value) {
        this.ignored()
    }

    return this
}