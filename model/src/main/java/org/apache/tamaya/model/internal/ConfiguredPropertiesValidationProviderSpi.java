package org.apache.tamaya.model.internal;

import org.apache.tamaya.model.Validation;
import org.apache.tamaya.model.spi.AreaValidation;
import org.apache.tamaya.model.spi.ConfigValidationsReader;
import org.apache.tamaya.model.spi.ParameterValidation;
import org.apache.tamaya.model.spi.ValidationProviderSpi;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Validation provider that reads model metadata from property files from
 * {@code classpath*:META-INF/configmodel.properties} in the following format:
 * <pre>
 * ###################################################################################
 * # Example of a configuration metamodel expressed via properties.
 * ####################################################################################
 *
 * # Metamodel information
 * [model].provider=ConfigModel Extension
 *
 * ####################################################################################
 * # Description of Configuration Sections (minimal, can be extended by other modules).
 * # By default its interpreted as a section !
 * ####################################################################################
 *
 * # a (section)
 * {model}a.class=Section
 * {model}a.params2.class=Parameter
 * {model}a.params2.type=String
 * {model}a.params2.required=true
 * {model}a.params2.description=a required parameter
 *
 * {model}a.paramInt.class=Parameter
 * {model}a.paramInt.ref=MyNumber
 * {model}a.paramInt.description=an optional parameter (default)
 *
 * {model}a._number.class=Parameter
 * {model}a._number.type=Integer
 * {model}a._number.deprecated=true
 * {model}a._number.mappedTo=a.paramInt
 *
 * # a.b.c (section)
 * {model}a.b.c.class=Section
 * {model}a.b.c.description=Just a test section
 *
 * # a.b.c.aRequiredSection (section)
 * {model}a.b.c.aRequiredSection.class=Section
 * {model}a.b.c.aRequiredSection.required=true
 * {model}a.b.c.aRequiredSection.description=A section containing required parameters is called a required section.\
 * Sections can also explicitly be defined to be required, but without\
 * specifying the paramteres to be contained.,
 *
 * # a.b.c.aRequiredSection.subsection (section)
 * {model}a.b.c.aRequiredSection.subsection.class=Section
 *
 * {model}a.b.c.aRequiredSection.subsection.param0.class=Parameter
 * {model}a.b.c.aRequiredSection.subsection.param0.type=String
 * {model}a.b.c.aRequiredSection.subsection.param0.description=a minmally documented String parameter
 * # A minmal String parameter
 * {model}a.b.c.aRequiredSection.subsection.param00.class=Parameter
 * {model}a.b.c.aRequiredSection.subsection.param00.type=String
 *
 * # a.b.c.aRequiredSection.subsection (section)
 * {model}a.b.c.aRequiredSection.subsection.param1.class=Parameter
 * {model}a.b.c.aRequiredSection.subsection.param1.type = String
 * {model}a.b.c.aRequiredSection.subsection.param1.required = true
 * {model}a.b.c.aRequiredSection.subsection.intParam.class=Parameter
 * {model}a.b.c.aRequiredSection.subsection.intParam.type = Integer
 * {model}a.b.c.aRequiredSection.subsection.intParam.description=an optional parameter (default)
 *
 * # a.b.c.aRequiredSection.nonempty-subsection (section)
 * {model}a.b.c.aRequiredSection.nonempty-subsection.class=Section
 * {model}a.b.c.aRequiredSection.nonempty-subsection.required=true
 *
 * # a.b.c.aRequiredSection.optional-subsection (section)
 * {model}a.b.c.aRequiredSection.optional-subsection.class=Section
 *
 * # a.b.c.aValidatedSection (section)
 * {model}a.b.c.aValidatedSection.class=Section
 * {model}a.b.c.aValidatedSection.description=A validated section.
 * {model}a.b.c.aValidatedSection.validations=org.apache.tamaya.model.TestValidator
 * </pre>
 */
public class ConfiguredPropertiesValidationProviderSpi implements ValidationProviderSpi {

    /** The logger. */
    private static final Logger LOG = Logger.getLogger(ConfiguredPropertiesValidationProviderSpi.class.getName());
    /** The validations read. */
    private List<Validation> validations = new ArrayList<>();

    public ConfiguredPropertiesValidationProviderSpi() {
        try {
            Enumeration<URL> configs = getClass().getClassLoader().getResources("META-INF/configmodel.properties");
            while (configs.hasMoreElements()) {
                URL config = configs.nextElement();
                try (InputStream is = config.openStream()) {
                    Properties props = new Properties();
                    props.load(is);
                    validations.addAll(ConfigValidationsReader.loadValidations(props, config.toString()));
                } catch (Exception e) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE,
                            "Error loading config metadata from " + config, e);
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE,
                    "Error loading config metadata from META-INF/configmodel.properties", e);
        }
        validations = Collections.unmodifiableList(validations);
    }


    @Override
    public Collection<Validation> getValidations() {
        return validations;
    }
}
