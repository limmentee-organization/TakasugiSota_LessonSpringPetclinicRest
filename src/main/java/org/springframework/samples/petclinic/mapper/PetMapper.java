package org.springframework.samples.petclinic.mapper;

import java.util.Collection;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.rest.dto.PetDto;
import org.springframework.samples.petclinic.rest.dto.PetFieldsDto;
import org.springframework.samples.petclinic.rest.dto.PetTypeDto;

@Mapper 
public interface PetMapper {

	@Mapping(source = "owner.id", target = "ownerId")
	PetDto toPetDto(Pet pet);

	Collection<PetDto> toPetsDto(Collection<Pet> pets);

	Collection<Pet> toPets(Collection<PetDto> petDtos);

	Pet toPet(PetDto petDto);

	Pet toPet(PetFieldsDto petFieldsDto);

	PetTypeDto toPetTypeDto(PetType petType);

	PetType toPetType(PetTypeDto petTypeDto);

	Collection<PetTypeDto> toPetTypeDtos(Collection<PetType> petTypes);

}
