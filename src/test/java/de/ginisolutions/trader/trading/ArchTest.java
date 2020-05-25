package de.ginisolutions.trader.trading;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {

        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("de.ginisolutions.trader.trading");

        noClasses()
            .that()
                .resideInAnyPackage("de.ginisolutions.trader.trading.service..")
            .or()
                .resideInAnyPackage("de.ginisolutions.trader.trading.repository..")
            .should().dependOnClassesThat()
                .resideInAnyPackage("..de.ginisolutions.trader.trading.web..")
        .because("Services and repositories should not depend on web layer")
        .check(importedClasses);
    }
}
