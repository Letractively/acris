package sk.seges.acris.recorder.server.model.dto.configuration;

import sk.seges.acris.recorder.server.domain.jpa.JpaRecordingSession;
import sk.seges.sesam.pap.model.annotation.*;

@TransferObjectMapping(domainClass = JpaRecordingSession.class)
@Mapping(Mapping.MappingType.AUTOMATIC)
@GenerateHashcode(generate = true, type = TraversalType.DEFAULT)
@GenerateEquals(generate = true, type = TraversalType.DEFAULT)
public class RecordingSessionDTOConfiguration {

}