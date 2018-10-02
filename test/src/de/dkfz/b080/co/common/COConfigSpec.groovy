package de.dkfz.b080.co.common

import de.dkfz.b080.co.knowledge.metadata.MethodForSampleFromFilenameExtraction
import de.dkfz.roddy.config.ConfigurationError
import de.dkfz.roddy.config.ConfigurationValue;
import de.dkfz.roddy.core.ContextResource;
import de.dkfz.roddy.core.ExecutionContext;
import org.junit.ClassRule;
import spock.lang.Shared;
import spock.lang.Specification;

class COConfigSpec extends Specification {

    @ClassRule
    static final ContextResource contextResource = new ContextResource() {
        {
            before()
        }
    }

    @Shared
    static final ExecutionContext context = contextResource.createSimpleContext(COConfigSpec)

    void getSelectedSampleExtractionMethod(String selected, MethodForSampleFromFilenameExtraction result) {

        when:
        context.configurationValues.add(new ConfigurationValue("selectSampleExtractionMethod", selected))

        then:
        new COConfig(context).selectedSampleExtractionMethod == result

        where:
        selected    | result
        "version_1" | MethodForSampleFromFilenameExtraction.version_1
        "version_2" | MethodForSampleFromFilenameExtraction.version_2
    }

    void getSelectedSampleExtractionMethodWithExceptions(String selected, Class<Throwable> exception) {

        when:
        context.configurationValues.add(new ConfigurationValue("selectSampleExtractionMethod", selected))
        new COConfig(context).selectedSampleExtractionMethod

        then:
        def exc = thrown(exception)
        exc.message == "Value for selectSampleExtractionMethod is wrong, needs to be one of:\n\t- version_1\n\t- version_2"

        where:
        selected     | exception
        null         | ConfigurationError
        ""           | ConfigurationError
        "variant222" | ConfigurationError
    }
}