package com.asian.auto.hub.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.asian.auto.hub.dto.CarPurchaseDto;
import com.asian.auto.hub.dto.CarPurchaseResponseDto;
import com.asian.auto.hub.model.CarPurchase;

@Mapper(componentModel = "spring")
public interface CarPurchaseMapper {

    // Entity → ResponseDTO
    @Mapping(source = "purchasedBy.firstname",  target = "purchasedByFirstname")
    @Mapping(source = "purchasedBy.email", target = "purchasedByEmail")
    @Mapping(source = "created_by",        target = "createdBy")
    @Mapping(source = "updated_by",        target = "updatedBy")
    CarPurchaseResponseDto toResponseDTO(CarPurchase carPurchase);

    // RequestDTO → Entity (ignore audit + relations set in service)
    @Mapping(target = "id",          ignore = true)
    @Mapping(target = "purchasedBy", ignore = true)
    @Mapping(target = "expenses",    ignore = true)
    @Mapping(target = "created_by",  ignore = true)
    @Mapping(target = "updated_by",  ignore = true)
    @Mapping(target = "createdOn",   ignore = true)
    @Mapping(target = "updatedOn",   ignore = true)
    CarPurchase toEntity(CarPurchaseDto requestDTO);

    // Update existing entity from RequestDTO
    @Mapping(target = "id",          ignore = true)
    @Mapping(target = "purchasedBy", ignore = true)
    @Mapping(target = "expenses",    ignore = true)
    @Mapping(target = "created_by",  ignore = true)
    @Mapping(target = "updated_by",  ignore = true)
    @Mapping(target = "createdOn",   ignore = true)
    @Mapping(target = "updatedOn",   ignore = true)
    void updateEntityFromDTO(CarPurchaseDto dto, @MappingTarget CarPurchase carPurchase);
}