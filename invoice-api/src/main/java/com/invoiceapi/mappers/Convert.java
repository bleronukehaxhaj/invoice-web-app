package com.invoiceapi.mappers;

public interface Convert<Entity, Dto, Response> {
    Entity toEntityFromDto(Dto dto);
    Dto toDtoFromEntity(Entity entity);
    Entity toEntityFromResponse(Response response);
    Response toResponseFromEntity(Entity entity);
}