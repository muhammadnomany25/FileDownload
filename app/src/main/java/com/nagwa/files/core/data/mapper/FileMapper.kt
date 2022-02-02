package com.nagwa.files.core.data.mapper

import com.nagwa.files.core.data.source.local.entity.FileEntity
import com.nagwa.files.core.domain.FileModel

/**
 * @Author: Muhammad Noamany
 * @Email: muhammadnoamany@gmail.com
 */
class FileMapper : EntityMapper<FileEntity, FileModel> {
    override fun mapFromEntity(entity: FileEntity): FileModel {
        return FileModel(entity.id, entity.type, entity.url, entity.name)
    }

    override fun mapToEntity(domainModel: FileModel): FileEntity {
        return FileEntity(
            id = domainModel.id,
            type = domainModel.type,
            url = domainModel.url,
            name = domainModel.name
        )
    }

    override fun mapFromEntityList(entities: List<FileEntity>): List<FileModel> {
        return entities.map { mapFromEntity(it) }
    }

    override fun mapToEntityList(entities: List<FileModel>): List<FileEntity> {
        return entities.map { mapToEntity(it) }
    }

}