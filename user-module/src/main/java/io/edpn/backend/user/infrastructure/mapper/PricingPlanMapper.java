package io.edpn.backend.user.infrastructure.mapper;

import io.edpn.backend.user.domain.model.PricingPlan;
import io.edpn.backend.user.infrastructure.entity.PricingPlanEntity;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PricingPlanMapper {

    public PricingPlan map(PricingPlanEntity entity) {
        return PricingPlan.builder()
                .id(entity.getId())
                .name(entity.getName())
                .capacityPerMinute(entity.getCapacityPerMinute())
                .build();
    }

    public PricingPlanEntity map(PricingPlan entity) {
        return PricingPlanEntity.builder()
                .id(entity.getId())
                .name(entity.getName())
                .capacityPerMinute(entity.getCapacityPerMinute())
                .build();
    }
}
