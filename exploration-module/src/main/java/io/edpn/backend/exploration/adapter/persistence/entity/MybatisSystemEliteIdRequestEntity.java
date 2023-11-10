package io.edpn.backend.exploration.adapter.persistence.entity;

import io.edpn.backend.exploration.application.dto.persistence.entity.SystemEliteIdRequestEntity;
import io.edpn.backend.util.Module;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class MybatisSystemEliteIdRequestEntity implements SystemEliteIdRequestEntity {
    private String systemName;
    private Module requestingModule;
}
