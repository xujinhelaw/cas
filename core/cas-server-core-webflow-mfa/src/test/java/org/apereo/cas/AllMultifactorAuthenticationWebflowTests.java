package org.apereo.cas;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.runner.RunWith;


/**
 * The {@link AllCoreTestsSuite} is responsible for
 * running all cas test cases.
 *
 * @author Misagh Moayyed
 * @since 4.2.0
 */
@SelectClasses({
        AdaptiveMultifactorAuthenticationPolicyEventResolverTests.class,
        MultifactorAuthenticationTests.class,
        AdaptiveMultifactorAuthenticationPolicyEventResolverTests.class,
        GroovyScriptMultifactorAuthenticationPolicyEventResolverTests.class,
        TimedMultifactorAuthenticationPolicyEventResolverTests.class
})
@RunWith(JUnitPlatform.class)
public class AllMultifactorAuthenticationWebflowTests {
}
