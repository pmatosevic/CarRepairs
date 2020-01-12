package org.infiniteam.autoservice.service;

import org.infiniteam.autoservice.model.AutoService;
import org.infiniteam.autoservice.model.ServiceLabor;

import java.util.List;

public interface ServiceLaborService {

    ServiceLabor fetch(long serviceLaborId);

    List<ServiceLabor> findAllByAutoService(AutoService autoService);

    ServiceLabor addOrModify(ServiceLabor serviceLabor);

    void delete(ServiceLabor labor);

}
