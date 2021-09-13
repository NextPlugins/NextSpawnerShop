package br.com.nextplugins.spawnershop.api.model.discount;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public final class Discount {

    private final String id;

    private final double value;

    private final String permission, groupName;

}
