package com.referencedatacodegen.json;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import javax.annotation.Generated;
import javax.lang.model.element.Modifier;

import static com.referencedatacodegen.util.FieldSanitizer.getCleanFieldName;

public class JSONCodeGen {

    public static void generateUnitOfMeasure() throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(JSONCodeGen.class.getClassLoader().getResourceAsStream("UnitOfMeasure.json")))) {
            List<UnitOfMeasure> unitsOfMeasure = new Gson().fromJson(bufferedReader, new TypeToken<List<UnitOfMeasure>>(){}.getType());

            TypeSpec.Builder typeBuilder = TypeSpec.classBuilder("UnitOfMeasure")
                    .addAnnotation(AnnotationSpec.builder(Generated.class)
                            .addMember("value", "$L", "\"com.referencedatacodegen.json.JSONCodeGen\"")
                            .build())
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL);

            for (UnitOfMeasure unitOfMeasure : unitsOfMeasure) {

                typeBuilder.addField(FieldSpec.builder(int.class, getCleanFieldName(unitOfMeasure.name), Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
                        .initializer("$L", unitOfMeasure.id)
                        .build());

                TypeSpec type = typeBuilder.build();

                JavaFile javaFile = JavaFile.builder("com.vitalityactive.va.constants", type)
                        .indent("    ")
                        .build();

                javaFile.writeTo(new File("../app/src/main/java/"));
            }
        }
    }

    private static class UnitOfMeasure {
        int id;
        String name;
    }
}
