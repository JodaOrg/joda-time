/*
 *  Copyright 2026 Stephen Colebourne
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.joda.time;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collections;
import java.util.Locale;
import java.util.Set;

import org.joda.time.tz.DefaultNameProvider;
import org.joda.time.tz.NameProvider;
import org.joda.time.tz.Provider;
import org.joda.time.tz.UTCProvider;
import org.joda.time.tz.ZoneInfoProvider;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests service provider discovery for DateTimeZone.
 */
public class TestServiceProviderLoader extends TestCase {

    private static final String PROVIDER_PROPERTY = "org.joda.time.DateTimeZone.Provider";
    private static final String NAME_PROVIDER_PROPERTY = "org.joda.time.DateTimeZone.NameProvider";

    private ClassLoader originalClassLoader;
    private String originalProviderProperty;
    private String originalNameProviderProperty;

    public TestServiceProviderLoader(String name) {
        super(name);
    }

    public static TestSuite suite() {
        return new TestSuite(TestServiceProviderLoader.class);
    }

    @Override
    protected void setUp() throws Exception {
        originalClassLoader = Thread.currentThread().getContextClassLoader();
        originalProviderProperty = System.getProperty(PROVIDER_PROPERTY);
        originalNameProviderProperty = System.getProperty(NAME_PROVIDER_PROPERTY);
        System.getProperties().remove(PROVIDER_PROPERTY);
        System.getProperties().remove(NAME_PROVIDER_PROPERTY);
    }

    @Override
    protected void tearDown() throws Exception {
        Thread.currentThread().setContextClassLoader(originalClassLoader);
        restoreProperty(PROVIDER_PROPERTY, originalProviderProperty);
        restoreProperty(NAME_PROVIDER_PROPERTY, originalNameProviderProperty);
        DateTimeZone.setProvider(null);
        DateTimeZone.setNameProvider(null);
    }

    public void testProviderUsesFirstUsableSpiDeclaration() throws Exception {
        useSpiDirectory("valid");

        DateTimeZone.setProvider(null);

        assertEquals(FirstProvider.class, DateTimeZone.getProvider().getClass());
    }

    public void testNameProviderUsesFirstUsableSpiDeclaration() throws Exception {
        useSpiDirectory("valid");

        DateTimeZone.setNameProvider(null);

        assertEquals(FirstNameProvider.class, DateTimeZone.getNameProvider().getClass());
    }

    public void testSpiFailuresFallBackToBuiltInProviders() throws Exception {
        useSpiDirectory("unusable");

        DateTimeZone.setProvider(null);
        DateTimeZone.setNameProvider(null);

        assertEquals(ZoneInfoProvider.class, DateTimeZone.getProvider().getClass());
        assertEquals(DefaultNameProvider.class, DateTimeZone.getNameProvider().getClass());
    }

    public void testSystemPropertiesTakePrecedenceOverSpi() throws Exception {
        useSpiDirectory("valid");
        System.setProperty(PROVIDER_PROPERTY, UTCProvider.class.getName());
        System.setProperty(NAME_PROVIDER_PROPERTY, DefaultNameProvider.class.getName());

        DateTimeZone.setProvider(null);
        DateTimeZone.setNameProvider(null);

        assertEquals(UTCProvider.class, DateTimeZone.getProvider().getClass());
        assertEquals(DefaultNameProvider.class, DateTimeZone.getNameProvider().getClass());
    }

    public void testBadSystemPropertiesDoNotFallBackToSpi() throws Exception {
        useSpiDirectory("valid");
        System.setProperty(PROVIDER_PROPERTY, InvalidProvider.class.getName());
        try {
            DateTimeZone.setProvider(null);
            fail();
        } catch (RuntimeException ex) {
            // expected
        }

        System.getProperties().remove(PROVIDER_PROPERTY);
        System.setProperty(NAME_PROVIDER_PROPERTY, String.class.getName());
        try {
            DateTimeZone.setNameProvider(null);
            fail();
        } catch (RuntimeException ex) {
            // expected
        }
    }

    private void useSpiDirectory(String directory) throws Exception {
        URL url = new File("src/test/resources/spi/" + directory).toURI().toURL();
        ClassLoader loader = new URLClassLoader(new URL[] {url}, originalClassLoader);
        Thread.currentThread().setContextClassLoader(loader);
    }

    private static void restoreProperty(String name, String value) {
        if (value == null) {
            System.getProperties().remove(name);
        } else {
            System.setProperty(name, value);
        }
    }

    public static class UncreatableProvider implements Provider {
        public UncreatableProvider(String ignored) {
        }

        public Set<String> getAvailableIDs() {
            return Collections.singleton("UTC");
        }

        public DateTimeZone getZone(String id) {
            return DateTimeZone.UTC;
        }
    }

    public static class InvalidProvider implements Provider {
        public Set<String> getAvailableIDs() {
            return Collections.emptySet();
        }

        public DateTimeZone getZone(String id) {
            return null;
        }
    }

    public static class FirstProvider implements Provider {
        public Set<String> getAvailableIDs() {
            return Collections.singleton("UTC");
        }

        public DateTimeZone getZone(String id) {
            return DateTimeZone.UTC;
        }
    }

    public static class SecondProvider extends FirstProvider {
    }

    public static class UncreatableNameProvider implements NameProvider {
        public UncreatableNameProvider(String ignored) {
        }

        public String getShortName(Locale locale, String id, String nameKey) {
            return null;
        }

        public String getName(Locale locale, String id, String nameKey) {
            return null;
        }
    }

    public static class FirstNameProvider implements NameProvider {
        public String getShortName(Locale locale, String id, String nameKey) {
            return "first";
        }

        public String getName(Locale locale, String id, String nameKey) {
            return "first";
        }
    }

    public static class SecondNameProvider extends FirstNameProvider {
    }
}
