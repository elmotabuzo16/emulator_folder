package com.referencedatacodegen.csv;

import com.opencsv.CSVReader;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.annotation.Generated;
import javax.lang.model.element.Modifier;

import static com.referencedatacodegen.util.FieldSanitizer.getCleanFieldName;

public class CSVCodeGen {
    public static void generateType(String csvFileName, String typeName, StartLine startLine, FieldNameIndex fieldFieldNameIndex, FieldValueIndex fieldValueIndex) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(CSVCodeGen.class.getClassLoader().getResourceAsStream(csvFileName)))) {

            CSVReader reader = new CSVReader(bufferedReader, ',', '\"', startLine.ordinal());

            TypeSpec.Builder typeBuilder = TypeSpec.classBuilder(typeName)
                    .addAnnotation(AnnotationSpec.builder(Generated.class)
                            .addMember("value", "$L", "\"com.referencedatacodegen.csv.CSVCodeGen\"")
                            .build())
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL);

            for (String[] entries : reader.readAll()) {
                typeBuilder.addField(FieldSpec.builder(int.class, getCleanFieldName(entries[fieldFieldNameIndex.ordinal()]), Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
                        .initializer("$L", Integer.valueOf(entries[fieldValueIndex.ordinal()]))
                        .build());
            }

            TypeSpec type = typeBuilder.build();

            JavaFile javaFile = JavaFile.builder("com.vitalityactive.va.constants", type)
                    .indent("    ")
                    .build();

            javaFile.writeTo(new File("../app/src/main/java/"));
        }
    }

    public enum FieldValueIndex {
        ZERO, ONE, TWO, THREE
    }

    public enum FieldNameIndex {
        ZERO,
        ONE,
        TWO,
        THREE,
        FOUR,
        FIVE
    }

    public enum StartLine {
        ZERO, ONE, TWO, THREE
    }
}
