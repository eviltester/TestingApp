package com.javafortesters.pulp.api.entities.payloads.responses;

import com.javafortesters.pulp.api.DomainToEntityConvertor;
import com.javafortesters.pulp.domain.groupings.PulpData;
import com.javafortesters.pulp.domain.objects.PulpAuthor;
import com.javafortesters.pulp.domain.objects.PulpBook;

public class ApiResponse {

    public EntityLists data;     // entities returned and impacted by your request
    public EntityLists created;  // entities created in response to your message
    public EntityLists amended;  // entities created in response to your message
    public ErrorList errors;     // any errors
}
