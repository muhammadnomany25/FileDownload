package com.nagwa.files.core.data.mapper

/**
 * Used to map to and from domain models
 * @Author: Muhammad Noamany
 * @Email: muhammadnoamany@gmail.com
 */
interface EntityMapper<Entity, DomainModel> {
    fun mapFromEntity(entity: Entity): DomainModel
    fun mapToEntity(domainModel: DomainModel): Entity?
    fun mapFromEntityList(entities: List<Entity>): List<DomainModel>
    fun mapToEntityList(entities: List<DomainModel>): List<Entity>
}