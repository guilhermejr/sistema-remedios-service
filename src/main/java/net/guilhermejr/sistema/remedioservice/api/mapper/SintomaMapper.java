package net.guilhermejr.sistema.remedioservice.api.mapper;

import net.guilhermejr.sistema.remedioservice.api.request.SintomaRequest;
import net.guilhermejr.sistema.remedioservice.api.response.SintomaResponse;
import net.guilhermejr.sistema.remedioservice.config.ModelMapperConfig;
import net.guilhermejr.sistema.remedioservice.domain.entity.Sintoma;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SintomaMapper extends ModelMapperConfig {

    public SintomaMapper() {

        this.modelMapper.createTypeMap(Sintoma.class, SintomaResponse.class)
                .addMapping(Sintoma::getRemedios, SintomaResponse::setRemedios);

    }

    public Sintoma mapObject(SintomaRequest sintomaRequest) {
        return this.mapObject(sintomaRequest, Sintoma.class);
    }

    public SintomaResponse mapObject(Sintoma sintoma) {
        return this.mapObject(sintoma, SintomaResponse.class);
    }

    public List<SintomaResponse> mapList(List<Sintoma> sintoma) {
        return this.mapList(sintoma, SintomaResponse.class);
    }

}
