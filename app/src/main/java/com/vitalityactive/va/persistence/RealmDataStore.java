package com.vitalityactive.va.persistence;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.log.RealmLog;

public class RealmDataStore implements DataStore {
    private final RealmConfiguration realmConfiguration;

    public RealmDataStore(RealmConfiguration realmConfiguration) {
        this.realmConfiguration = realmConfiguration;
    }

    @Override
    public String toString() {
        return "RealmDataStore@" + realmConfiguration.getRealmFileName();
    }

    @Override
    public <T extends Model> boolean add(final T model) {
        return executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(model);
            }
        });
    }

    @Override
    public <T extends Model> boolean add(final List<T> models) {
        return executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (T model : models) {
                    realm.copyToRealm(model);
                }
            }
        });
    }

    @Override
    public <T extends Model> boolean addOrUpdate(final List<T> models) {
        return executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (T model : models) {
                    realm.copyToRealmOrUpdate(model);
                }
            }
        });
    }

    @Override
    public <T extends Model> boolean addOrUpdate(final T model) {
        return executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(model);
            }
        });
    }

    @Override
    public <T extends Model> boolean replaceAll(final Class<T> clazz, final List<T> list) {
        return executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.delete(clazz);
                realm.copyToRealmOrUpdate(list);
            }
        });
    }

    @Override
    public void clear() {
        executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.deleteAll();
            }
        });
    }

    @Override
    public <T extends Model> boolean hasModelInstance(Class<T> modelClass) {
        try (Realm realm = getRealm()) {
            return getQuery(modelClass, realm).findFirst() != null;
        }
    }

    @Override
    public <T extends Model> boolean hasModelInstance(Class<T> modelClass, String fieldName, String fieldValue) {
        try (Realm realm = getRealm()) {
            return getQuery(modelClass, realm).equalTo(fieldName, fieldValue).findFirst() != null;
        }
    }

    @Override
    public <T extends Model> boolean hasModelInstance(Class<T> modelClass, String fieldName, long fieldValue) {
        try (Realm realm = getRealm()) {
            return getQuery(modelClass, realm).equalTo(fieldName, fieldValue).findFirst() != null;
        }
    }

    @Override
    public <T extends Model, U> U getModelInstance(Class<T> modelClass, ModelMapper<T, U> modelMapper, String fieldName, String fieldValue) {
        try (Realm realm = getRealm()) {
            T model = realm.where(modelClass).equalTo(fieldName, fieldValue).findFirst();
            return model == null ? null : modelMapper.mapModel(model);
        }
    }

    @Override
    public <T extends Model, U> U getModelInstance(Class<T> modelClass, ModelMapper<T, U> modelMapper, String fieldName, int fieldValue) {
        try (Realm realm = getRealm()) {
            T model = realm.where(modelClass).equalTo(fieldName, fieldValue).findFirst();
            return model == null ? null : modelMapper.mapModel(model);
        }
    }

    @Override
    public <T extends Model, DTO> DTO getModelInstance(Class<T> modelClass, ModelMapper<T, DTO> modelMapper, String fieldName, long fieldValue) {
        try (Realm realm = getRealm()) {
            T model = realm.where(modelClass).equalTo(fieldName, fieldValue).findFirst();
            return model == null ? null : modelMapper.mapModel(model);
        }
    }

    @Override
    public <T extends Model, DTO> DTO getModelInstance(Class<T> modelClass, ModelMapper<T, DTO> modelMapper, String whereFieldName1, long whereFieldValue1, String whereFieldName2, long whereFieldValue2) {
        try (Realm realm = getRealm()) {
            T model = realm.where(modelClass)
                    .equalTo(whereFieldName1, whereFieldValue1)
                    .equalTo(whereFieldName2, whereFieldValue2)
                    .findFirst();
            return model == null ? null : modelMapper.mapModel(model);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Model, DTO, U> DTO getModelInstance(Class<T> modelClass, SingleModelQueryExecutor<T, U> queryExecutor, ModelMapper<T, DTO> modelMapper) {
        try (Realm realm = getRealm()) {
            return modelMapper.mapModel(queryExecutor.executeQueries((U)getQuery(modelClass, realm)));
        }
    }

    @Override
    public <T extends Model, DTO> DTO getFirstModelInstance(Class<T> modelClass, ModelMapper<T, DTO> modelMapper) {
        try (Realm realm = getRealm()) {
            T model = getQuery(modelClass, realm).findFirst();
            return model == null ? null : modelMapper.mapModel(model);
        }
    }

    @Override
    public <T extends Model> void removeAll(final Class<T> modelClass) {
        executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(modelClass).findAll().deleteAllFromRealm();
            }
        });
    }

    @Override
    public <T extends Model> void remove(final Class<T> modelClass, final String fieldName, final String fieldValue) {
        executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                T model = realm.where(modelClass).equalTo(fieldName, fieldValue).findFirst();
                if (model != null) {
                    RealmObject.deleteFromRealm(model);
                }
            }
        });
    }

    @Override
    public <T extends Model> void remove(final Class<T> modelClass, final String fieldName, final long fieldValue) {
        executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                T model = realm.where(modelClass).equalTo(fieldName, fieldValue).findFirst();
                if (model != null) {
                    RealmObject.deleteFromRealm(model);
                }
            }
        });
    }

    @Override
    public <T extends Model> void removeAll(final Class<T> modelClass, final String fieldName, final long fieldValue) {
        executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(modelClass).equalTo(fieldName, fieldValue).findAll().deleteAllFromRealm();
            }
        });
    }

    @Override
    public <T extends Model, DTO> List<DTO> getModels(Class<T> modelClass, ModelListMapper<T, DTO> modelListMapper) {
        try (Realm realm = getRealm()) {
            return modelListMapper.mapModels(realm.where(modelClass).findAll());
        }
    }

    @Override
    public <T extends Model, DTO> List<DTO> getModels(Class<T> modelClass, ModelListMapper<T, DTO> modelListMapper, final String fieldName, final String fieldValue) {
        try (Realm realm = getRealm()) {
            return modelListMapper.mapModels(realm.where(modelClass).equalTo(fieldName, fieldValue).findAll());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Model, DTO, U> List<DTO> getModels(Class<T> modelClass, QueryExecutor<T, U> queryExecutor, ModelListMapper<T, DTO> modelListMapper) {
        try (Realm realm = getRealm()) {
            return modelListMapper.mapModels(queryExecutor.executeQueries((U)getQuery(modelClass, realm)));
        }
    }

    @Override
    public <T extends Model, FieldType> FieldType getFieldValue(Class<T> modelClass, FieldAccessor<T, FieldType> fieldAccessor) {
        try (Realm realm = getRealm()) {
            return fieldAccessor.getField(getQuery(modelClass, realm).findFirst());
        }
    }

    @Override
    public <T extends Model, FieldType> FieldType getFieldValue(Class<T> modelClass, String fieldName, String fieldValue, FieldAccessor<T, FieldType> fieldAccessor) {
        try (Realm realm = getRealm()) {
            return fieldAccessor.getField(realm.where(modelClass).equalTo(fieldName, fieldValue).findFirst());
        }
    }

    @Override
    public <T extends Model, FieldType> FieldType getFieldValue(Class<T> modelClass, String fieldName, long fieldValue, FieldAccessor<T, FieldType> fieldAccessor) {
        try (Realm realm = getRealm()) {
            return fieldAccessor.getField(realm.where(modelClass).equalTo(fieldName, fieldValue).findFirst());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Model, FieldType, U> FieldType getFieldValue(Class<T> modelClass, SingleModelQueryExecutor<T, U> queryExecutor, FieldAccessor<T, FieldType> fieldAccessor) {
        try (Realm realm = getRealm()) {
            return fieldAccessor.getField(queryExecutor.executeQueries((U)getQuery(modelClass, realm)));
        }
    }

    @Override
    public <T extends Model> void setFieldValue(final Class<T> modelClass, final String whereFieldName, final String whereFieldValue, final FieldUpdater<T> fieldUpdater) {
        executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                fieldUpdater.updateField(realm.where(modelClass).equalTo(whereFieldName, whereFieldValue).findFirst());
            }
        });
    }

    @Override
    public <T extends Model> void setFieldValue(final Class<T> modelClass, final String whereFieldName, final long whereFieldValue, final FieldUpdater<T> fieldUpdater) {
        executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                fieldUpdater.updateField(realm.where(modelClass).equalTo(whereFieldName, whereFieldValue).findFirst());
            }
        });
    }

    @Override
    public <T extends Model> void setFieldValue(final Class<T> modelClass, final String whereFieldName, final List<Long> whereFieldValues, final FieldUpdater<T> fieldUpdater) {
        executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Long[] longs = new Long[whereFieldValues.size()];
                whereFieldValues.toArray(longs);
                RealmResults<T> matching = realm.where(modelClass).in(whereFieldName, longs).findAll();
                for (T match : matching) {
                    fieldUpdater.updateField(match);
                }
            }
        });
    }

    @Override
    public <T extends Model> List<T> copyFromDataStore(List<T> models) {
        // We should never call getRealm() without the try-with-resources block, because that ensures that the Realm instance is closed properly:
        // https://realm.io/docs/java/latest/#closing-realm-instances
        // Because we use a thread pool, strange stale data issues crop up if we re-use a thread containing an open Realm and do queries
        // (to refresh a Realm instance on a thread without a Looper, i.e. all our background threads, you have to explicitly call waitForChange()
        // https://realm.io/docs/java/latest/#auto-refresh

        try (Realm realm = getRealm()) {
            return realm.copyFromRealm(models);
        }
    }

    @Override
    public <T extends Model> T copyFromDataStore(T model) {
        // We should never call getRealm() without the try-with-resources block, because that ensures that the Realm instance is closed properly:
        // https://realm.io/docs/java/latest/#closing-realm-instances
        // Because we use a thread pool, strange stale data issues crop up if we re-use a thread containing an open Realm and do queries
        // (to refresh a Realm instance on a thread without a Looper, i.e. all our background threads, you have to explicitly call waitForChange()
        // https://realm.io/docs/java/latest/#auto-refresh

        try (Realm realm = getRealm()) {
            return realm.copyFromRealm(model);
        }
    }

    @Override
    public <T extends Model> long getNumberOfModels(Class<T> modelClass, String fieldName, long fieldValue) {
        try (Realm realm = getRealm()) {
            return getQuery(modelClass, realm).equalTo(fieldName, fieldValue).count();
        }
    }

    @Override
    public <T extends Model> long getNumberOfModels(Class<T> modelClass, String whereFieldName1, long whereFieldValue1, String whereFieldName2, boolean whereFieldValue2) {
        try (Realm realm = getRealm()) {
            return getQuery(modelClass, realm)
                    .equalTo(whereFieldName1, whereFieldValue1)
                    .equalTo(whereFieldName2, whereFieldValue2)
                    .count();
        }
    }

    @Override
    public <T extends Model> long getNumberOfModels(Class<T> modelClass, String whereFieldName1, long whereFieldValue1, String whereFieldName2, long whereFieldValue2) {
        try (Realm realm = getRealm()) {
            return getQuery(modelClass, realm)
                    .equalTo(whereFieldName1, whereFieldValue1)
                    .equalTo(whereFieldName2, whereFieldValue2)
                    .count();
        }
    }

    @Override
    public <T extends Model> long getNumberOfModels(Class<T> modelClass) {
        try (Realm realm = getRealm()) {
            return getQuery(modelClass, realm).count();
        }
    }

    private <T extends Model> RealmQuery<T> getQuery(Class<T> modelClass, Realm realm) {
        return realm.where(modelClass);
    }

    private boolean executeTransaction(Realm.Transaction transaction) {
        try (Realm realm = getRealm()) {
            realm.beginTransaction();
            try {
                transaction.execute(realm);
                realm.commitTransaction();
                return true;
            } catch (Throwable e) {
                RealmLog.warn(e, "Failed to execute transaction");

                if (realm.isInTransaction()) {
                    realm.cancelTransaction();
                } else {
                    RealmLog.warn("Could not cancel transaction, not currently in a transaction.");
                }
                return false;
            }
        }
    }

    private Realm getRealm() {
        return Realm.getInstance(realmConfiguration);
    }
}
