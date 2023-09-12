package net.guilhermejr.sistema.remedioservice.api.mapper;

import net.guilhermejr.sistema.remedioservice.api.request.RemedioRequest;
import net.guilhermejr.sistema.remedioservice.api.response.RemedioResponse;
import net.guilhermejr.sistema.remedioservice.config.ModelMapperConfig;
import net.guilhermejr.sistema.remedioservice.domain.entity.Remedio;
import net.guilhermejr.sistema.remedioservice.domain.entity.Sintoma;
import net.guilhermejr.sistema.remedioservice.util.ConverteStringUtil;
import org.modelmapper.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class RemedioMapper extends ModelMapperConfig {

    public RemedioMapper(ConverteStringUtil converteStringUtil) {

        Converter<String, LocalDate> converterData = ctx -> converteStringUtil.toLocalDate(ctx.getSource());

        Converter<Set<Long>, Set<Sintoma>> sintomasConverter = ctx -> {
            Set<Sintoma> sintomas = new HashSet<>();
            ctx.getSource().forEach(id -> {
                sintomas.add(Sintoma.builder().id(id).build());
            });
            return sintomas;
        };

        this.modelMapper.createTypeMap(RemedioRequest.class, Remedio.class)
                .addMappings(mapper ->mapper.using(converterData).map(RemedioRequest::getValidade, Remedio::setValidade))
                .addMappings(mapper -> mapper.using(sintomasConverter).map(RemedioRequest::getSintomas, Remedio::setSintomas));

    }

    public Remedio mapObject(RemedioRequest remedioRequest) {
        return this.mapObject(remedioRequest, Remedio.class);
    }

    public RemedioResponse mapObject(Remedio remedio) {
        return this.mapObject(remedio, RemedioResponse.class);
    }

    public List<RemedioResponse> mapList(List<Remedio> remedios) {
        return this.mapList(remedios, RemedioResponse.class);
    }
}
