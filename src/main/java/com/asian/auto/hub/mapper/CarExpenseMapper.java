package com.asian.auto.hub.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.asian.auto.hub.dto.CarExpenseDto;
import com.asian.auto.hub.dto.CarExpenseResponseDto;
import com.asian.auto.hub.model.CarExpense;

@Mapper(componentModel = "spring")
public interface CarExpenseMapper {

    // Entity → ResponseDTO
    @Mapping(source = "carPurchase.id",          target = "carPurchaseId")
    @Mapping(source = "carPurchase.numberPlate",  target = "carNumberPlate")
    @Mapping(source = "paidBy.firstname",              target = "paidByFirstname")
    @Mapping(source = "created_by",               target = "createdBy")
    @Mapping(source = "updated_by",               target = "updatedBy")
    CarExpenseResponseDto toResponseDTO(CarExpense carExpense);

    // RequestDTO → Entity
    @Mapping(target = "id",          ignore = true)
    @Mapping(target = "carPurchase", ignore = true)
    @Mapping(target = "paidBy",      ignore = true)
    @Mapping(target = "created_by",  ignore = true)
    @Mapping(target = "updated_by",  ignore = true)
    @Mapping(target = "createdOn",   ignore = true)
    @Mapping(target = "updatedOn",   ignore = true)
    CarExpense toEntity(CarExpenseDto requestDTO);

    // Update existing entity from RequestDTO
    @Mapping(target = "id",          ignore = true)
    @Mapping(target = "carPurchase", ignore = true)
    @Mapping(target = "paidBy",      ignore = true)
    @Mapping(target = "created_by",  ignore = true)
    @Mapping(target = "updated_by",  ignore = true)
    @Mapping(target = "createdOn",   ignore = true)
    @Mapping(target = "updatedOn",   ignore = true)
    void updateEntityFromDTO(CarExpenseDto dto, @MappingTarget CarExpense carExpense);
}