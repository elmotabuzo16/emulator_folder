package com.vitalityactive.va.persistence;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

public interface DataStore {
    <T extends Model> boolean add(T model);

    <T extends Model> boolean add(List<T> models);

    <T extends Model> boolean addOrUpdate(List<T> models);

    <T extends Model> boolean addOrUpdate(T model);

    <T extends Model> boolean replaceAll(Class<T> clazz, List<T> list);

    void clear();

    <T extends Model> boolean hasModelInstance(Class<T> modelClass);

    <T extends Model> boolean hasModelInstance(Class<T> modelClass, String fieldName, String fieldValue);

    <T extends Model> boolean hasModelInstance(Class<T> modelClass, String fieldName, long fieldValue);

    <T extends Model, DTO> DTO getModelInstance(Class<T> modelClass, ModelMapper<T, DTO> modelMapper, String fieldName, String fieldValue);

    <T extends Model, DTO> DTO getModelInstance(Class<T> modelClass, ModelMapper<T, DTO> modelMapper, String fieldName, int fieldValue);

    <T extends Model, DTO> DTO getModelInstance(Class<T> modelClass, ModelMapper<T, DTO> modelMapper, String fieldName, long fieldValue);

    <T extends Model, DTO> DTO getModelInstance(Class<T> modelClass, ModelMapper<T, DTO> modelMapper, String whereFieldName1, long whereFieldValue1, String whereFieldName2, long whereFieldValue2);

    <T extends Model, DTO, U> DTO getModelInstance(Class<T> modelClass, SingleModelQueryExecutor<T, U> queryExecutor, ModelMapper<T, DTO> modelMapper);

    <T extends Model, DTO> DTO getFirstModelInstance(Class<T> modelClass, ModelMapper<T, DTO> modelMapper);

    <T extends Model> void removeAll(Class<T> modelClass);

    <T extends Model> void remove(Class<T> modelClass, String fieldName, String fieldValue);

    <T extends Model> void remove(Class<T> modelClass, String fieldName, long fieldValue);

    <T extends Model> void removeAll(Class<T> modelClass, String fieldName, long fieldValue);

    <T extends Model, DTO> List<DTO> getModels(Class<T> modelClass, ModelListMapper<T, DTO> modelListMapper);

    <T extends Model, DTO> List<DTO> getModels(Class<T> modelClass, ModelListMapper<T, DTO> modelListMapper, String fieldName, String fieldValue);

    <T extends Model, DTO, U> List<DTO> getModels(Class<T> modelClass, QueryExecutor<T, U> queryExecutor, ModelListMapper<T, DTO> modelListMapper);

    <T extends Model, FieldType> FieldType getFieldValue(Class<T> modelClass, FieldAccessor<T, FieldType> fieldAccessor);

    <T extends Model, FieldType> FieldType getFieldValue(Class<T> modelClass, String fieldName, String fieldValue, FieldAccessor<T, FieldType> fieldAccessor);

    <T extends Model, FieldType> FieldType getFieldValue(Class<T> modelClass, String fieldName, long fieldValue, FieldAccessor<T, FieldType> fieldAccessor);

    <T extends Model, FieldType, U> FieldType getFieldValue(Class<T> modelClass, SingleModelQueryExecutor<T, U> queryExecutor, FieldAccessor<T, FieldType> fieldAccessor);

    <T extends Model> void setFieldValue(Class<T> modelClass, String whereFieldName, String whereFieldValue, FieldUpdater<T> fieldUpdater);

    <T extends Model> void setFieldValue(Class<T> modelClass, String whereFieldName, long whereFieldValue, FieldUpdater<T> fieldUpdater);

    <T extends Model> void setFieldValue(final Class<T> modelClass, final String whereFieldName, final List<Long> whereFieldValues, final FieldUpdater<T> fieldUpdater);

    <T extends Model> List<T> copyFromDataStore(List<T> models);

    <T extends Model> T copyFromDataStore(T model);

    <T extends Model> long getNumberOfModels(Class<T> modelClass, String fieldName, long fieldValue);

    <T extends Model> long getNumberOfModels(Class<T> modelClass, String whereFieldName1, long whereFieldValue1, String whereFieldName2, boolean whereFieldValue2);

    <T extends Model> long getNumberOfModels(Class<T> modelClass, String whereFieldName1, long whereFieldValue1, String whereFieldName2, long whereFieldValue2);

    <T extends Model> long getNumberOfModels(Class<T> modelClass);

    interface ModelMapper<T, DTO> {
        DTO mapModel(T model);
    }

    interface SelfModelMapper<T> extends ModelMapper<T,T> {
    }

    interface ModelListMapper<T, DTO> {
        List<DTO> mapModels(List<T> models);
    }

    interface QueryExecutor<T extends Model, U> {
        List<T> executeQueries(U initialQuery);
    }

    interface SingleModelQueryExecutor<T extends Model, U> {
        T executeQueries(U initialQuery);
    }

    interface FieldAccessor<T extends Model, U> {
        @NonNull
        U getField(@Nullable T model);
    }

    interface FieldUpdater<T extends Model> {
        void updateField(@Nullable T model);
    }
}
