package ru.mertsalovda.core_api.mapper

/**
 * Базовый тип преобразователя типов
 *
 * @param Entity    Сущность из БД
 * @param Model     Модель (дата класс)
 */
abstract class BaseMapper<Entity, Model> {

    /**
     * Преобразовать из Entity в Model
     * @param entity    сущьность БД
     * @return          модель DTO
     */
    abstract fun map(entity: Entity?) : Model?

    /**
     * Преобразовать из Model в Entity
     * @param model     модель DTO
     * @return          сущьность БД
     */
    abstract fun reverseMap(model: Model?) : Entity?

    /**
     * Преобразовать из коллекции Entity в коллекцию Model
     * @param entityList    коллекция Entity
     * @return              коллекция Model
     */
    fun map(entityList: List<Entity>) : List<Model> {
        val modelList = arrayListOf<Model>()
        entityList.forEach { entity ->
            map(entity)?.let { model ->
                modelList.add(model)
            }
        }
        return modelList
    }

    /**
     * Преобразовать из коллекции Model в коллекцию Entity
     * @param modelList     коллекция Model
     * @return              коллекция Entity
     */
    fun reverseMap(modelList: List<Model>) : List<Entity> {
        val entityList = arrayListOf<Entity>()
        modelList.forEach { model ->
            reverseMap(model)?.let { entity ->
                entityList.add(entity)
            }
        }
        return entityList
    }
}