package com.fema.tcc.usecases.dietaryRestriction;

import com.fema.tcc.domains.dietaryRestriction.DietaryRestriction;
import com.fema.tcc.gateways.DietaryRestrictionGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DietaryRestrictionGetAllUseCase {

    private final DietaryRestrictionGateway dietaryRestrictionGateway;

    public List<DietaryRestriction> execute() {
        return dietaryRestrictionGateway.findAll();
    }
}
