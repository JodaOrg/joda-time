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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Collection;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A utility for looking up service provider implementations.
 *
 * @author Brett Okken
 * @since 2.15
 */
final class ServiceProviderLoader {

    private static final Logger LOGGER = Logger.getLogger(ServiceProviderLoader.class.getName());

    /**
     * Tests whether a service provider is valid for use.
     *
     * @param <T>  the service provider type
     */
    static interface Predicate<T> {

        /**
         * Tests the service provider.
         *
         * @param value  the service provider, not null
         * @return true if the service provider is valid
         */
        boolean test(T value);
    }

    /**
     * Loads the configured service provider, or the first discovered service provider that can be
     * created and validated. A configured provider failure is fatal, while a discovered provider
     * failure is ignored so that the next provider can be attempted.
     *
     * @param <T>  the service provider type
     * @param property  the system property used to configure the provider, not null
     * @param service  the service provider type, not null
     * @param predicate  the predicate used to validate providers, null to accept all
     * @return the configured provider or first valid discovered provider, null if none
     * @throws RuntimeException if a configured provider cannot be loaded, created or validated
     */
    static <T> T load(String property, Class<T> service, Predicate<? super T> predicate) {
        String propertyClass = null;
        try {
            propertyClass = System.getProperty(property);
        } catch (SecurityException ex) {
            // ignored
        }
        if (propertyClass != null) {
            try {
                ClassLoader loader = service.getClassLoader();
                Class<?> cls = Class.forName(propertyClass, false, loader);
                if (!service.isAssignableFrom(cls)) {
                    throw new IllegalArgumentException(
                            "System property referred to class that does not implement " + service);
                }
                T provider = cls.asSubclass(service).getConstructor().newInstance();
                if (!test(predicate, provider)) {
                    throw new IllegalArgumentException("System property referred to an invalid provider");
                }
                return provider;
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }

        for (Class<? extends T> providerClass : lookupServiceProviders(service)) {
            try {
                T provider = providerClass.getConstructor().newInstance();
                if (test(predicate, provider)) {
                    return provider;
                }
                LOGGER.log(Level.FINE, "Service provider failed validation " + providerClass.getName());
            } catch (Exception ex) {
                LOGGER.log(Level.FINE, "Unable to create service provider " + providerClass.getName(), ex);
            } catch (LinkageError ex) {
                LOGGER.log(Level.FINE, "Unable to create service provider " + providerClass.getName(), ex);
            }
        }
        return null;
    }

    private static <T> boolean test(Predicate<? super T> predicate, T provider) {
        return predicate == null || predicate.test(provider);
    }

    private static <T> Collection<Class<? extends T>> lookupServiceProviders(Class<T> service) {
        String serviceName = service.getName();
        String resourceName = "META-INF/services/" + serviceName;
        ClassLoader loader = getClassLoader(service);
        Set<Class<? extends T>> providers = new LinkedHashSet<Class<? extends T>>();

        if (loader == null) {
            return providers;
        }
        try {
            Enumeration<URL> resources = loader.getResources(resourceName);
            while (resources.hasMoreElements()) {
                loadResource(service, loader, resources.nextElement(), providers);
            }
        } catch (IOException ex) {
            LOGGER.log(Level.FINE, "Unable to find service provider files for " + serviceName, ex);
        } catch (SecurityException ex) {
            LOGGER.log(Level.FINE, "Unable to find service provider files for " + serviceName, ex);
        }
        return providers;
    }

    private static <T> void loadResource(
            Class<T> service, ClassLoader loader, URL resource, Set<Class<? extends T>> providers) {
        InputStream in = null;
        try {
            in = resource.openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                int comment = line.indexOf('#');
                if (comment >= 0) {
                    line = line.substring(0, comment);
                }
                line = line.trim();
                if (line.length() > 0) {
                    loadClass(service, loader, resource, line, providers);
                }
            }
        } catch (UnsupportedEncodingException ex) {
            throw new AssertionError(ex);
        } catch (IOException ex) {
            LOGGER.log(Level.FINE, "Unable to read service provider file " + resource, ex);
        } catch (SecurityException ex) {
            LOGGER.log(Level.FINE, "Unable to read service provider file " + resource, ex);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {
                    LOGGER.log(Level.FINE, "Unable to close service provider file " + resource, ex);
                }
            }
        }
    }

    private static <T> void loadClass(
            Class<T> service, ClassLoader loader, URL resource, String className,
            Set<Class<? extends T>> providers) {
        try {
            Class<?> provider = Class.forName(className, false, loader);
            if (service.isAssignableFrom(provider)) {
                providers.add(provider.asSubclass(service));
            } else {
                LOGGER.log(
                        Level.FINE,
                        className + " in " + resource + " does not implement " + service.getName());
            }
        } catch (ClassNotFoundException ex) {
            LOGGER.log(Level.FINE, "Unable to load service provider " + className + " from " + resource, ex);
        } catch (LinkageError ex) {
            LOGGER.log(Level.FINE, "Unable to load service provider " + className + " from " + resource, ex);
        } catch (SecurityException ex) {
            LOGGER.log(Level.FINE, "Unable to load service provider " + className + " from " + resource, ex);
        }
    }

    private static ClassLoader getClassLoader(Class<?> service) {
        ClassLoader loader = null;
        try {
            loader = Thread.currentThread().getContextClassLoader();
        } catch (SecurityException ex) {
            LOGGER.log(Level.FINE, "Unable to access the context class loader", ex);
        }
        if (loader == null) {
            try {
                loader = service.getClassLoader();
            } catch (SecurityException ex) {
                LOGGER.log(Level.FINE, "Unable to access the service class loader", ex);
            }
        }
        if (loader == null) {
            try {
                loader = ServiceProviderLoader.class.getClassLoader();
            } catch (SecurityException ex) {
                LOGGER.log(Level.FINE, "Unable to access the service provider loader class loader", ex);
            }
        }
        if (loader == null) {
            try {
                loader = ClassLoader.getSystemClassLoader();
            } catch (SecurityException ex) {
                LOGGER.log(Level.FINE, "Unable to access the system class loader", ex);
            }
        }
        return loader;
    }

}
